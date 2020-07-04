package com.app.features.navmenu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.R;
import com.app.callback.HomeClickLisener;
import com.app.features.cart.CartActivity;
import com.app.features.home.model.Category;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    Context mcontext;
    List<Category> mdata;
    HomeClickLisener lisener;

    public OrderAdapter(Context mcontext, HomeClickLisener lisener, List<Category> mdata) {
        this.mcontext = mcontext;
        this.lisener = lisener;
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_adapter, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Category category = mdata.get(position);
        holder.tv_orderId.setText(category.getTv_orderId());
        holder.tv_orderDate.setText(category.getTv_orderDate());
        holder.tv_deliveryDate.setText(category.getTv_deliveryDate());
        holder.tv_orderStatus.setText(category.getTv_orderStatus());
        holder.tv_payment_status.setText(category.getTv_payment_status());
        holder.tv_orderAmount.setText(category.getTv_orderAmount());
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_cancel;
        TextView tv_orderId, tv_orderDate, tv_deliveryDate, tv_orderStatus, tv_cancel, tv_reorder, tv_payment_status, tv_orderAmount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_orderId = (TextView)itemView.findViewById(R.id.tv_orderId);
            tv_orderDate = (TextView)itemView.findViewById(R.id.tv_orderDate);
            tv_deliveryDate = (TextView)itemView.findViewById(R.id.tv_deliveryDate);
            tv_orderStatus = (TextView)itemView.findViewById(R.id.tv_orderStatus);
            tv_payment_status = (TextView)itemView.findViewById(R.id.tv_payment_status);
            tv_orderAmount = (TextView)itemView.findViewById(R.id.tv_orderAmount);
            tv_reorder = (TextView)itemView.findViewById(R.id.tv_reorder);
            tv_cancel = (TextView)itemView.findViewById(R.id.tv_cancel);
            iv_cancel = (ImageView)itemView.findViewById(R.id.iv_cancel);

            iv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iv_cancel.setVisibility(View.GONE);
                    tv_reorder.setVisibility(View.VISIBLE);
                    tv_orderStatus.setText("Cancelled");
                    tv_orderStatus.setTextColor(Color.parseColor("#E72408"));
                }
            });

            tv_reorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, CartActivity.class);
                    mcontext.startActivity(intent);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lisener.orderClickLisener(mdata.get(getAdapterPosition()));
                }
            });
        }
    }
}
