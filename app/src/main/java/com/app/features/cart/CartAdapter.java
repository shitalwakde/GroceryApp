package com.app.features.cart;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.R;
import com.app.callback.HomeClickLisener;
import com.app.features.home.Category;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    List<Category> mdata;
    HomeClickLisener lisener;

    public CartAdapter(HomeClickLisener lisener, List<Category> mdata) {
        this.lisener = lisener;
        this.mdata = mdata;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_adapter, parent, false);
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
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_best;
        TextView tv_pr_name, tv_pr_sub_name, tv_price, tv_discount_price, tv_add, tv_remove;
        LinearLayout ll_quantity;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_remove = (TextView)itemView.findViewById(R.id.tv_remove);
            iv_best = (ImageView)itemView.findViewById(R.id.iv_best);
            tv_pr_name = (TextView)itemView.findViewById(R.id.tv_pr_name);
            tv_pr_sub_name = (TextView)itemView.findViewById(R.id.tv_pr_sub_name);
            tv_price = (TextView)itemView.findViewById(R.id.tv_price);
            tv_discount_price = (TextView)itemView.findViewById(R.id.tv_discount_price);
            tv_add = (TextView)itemView.findViewById(R.id.tv_add);
            ll_quantity = (LinearLayout)itemView.findViewById(R.id.ll_quantity);

            tv_remove.setVisibility(View.VISIBLE);
            ll_quantity.setVisibility(View.VISIBLE);
            tv_add.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lisener.productClickLisener(mdata.get(getAdapterPosition()));
                }
            });
        }
    }
}
