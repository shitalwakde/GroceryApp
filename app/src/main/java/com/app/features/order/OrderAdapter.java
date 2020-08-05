package com.app.features.order;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;
import com.app.callback.HomeClickLisener;
import com.app.callback.OrderDetailLisener;
import com.app.features.cart.CartActivity;
import com.app.features.home.model.Category;
import com.app.util.RestClient;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    ArrayList<OrderList> mdata;
    OrderDetailLisener lisener;

    public OrderAdapter(ArrayList<OrderList> mdata, OrderDetailLisener lisener) {
        this.mdata = mdata;
        this.lisener = lisener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_adapter_temp, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrderList category = mdata.get(position);
        holder.tv_orderId.setText(category.getOrderId());
        //holder.tv_orderDate.setText(category.getOrderDate());
        holder.tv_deliveryDate.setText(category.getOrderDate());
        if(category.getOrderStatus().equals("Pending")){
            holder.tv_orderStatus.setTextColor(Color.parseColor("#f47443"));
        }else if(category.getOrderStatus().equals("Cancelled")){
            holder.tv_orderStatus.setTextColor(Color.parseColor("#E72408"));
        }else {
            holder.tv_orderStatus.setTextColor(Color.parseColor("#4d8603"));
        }
        holder.tv_orderStatus.setText(category.getOrderStatus());
        if(category.getOrderStatus().equals("Pending")){
            holder.tv_payment_status.setText("Unpaid");
        }else{
            holder.tv_payment_status.setText("Paid");
        }
        holder.tv_orderAmount.setText("\u20B9 "+category.getOrderAmount());
        if(TextUtils.isEmpty(category.getDeliveryName())){
            holder.txtAddress.setText("Address Not Avaliable");
        }else{
            holder.txtAddress.setText(category.getDeliveryName() + ", " + category.getDeliveryHouseNo() + ", " +
                    category.getDeliveryArea() + ", " + category.getDeliveryState() + ", " + category.getDeliveryCity() + ", " + category.getDeliveryPincode());
        }
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_arrow;
        TextView tv_orderId, tv_orderDate, tv_deliveryDate, tv_orderStatus, tv_cancel, tv_reorder, tv_payment_status, tv_orderAmount, txtAddress;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_orderId = (TextView)itemView.findViewById(R.id.txt_order_no);
            //tv_orderDate = (TextView)itemView.findViewById(R.id.tv_orderDate);
            tv_deliveryDate = (TextView)itemView.findViewById(R.id.txt_date_time);
            tv_orderStatus = (TextView)itemView.findViewById(R.id.tv_orderStatus);
            tv_payment_status = (TextView)itemView.findViewById(R.id.tv_payment_status);
            tv_orderAmount = (TextView)itemView.findViewById(R.id.tv_orderAmount);
            //tv_reorder = (TextView)itemView.findViewById(R.id.tv_reorder);
            txtAddress = (TextView)itemView.findViewById(R.id.txtAddress);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lisener.orderClickLisener(mdata.get(getAdapterPosition()));
                }
            });

        }

    }
}
