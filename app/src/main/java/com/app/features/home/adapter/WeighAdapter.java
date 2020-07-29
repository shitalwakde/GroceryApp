package com.app.features.home.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.R;
import com.app.features.home.model.Product;
import com.app.features.navmenu.WishListAdapter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WeighAdapter extends RecyclerView.Adapter<WeighAdapter.MyViewHolder> {
    ArrayList<Product> mdata;
    private WishListAdapter.OnWeightItemCLickListener listener;

    public WeighAdapter(ArrayList<Product> mdata, WishListAdapter.OnWeightItemCLickListener listener) {
        this.mdata = mdata;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product products= mdata.get(position);
        holder.tv_weight.setText(products.getQuantity());

        if(products.getMaxProductQuantity().equals("0")){
            holder.tv_outOfStock.setVisibility(View.VISIBLE);
            //holder.rl_view.setVisibility(View.GONE);
        }else {
            holder.tv_outOfStock.setVisibility(View.GONE);
        }

        if(products.getDiscount().equals("0")){
            holder.tv_price.setVisibility(View.GONE);
        }else{
            holder.tv_price.setVisibility(View.VISIBLE);
            holder.tv_price.setText("\u20B9 "+String.valueOf(products.getGrossAmount()));
            holder.tv_price.setPaintFlags(holder.tv_price.getPaintFlags()
                    | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.tv_disc_price.setText("\u20B9 "+String.valueOf(products.getFinalAmount()));
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_weight, tv_price, tv_disc_price, tv_outOfStock;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_outOfStock = (TextView)itemView.findViewById(R.id.tv_outOfStock);
            tv_weight = (TextView)itemView.findViewById(R.id.tv_weight);
            tv_price = (TextView)itemView.findViewById(R.id.tv_price);
            tv_disc_price = (TextView)itemView.findViewById(R.id.tv_disc_price);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                        listener.onWeightClicked(mdata.get(getAdapterPosition()));
                }
            });
        }
    }
}
