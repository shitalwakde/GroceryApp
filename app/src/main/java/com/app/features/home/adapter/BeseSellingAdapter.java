package com.app.features.home.adapter;

import android.content.Context;
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
import com.app.callback.ProductListener;
import com.app.features.home.model.Category;
import com.app.features.home.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BeseSellingAdapter extends RecyclerView.Adapter<BeseSellingAdapter.MyViewHolder> {
    Context context;
    List<Product> mdata;
    ProductListener lisener;
    public static final int ADD=1;
    public static final int REMOVE=2;
    public static final int RESET=3;
    boolean flag = false;

    public BeseSellingAdapter(ProductListener lisener,final ArrayList<Product> mdata) {
        this.lisener = lisener;
        this.mdata = mdata;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.best_selling_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product category = mdata.get(position);
        Picasso.with(context).load(category.getImage());
        holder.tv_pr_name.setText(category.getName());
        holder.tv_pr_sub_name.setText(category.getBrandName());
        holder.tv_star.setText(category.getRate());
        holder.tv_rating.setText(category.getRate()+" Rating");
        holder.txtDiscountOff.setText(category.getDiscount()+" %");
        holder.tv_price.setText("\u20B9 "+category.getGrossAmount());
        holder.tv_discount_price.setText("\u20B9 "+category.getFinalAmount());
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

        if(category.getProductType().equals("Quantity")){
            holder.rl_weight.setVisibility(View.GONE);
            holder.tvPiece.setVisibility(View.VISIBLE);
            holder.tvPiece.setText(category.getQuantity()+" Pc");
        }

    }

    @Override
    public int getItemCount() {
        return mdata.size()>=4? 4:(mdata.size()/2)*2;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_best, iv_unwish;
        TextView tv_pr_name, tv_pr_sub_name, tv_price, tv_discount_price, tv_add, tv_minus, tv_quantity, tv_plus, tvPiece, tv_star,tv_rating, txtDiscountOff;
        LinearLayout ll_quantity;
        RelativeLayout rl_like, rl_weight;

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
            tvPiece = (TextView)itemView.findViewById(R.id.tvPiece);
            tv_star = (TextView)itemView.findViewById(R.id.tv_star);
            tv_rating = (TextView)itemView.findViewById(R.id.tv_rating);
            txtDiscountOff = (TextView)itemView.findViewById(R.id.txtDiscountOff);
            ll_quantity = (LinearLayout)itemView.findViewById(R.id.ll_quantity);
            rl_like = (RelativeLayout)itemView.findViewById(R.id.rl_like);
            rl_weight = (RelativeLayout)itemView.findViewById(R.id.rl_weight);


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
            Product category=mdata.get(adapterPosition);
            int qty=category.qty;
            if(type==ADD)
                qty=qty +1;
            else if(type ==REMOVE)
                qty=qty-1;
            else
                qty=0;
            category.qty=qty;
            notifyItemChanged(adapterPosition);
        }
    }
}
