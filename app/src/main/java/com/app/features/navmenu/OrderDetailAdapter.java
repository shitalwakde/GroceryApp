package com.app.features.navmenu;

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
import android.widget.TextView;

import com.app.R;
import com.app.features.home.Category;
import com.app.features.productdetail.ProductDetailActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyViewHolder> {
    Context mcontext;
    List<Category> mdata;

    public OrderDetailAdapter(Context mcontext, List<Category> mdata) {
        this.mcontext = mcontext;
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
        TextView tv_pr_name, tv_pr_sub_name, tv_price, tv_discount_price, tv_rate_product;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_best = (ImageView)itemView.findViewById(R.id.iv_best);
            tv_pr_name = (TextView)itemView.findViewById(R.id.tv_pr_name);
            tv_pr_sub_name = (TextView)itemView.findViewById(R.id.tv_pr_sub_name);
            tv_price = (TextView)itemView.findViewById(R.id.tv_price);
            tv_discount_price = (TextView)itemView.findViewById(R.id.tv_discount_price);
            tv_rate_product = (TextView)itemView.findViewById(R.id.tv_rate_product);

            tv_rate_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showRatingDialog();
                }
            });
        }
    }

    public void showRatingDialog(){
        final Dialog dialog1 = new Dialog(mcontext);
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
