package com.app.features.home.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;
import com.app.callback.OnItemCountChanged;
import com.app.callback.ProductListener;
import com.app.constant.AppConstant;
import com.app.controller.AppController;
import com.app.features.home.model.Product;
import com.app.features.login.LoginActivity;
import com.app.util.AppUtils;
import com.app.util.RestClient;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BestSellingAdapter extends RecyclerView.Adapter<BestSellingAdapter.MyViewHolder> {
    List<Product> mdata;
    ProductListener lisener;
    public static final int ADD=1;
    public static final int REMOVE=2;
    public static final int RESET=3;
    boolean flag = false;
    String wishList="";

    public BestSellingAdapter(ProductListener lisener, final ArrayList<Product> mdata) {
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
        Picasso.with(holder.itemView.getContext()).load(category.getImage()).into(holder.iv_best);
        holder.tv_pr_name.setText(category.getName());
        holder.tv_pr_sub_name.setText(category.getBrandName());
        holder.tv_star.setText("4.5");
        holder.tv_rating.setText(category.getRate()+" Rating");
        holder.tv_discount_price.setText("\u20B9 "+category.getGrossAmount());

        if(category.isLoading()){
            holder.rl_addCartContainer.setVisibility(View.INVISIBLE);
            holder.progressBar.setVisibility(View.VISIBLE);
        }else {
            holder.rl_addCartContainer.setVisibility(View.VISIBLE);
            holder.progressBar.setVisibility(View.GONE);
        }

        if(category.getDiscount().equals("0")){
            holder.tv_price.setVisibility(View.GONE);
            holder.rl_discount.setVisibility(View.GONE);
        }else{
            holder.tv_price.setVisibility(View.VISIBLE);
            holder.rl_discount.setVisibility(View.VISIBLE);
            holder.tv_price.setPaintFlags(holder.tv_price.getPaintFlags()
                    | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tv_price.setText("\u20B9 "+category.getFinalAmount());
            holder.txtDiscountOff.setText(category.getDiscount()+"%");
        }

        if(category.getCartQuantityInteger()<=0){
            holder.tv_add.setVisibility(View.VISIBLE);
            holder.ll_quantity.setVisibility(View.GONE);
        }else{
            holder.ll_quantity.setVisibility(View.VISIBLE);
            holder.tv_add.setVisibility(View.GONE);
            holder.tv_quantity.setText(String.valueOf(category.getCartQuantityInteger()));
        }

        if(category.getIsInWishList().equalsIgnoreCase("no")){
            holder.iv_unwish.setImageResource(R.drawable.ic_heart);
        }else{
            holder.iv_unwish.setImageResource(R.drawable.ic_heart_red);
        }

        if(category.getProductType().equals("Quantity")){
            holder.rl_weight.setVisibility(View.GONE);
            holder.tvPiece.setVisibility(View.VISIBLE);
            holder.tvPiece.setText(category.getQuantity()+" Pc");
        }else{
            holder.rl_weight.setVisibility(View.VISIBLE);
            holder.tvPiece.setVisibility(View.GONE);
            holder.tv_peice.setText(category.getQuantity()+" Kg");
        }

    }

    @Override
    public int getItemCount() {
        return mdata.size()>=4? 4:(mdata.size()/2)*2;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_best, iv_unwish;
        TextView tv_pr_name, tv_pr_sub_name, tv_price, tv_discount_price, tv_add, tv_minus, tv_quantity, tv_plus, tvPiece, tv_star,tv_rating, txtDiscountOff, tv_peice;
        LinearLayout ll_quantity;
        RelativeLayout rl_like, rl_weight,rl_discount, rl_addCartContainer;
        ProgressBar progressBar;

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
            tv_peice = (TextView)itemView.findViewById(R.id.tv_peice);
            txtDiscountOff = (TextView)itemView.findViewById(R.id.txtDiscountOff);
            ll_quantity = (LinearLayout)itemView.findViewById(R.id.ll_quantity);
            rl_like = (RelativeLayout)itemView.findViewById(R.id.rl_like);
            rl_weight = (RelativeLayout)itemView.findViewById(R.id.rl_weight);
            rl_discount = (RelativeLayout)itemView.findViewById(R.id.rl_discount);
            rl_addCartContainer = (RelativeLayout)itemView.findViewById(R.id.rl_addCartContainer);
            progressBar = itemView.findViewById(R.id.progressbar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lisener.productClickLisener(mdata.get(getAdapterPosition()));
                }
            });


            rl_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(AppConstant.isLogin(itemView.getContext())){
                        if(mdata.get(getAdapterPosition()).getIsInWishList()!=null && mdata.get(getAdapterPosition()).getIsInWishList().equalsIgnoreCase("yes")){
                            addWishList("no", mdata.get(getAdapterPosition()), getAdapterPosition());
                        }else{
                            addWishList("yes", mdata.get(getAdapterPosition()), getAdapterPosition());
                        }
                    }else {
                        AlertDialog.Builder builder=new AlertDialog.Builder(itemView.getContext());
                        builder.setMessage("Please login to add product in wishlist");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(itemView.getContext(), LoginActivity.class);
                                intent.putExtra("type","login");
                                itemView.getContext().startActivity(intent);
                                dialog.dismiss();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
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
            mdata.get(getAdapterPosition()).setLoading(true);
            notifyItemChanged(getAdapterPosition());
            int qty=mdata.get(adapterPosition).getCartQuantityInteger();
            if(type==ADD) {
                qty = qty + 1;
            }else if(type ==REMOVE) {
                qty = qty - 1;
            }else {
                qty = 0;
            }
            AppUtils.addTocart(qty,getAdapterPosition(),mdata,BestSellingAdapter.this,itemView.getContext(),new OnItemCountChanged(){
                @Override
                public void onSuccess(){
                    lisener.updateCartCount(null);
                }
            });
        }


        private void addWishList(String wishList, final Product mDataProduct, final int position){
            JsonObject jsonObject = new JsonObject();
            if(AppConstant.isLogin(itemView.getContext())){
                jsonObject.addProperty("userId", AppUtils.getUserDetails(itemView.getContext()).getLoginId());
            }else{
                jsonObject.addProperty("userId", "");
            }
            jsonObject.addProperty("tempUserId", AppController.getInstance().getUniqueID());
            jsonObject.addProperty("productId",mdata.get(getAdapterPosition()).getProductId());
            jsonObject.addProperty("wishList",wishList);

            new RestClient().getApiService().addWishList(jsonObject, new Callback<Product>() {
                @Override
                public void success(Product product, Response response) {
                    if(product.getSuccess().equals("1")){
                        mDataProduct.setIsInWishList(wishList);
                        if(mDataProduct.getIsInWishList().equalsIgnoreCase("no")){
                            iv_unwish.setImageResource(R.drawable.ic_heart);
                        }else{
                            iv_unwish.setImageResource(R.drawable.ic_heart_red);
                        }
                        notifyItemChanged(position);
                        //Toast.makeText(itemView.getContext(), product.getMessage(), Toast.LENGTH_SHORT).show();
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
    }
}
