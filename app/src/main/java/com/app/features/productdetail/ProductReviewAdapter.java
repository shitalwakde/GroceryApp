package com.app.features.productdetail;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;
import com.app.constant.AppConstant;
import com.app.controller.AppController;
import com.app.features.home.model.Product;
import com.app.util.AppUtils;
import com.app.util.RestClient;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProductReviewAdapter extends RecyclerView.Adapter<ProductReviewAdapter.MyViewHolder> {
    ArrayList<Product> mdata;

    public ProductReviewAdapter(ArrayList<Product> mdata) {
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_review_adapter, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = mdata.get(position);
        holder.tv_star_line.setText(String.valueOf(product.getRating()));
        holder.rating_title.setText(product.getRatingComment());
        holder.rating_comment.setText(product.getComment());
        holder.tv_reviewCount.setText(product.getLike());
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_star_line, rating_title, rating_comment, tv_reviewCount, tv_reviewLike;
        RelativeLayout rl_thumb;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_star_line = (TextView)itemView.findViewById(R.id.tv_star_line);
            rating_title = (TextView)itemView.findViewById(R.id.rating_title);
            rating_comment = (TextView)itemView.findViewById(R.id.rating_comment);
            tv_reviewCount = (TextView)itemView.findViewById(R.id.tv_reviewCount);
            tv_reviewLike = (TextView)itemView.findViewById(R.id.tv_reviewLike);
            rl_thumb = (RelativeLayout) itemView.findViewById(R.id.rl_thumb);


            rl_thumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mdata.get(getAdapterPosition()).getLikeDisLike().equals("Like")){
                        addLikeDislike("Dislike", mdata.get(getAdapterPosition()).getReviewId());
                    }else {
                        addLikeDislike("Like", mdata.get(getAdapterPosition()).getReviewId());
                    }
                }
            });
        }

        private void addLikeDislike(String dislike, String reviewId){
            JsonObject jsonObject = new JsonObject();
            if(AppConstant.isLogin(itemView.getContext())){
                jsonObject.addProperty("userId", AppUtils.getUserDetails(itemView.getContext()).getLoginId());
            }else{
                jsonObject.addProperty("userId", "");
            }
            jsonObject.addProperty("tempUserId", AppController.getInstance().getUniqueID());
            jsonObject.addProperty("reviewId", reviewId);
            jsonObject.addProperty("likeDisLike", dislike);

            new RestClient().getApiService().addLikeDislike(jsonObject, new Callback<Product>() {
                @Override
                public void success(Product product, Response response) {
                    if(product.getSuccess().equals("1")){
                        product.setLikeDisLike(dislike);
                        product.setLike(mdata.get(getAdapterPosition()).getLikeCount());
                        tv_reviewCount.setText(product.getLikeCount());
                        notifyItemChanged(getAdapterPosition());
                        setReviewLike(product);
                    }else{
                        Toast.makeText(itemView.getContext(), product.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(itemView.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

        private void setReviewLike(Product product){
            if(product.getLikeDisLike().equals("Like")){
                tv_reviewCount.setText(product.getLikeCount());
                tv_reviewLike.setText("Liked");
                tv_reviewLike.setTextColor(Color.parseColor("#2196F3"));
            }else{
                tv_reviewLike.setText("Like");
            }
        }

    }
}
