package com.app.features.order;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.app.R;
import com.app.features.home.model.Category;
import com.app.util.AppUtils;
import com.app.util.RestClient;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyViewHolder> {
    List<OrderDetailList> mdata;
    String reviewId = "";

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
        holder.tv_discount_price.setText("\u20B9 " + category.getProductFinalAmount());
        //holder.tv_star.setText(String.valueOf(category.getRating()));
        if(category.getProductRate().equals("")){
            holder.tv_rating.setVisibility(View.INVISIBLE);
        }else{
            holder.tv_rating.setVisibility(View.VISIBLE);
            holder.tv_rating.setText(category.getProductRate()+" Reviews");
        }

        if (category.getProductType().equals("Quantity")) {
            holder.tvPc.setText(category.getProductQuantity());
        } else {
            holder.tvPc.setText(category.getProductQuantity());
        }
        if (category.getProductDiscount().equals("0")) {
            holder.rl_discount.setVisibility(View.GONE);
            holder.tv_price.setVisibility(View.GONE);
        } else {
            holder.rl_discount.setVisibility(View.VISIBLE);
            holder.tv_price.setVisibility(View.VISIBLE);
            holder.txtDiscountOff.setText(category.getProductDiscount() + "%");
            holder.tv_price.setText("\u20B9 " + category.getProductGrossAmount());
            holder.tv_price.setPaintFlags(holder.tv_price.getPaintFlags()
                    | Paint.STRIKE_THRU_TEXT_FLAG);
        }

    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_best;
        TextView tv_pr_name, tv_pr_sub_name, tv_price, tv_discount_price, tv_rate_product, tvPrdQuantity, tv_peice, tvPc, txtDiscountOff,
                tv_rating, tv_star, tv_return;
        RelativeLayout rl_discount, rl_weight, rl_cancel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_best = (ImageView) itemView.findViewById(R.id.iv_best);
            tv_pr_name = (TextView) itemView.findViewById(R.id.tv_pr_name);
            tv_pr_sub_name = (TextView) itemView.findViewById(R.id.tv_pr_sub_name);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_discount_price = (TextView) itemView.findViewById(R.id.tv_discount_price);
            tv_rate_product = (TextView) itemView.findViewById(R.id.tv_rate_product);
            tvPrdQuantity = (TextView) itemView.findViewById(R.id.tvPrdQuantity);
            tv_peice = (TextView) itemView.findViewById(R.id.tv_peice);
            tvPc = (TextView) itemView.findViewById(R.id.tvPiece);
            txtDiscountOff = (TextView) itemView.findViewById(R.id.txtDiscountOff);
            tv_rating = (TextView) itemView.findViewById(R.id.tv_rating);
            tv_star = (TextView) itemView.findViewById(R.id.tv_star);
            tv_return = (TextView) itemView.findViewById(R.id.tv_return);
            rl_discount = (RelativeLayout) itemView.findViewById(R.id.rl_discount);
            rl_weight = (RelativeLayout) itemView.findViewById(R.id.rl_weight);
            rl_cancel = (RelativeLayout) itemView.findViewById(R.id.rl_cancel);

            rl_weight.setVisibility(View.GONE);
            tvPc.setVisibility(View.VISIBLE);

            tv_rate_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showRatingDialog();
                }
            });

            rl_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(itemView.getContext());
                    builder.setMessage("Are you sure want to remove this product ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeProduct();
                            //mdata.remove(getAdapterPosition());
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
        }



        public void showRatingDialog() {
            final Dialog dialog1 = new Dialog(itemView.getContext());
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.setCancelable(true);
            dialog1.setContentView(R.layout.dlg_rating);
            ImageView iv_cross = (ImageView) dialog1.findViewById(R.id.iv_cross);
            TextView tv_submit = (TextView) dialog1.findViewById(R.id.tv_submit);
            final RatingBar ratingBar = (RatingBar) dialog1.findViewById(R.id.ratingBar);
            final EditText et_review = (EditText) dialog1.findViewById(R.id.et_review);
            final EditText etRateTitle = (EditText) dialog1.findViewById(R.id.etRateTitle);

            tv_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addProductReviews(etRateTitle.getText().toString(), et_review.getText().toString(), ratingBar.getRating(), dialog1);
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


        private void addProductReviews(String rateTitle, String comment, float ratingBar, Dialog dialog1) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("userId", AppUtils.getUserDetails(itemView.getContext()).getLoginId());
            jsonObject.addProperty("productId", mdata.get(getAdapterPosition()).getProductId());
            jsonObject.addProperty("orderId", mdata.get(getAdapterPosition()).getOrderId());
            jsonObject.addProperty("orderDetailId", mdata.get(getAdapterPosition()).getOrderDetailId());
            jsonObject.addProperty("comment", comment);
            jsonObject.addProperty("rating", ratingBar);
            jsonObject.addProperty("ratingComment", rateTitle);
            jsonObject.addProperty("reviewId", reviewId);

            new RestClient().getApiService().addProductReviews(jsonObject, new Callback<ReviewModel>() {
                @Override
                public void success(ReviewModel reviewModel, Response response) {
                    if (reviewModel.getSuccess().equals("1")) {
                        //Toast.makeText(itemView.getContext(), reviewModel.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog1.dismiss();
                        reviewId = reviewModel.getProductReviewId();
                    } else {
                        Toast.makeText(itemView.getContext(), reviewModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(itemView.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void removeProduct(){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("userId", AppUtils.getUserDetails(itemView.getContext()).getLoginId());
            jsonObject.addProperty("productId", mdata.get(getAdapterPosition()).getProductId());
            jsonObject.addProperty("orderId", mdata.get(getAdapterPosition()).getOrderId());
            jsonObject.addProperty("orderDetailId", mdata.get(getAdapterPosition()).getOrderDetailId());

            new RestClient().getApiService().productCancel(jsonObject, new Callback<OrderModel>() {
                @Override
                public void success(OrderModel orderModel, Response response) {
                    if(orderModel.getSuccess().equals("1")){
                        if(orderModel.getIsReturn().equals("Yes")){
                            tv_return.setVisibility(View.VISIBLE);
                        }else{
                            tv_return.setVisibility(View.GONE);
                        }
                        notifyItemChanged(getAdapterPosition());
                        notifyDataSetChanged();
                    }else{
                        Toast.makeText(itemView.getContext(), orderModel.getMessage(), Toast.LENGTH_SHORT).show();
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
