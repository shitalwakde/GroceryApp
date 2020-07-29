package com.app.features.address;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;
import com.app.callback.AddressListLisener;
import com.app.util.AppUtils;
import com.app.util.RestClient;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.MyViewHolder> {
    ArrayList<AddressModel> mdata;
    AddressListLisener listLisener;

    public AddressListAdapter(ArrayList<AddressModel> mdata, AddressListLisener listLisener) {
        this.mdata = mdata;
        this.listLisener = listLisener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_list_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AddressModel addressModel = mdata.get(position);
        holder.rbName.setChecked(false);
        if(addressModel.getIsDefault().equals("Yes")){
            //holder.rbName.setChecked(true);
            holder.tv_default.setVisibility(View.VISIBLE);
            holder.tv_setDefault.setVisibility(View.GONE);
        }else{
            holder.tv_default.setVisibility(View.GONE);
            holder.tv_setDefault.setVisibility(View.VISIBLE);
        }

        holder.rbName.setText(addressModel.getName());
        holder.tv_mobile.setText(addressModel.getMobile());
        holder.tv_house.setText(addressModel.getHouseNo()+", "+addressModel.getArea());
        holder.tv_state.setText(addressModel.getState()+", "+addressModel.getCity()+"- "+addressModel.getPincode());
        if(addressModel.getLandmark().equals("")){
            holder.tv_Landmark.setVisibility(View.GONE);
        }else{
            holder.tv_Landmark.setVisibility(View.VISIBLE);
            holder.tv_Landmark.setText(addressModel.getLandmark());
        }
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        RadioButton rbName;
        ImageView iv_remove, iv_edit;
        TextView tv_house, tv_Landmark, tv_state, tv_mobile, tv_default, tv_setDefault;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rbName = (RadioButton)itemView.findViewById(R.id.rbName);
            iv_remove = (ImageView)itemView.findViewById(R.id.iv_remove);
            iv_edit = (ImageView)itemView.findViewById(R.id.iv_edit);
            tv_house = (TextView)itemView.findViewById(R.id.tv_house);
            tv_Landmark = (TextView)itemView.findViewById(R.id.tv_Landmark);
            tv_state = (TextView)itemView.findViewById(R.id.tv_state);
            tv_mobile = (TextView)itemView.findViewById(R.id.tv_mobile);
            tv_default = (TextView)itemView.findViewById(R.id.tv_default);
            tv_setDefault = (TextView)itemView.findViewById(R.id.tv_setDefault);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listLisener.AddressListLisenerClick(mdata.get(getAdapterPosition()).getDeliveryId());
                }
            });

            iv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listLisener.EditAddressLisenerClick(mdata.get(getAdapterPosition()).getDeliveryId());
                }
            });

            iv_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(itemView.getContext());
                    builder.setMessage("Are you sure want to remove the addressa ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setDefaultRemove("remove", getAdapterPosition());
                            dialog.dismiss();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();

                }
            });

            tv_setDefault.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDefault(mdata.get(getAdapterPosition()), getAdapterPosition());
                }
            });

        }


        private void setDefault(AddressModel addressModel, int position){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("userId", AppUtils.getUserDetails(itemView.getContext()).getLoginId());
            jsonObject.addProperty("deliveryId", mdata.get(getAdapterPosition()).getDeliveryId());
            jsonObject.addProperty("flag", "Yes");

            new RestClient().getApiService().setDefaultRemove(jsonObject, new Callback<AddressModel>() {
                @Override
                public void success(AddressModel address, Response response) {
                    if(address.getSuccess().equals("1")){
                        addressModel.setIsDefault("Yes");
                        listLisener.AddressListLisenerClick(mdata.get(getAdapterPosition()).getDeliveryId());
                        notifyItemChanged(position);
                        notifyDataSetChanged();
                    }else {
                        Toast.makeText(itemView.getContext(), address.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(itemView.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }


        private void setDefaultRemove(String remove, int position){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("userId", AppUtils.getUserDetails(itemView.getContext()).getLoginId());
            jsonObject.addProperty("deliveryId", mdata.get(getAdapterPosition()).getDeliveryId());
            jsonObject.addProperty("flag", remove);

            new RestClient().getApiService().setDefaultRemove(jsonObject, new Callback<AddressModel>() {
                @Override
                public void success(AddressModel addressModel, Response response) {
                    if(addressModel.getSuccess().equals("1")){
                        mdata.remove(position);
                        notifyItemChanged(position);
                        notifyDataSetChanged();
                    }else {
                        //Toast.makeText(itemView.getContext(), addressModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(itemView.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
