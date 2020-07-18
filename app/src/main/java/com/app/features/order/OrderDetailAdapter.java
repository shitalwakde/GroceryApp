package com.app.features.order;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.R;
import com.app.features.home.model.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyViewHolder> {
    List<OrderDetailList> mdata;

    public OrderDetailAdapter(List<OrderDetailList> mdata) {
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrderDetailList category = mdata.get(position);
        Picasso.with(holder.itemView.getContext()).load(category.getProductImage()).into(holder.iv_best);
        holder.tv_pr_name.setText(category.getProductName());
        holder.tv_pr_sub_name.setText(category.getProductName());
        holder.tv_discount_price.setText("\u20B9 "+category.getProductGrossAmount());

        if(category.getProductType().equals("Quantity")){
            holder.tvPc.setText(category.getProductQuantity()+"Pc");
        }else{
            holder.tvPc.setText(category.getProductQuantity());
        }
        if(category.getProductDiscount().equals("0")){
            holder.rl_discount.setVisibility(View.GONE);
            holder.tv_price.setVisibility(View.GONE);
        }else{
            holder.rl_discount.setVisibility(View.VISIBLE);
            holder.tv_price.setVisibility(View.VISIBLE);
            holder.txtDiscountOff.setText(category.getProductDiscount()+"%");
            holder.tv_price.setText("\u20B9 "+category.getProductFinalAmount());
            holder.tv_price.setPaintFlags(holder.tv_price.getPaintFlags()
                    | Paint.STRIKE_THRU_TEXT_FLAG);
        }

    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_best;
        TextView tv_pr_name, tv_pr_sub_name, tv_price, tv_discount_price, tv_rate_product, tvPrdQuantity, tv_peice, tvPc, txtDiscountOff;
        RelativeLayout rl_discount, rl_weight;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_best = (ImageView)itemView.findViewById(R.id.iv_best);
            tv_pr_name = (TextView)itemView.findViewById(R.id.tv_pr_name);
            tv_pr_sub_name = (TextView)itemView.findViewById(R.id.tv_pr_sub_name);
            tv_price = (TextView)itemView.findViewById(R.id.tv_price);
            tv_discount_price = (TextView)itemView.findViewById(R.id.tv_discount_price);
            tv_rate_product = (TextView)itemView.findViewById(R.id.tv_rate_product);
            tvPrdQuantity = (TextView)itemView.findViewById(R.id.tvPrdQuantity);
            tv_peice = (TextView)itemView.findViewById(R.id.tv_peice);
            tvPc = (TextView)itemView.findViewById(R.id.tvPiece);
            txtDiscountOff = (TextView)itemView.findViewById(R.id.txtDiscountOff);
            rl_discount = (RelativeLayout)itemView.findViewById(R.id.rl_discount);
            rl_weight = (RelativeLayout)itemView.findViewById(R.id.rl_weight);

            rl_weight.setVisibility(View.GONE);
            tvPc.setVisibility(View.VISIBLE);

            tv_rate_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showRatingDialog();
                }
            });

        }

        public void showRatingDialog(){
            final Dialog dialog1 = new Dialog(itemView.getContext());
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.setCancelable(true);
            dialog1.setContentView(R.layout.dlg_rating);
            ImageView iv_cross = (ImageView)dialog1.findViewById(R.id.iv_cross);
            TextView tv_submit = (TextView)dialog1.findViewById(R.id.tv_submit);
            final RatingBar ratingBar = (RatingBar)dialog1.findViewById(R.id.ratingBar);
            final EditText et_review = (EditText)dialog1.findViewById(R.id.et_review);

            tv_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog1.dismiss();
                    //saveRatings(String.valueOf(ratingBar.getRating()), et_review.getText().toString(), dialog1);
                }
            });

            iv_cross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog1.dismiss();
                }
            });
            dialog1.show();
        }
    }

}
