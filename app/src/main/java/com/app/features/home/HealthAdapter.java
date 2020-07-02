package com.app.features.home;

import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.R;
import com.app.callback.HomeClickLisener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HealthAdapter extends RecyclerView.Adapter<HealthAdapter.MyViewHolder> {
    List<Category> mdata;
    HomeClickLisener lisener;
    boolean flag = false;
    public static final int ADD=1;
    public static final int REMOVE=2;
    public static final int RESET=3;

    public HealthAdapter(HomeClickLisener lisener, List<Category> mdata) {
        this.lisener = lisener;
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.health_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Category category = mdata.get(position);
        holder.iv_best.setImageResource(category.getIv_best());
        holder.tv_pr_name.setText(category.getTv_pr_name());
        holder.tv_pr_sub_name.setText(category.getTv_pr_sub_name());
        holder.tv_price.setPaintFlags(holder.tv_price.getPaintFlags()
                | Paint.STRIKE_THRU_TEXT_FLAG);

        if(category.qty <= 0){
            holder.tv_add.setVisibility(View.VISIBLE);
            holder.ll_quantity.setVisibility(View.GONE);
        }else {
            holder.ll_quantity.setVisibility(View.VISIBLE);
            holder.tv_add.setVisibility(View.GONE);
        }
        holder.tv_quantity.setText(String.valueOf(category.qty));

    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_best, iv_unwish;
        TextView tv_pr_name, tv_pr_sub_name, tv_price, tv_discount_price, tv_add, tv_minus, tv_quantity, tv_plus;
        LinearLayout ll_quantity;
        RelativeLayout rl_like;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_best = (ImageView)itemView.findViewById(R.id.iv_best);
            iv_unwish = (ImageView)itemView.findViewById(R.id.iv_unwish);
            tv_pr_name = (TextView)itemView.findViewById(R.id.tv_pr_name);
            tv_pr_sub_name = (TextView)itemView.findViewById(R.id.tv_pr_sub_name);
            tv_price = (TextView)itemView.findViewById(R.id.tv_price);
            tv_discount_price = (TextView)itemView.findViewById(R.id.tv_discount_price);
            tv_add = (TextView)itemView.findViewById(R.id.tv_add);
            tv_minus = (TextView)itemView.findViewById(R.id.tv_minus);
            tv_quantity = (TextView)itemView.findViewById(R.id.tv_quantity);
            tv_plus = (TextView)itemView.findViewById(R.id.tv_plus);
            ll_quantity = (LinearLayout)itemView.findViewById(R.id.ll_quantity);
            rl_like = (RelativeLayout)itemView.findViewById(R.id.rl_like);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lisener.productClickLisener(mdata.get(getAdapterPosition()));
                }
            });

            rl_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(flag==false){
                        flag=true;
                        iv_unwish.setImageResource(R.drawable.ic_heart_red);
                    }else{
                        flag=false;
                        iv_unwish.setImageResource(R.drawable.ic_heart);
                    }
                }
            });

            tv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeQty(getAdapterPosition(),ADD);
                }
            });

            tv_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeQty(getAdapterPosition(),ADD);
                }
            });

            tv_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeQty(getAdapterPosition(),REMOVE);
                }
            });

        }

        private void changeQty(int adapterPosition,int type) {
            int qty=mdata.get(adapterPosition).qty;
            if(type==ADD)
                qty=qty +1;
            else if(type ==REMOVE)
                qty=qty-1;
            else
                qty=0;
            mdata.get(adapterPosition).qty=qty;
            notifyItemChanged(adapterPosition);
        }
    }
}
