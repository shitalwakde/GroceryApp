package com.app.features.product;

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
import com.app.features.home.Category;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    List<Category> mdata;
    HomeClickLisener lisener;
    boolean flag = false;

    public ProductAdapter(HomeClickLisener lisener, List<Category> mdata) {
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

        ImageView iv_best, iv_unwish;
        TextView tv_pr_name, tv_pr_sub_name, tv_price, tv_discount_price, tv_add;
        LinearLayout ll_quantity;
        RelativeLayout rl_wish;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_best = (ImageView)itemView.findViewById(R.id.iv_best);
            iv_unwish = (ImageView)itemView.findViewById(R.id.iv_unwish);
            tv_pr_name = (TextView)itemView.findViewById(R.id.tv_pr_name);
            tv_pr_sub_name = (TextView)itemView.findViewById(R.id.tv_pr_sub_name);
            tv_price = (TextView)itemView.findViewById(R.id.tv_price);
            tv_discount_price = (TextView)itemView.findViewById(R.id.tv_discount_price);
            tv_add = (TextView)itemView.findViewById(R.id.tv_add);
            ll_quantity = (LinearLayout)itemView.findViewById(R.id.ll_quantity);
            rl_wish = (RelativeLayout)itemView.findViewById(R.id.rl_wish);


            rl_wish.setVisibility(View.VISIBLE);
            iv_unwish.setImageResource(R.drawable.ic_heart);
            iv_unwish.setOnClickListener(new View.OnClickListener() {
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lisener.productClickLisener(mdata.get(getAdapterPosition()));
                }
            });

            tv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ll_quantity.setVisibility(View.VISIBLE);
                    tv_add.setVisibility(View.GONE);
                }
            });
        }
    }
}
