package com.app.features.cart;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;
import com.app.activities.MainActivity;
import com.app.callback.CalculateLisener;
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


import static com.app.util.AppUtils.setCartCount;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    List<Product> mdata;
    private RelativeLayout rl_noDataFound;
    ProductListener productListener;
    public static final int ADD=1;
    public static final int REMOVE=2;
    public static final int RESET=3;
    boolean flag = false;
    private String wishList="", selected="";
    CalculateLisener calculateLisener;


    public CartAdapter(ProductListener productListener, List<Product> mdata, RelativeLayout rl_noDataFound, CalculateLisener calculateLisener) {
        this.productListener = productListener;
        this.mdata = mdata;
        this.rl_noDataFound = rl_noDataFound;
        this.calculateLisener = calculateLisener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product category = mdata.get(position);
        Picasso.with(holder.itemView.getContext()).load(category.getProductImage()).into(holder.iv_best);
        holder.tv_pr_name.setText(category.getCartProduct());
        holder.tv_pr_sub_name.setText(category.getBrandName());
        holder.tv_discount_price.setText("\u20B9 "+category.getProductCartAmount());

        if(category.getMaxProductQuantity().equals("0")){
            holder.rl_outOfStock.setVisibility(View.VISIBLE);
            holder.tv_notify.setVisibility(View.VISIBLE);
            //holder.rl_view.setVisibility(View.GONE);
        }else {
            holder.tv_notify.setVisibility(View.GONE);
            holder.rl_outOfStock.setVisibility(View.GONE);
            holder.rl_view.setVisibility(View.VISIBLE);
        }

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
            holder.tv_price.setText("\u20B9 "+category.getProductFinalAmount());
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

        holder.rl_weight.setVisibility(View.GONE);
        holder.tvPiece.setVisibility(View.VISIBLE);
        if(category.getProductVarientId().equals("0")){
            holder.tvPiece.setText(category.getProductQuantity());
        }else{
            holder.tvPiece.setText(category.getProductQuantity());
        }

        /*if(category.getRating() == 0){
            holder.rl_rating.setVisibility(View.INVISIBLE);
        }else{
            holder.rl_rating.setVisibility(View.VISIBLE);
            holder.tv_star.setText(String.valueOf(category.getRating()));
        }*/
        holder.tv_star.setText(String.valueOf(category.getRating()));
        if(category.getRate() != null){
            if(category.getRate().equals("")){
                holder.tv_rating.setVisibility(View.INVISIBLE);
            }else{
                holder.tv_rating.setVisibility(View.VISIBLE);
                holder.tv_rating.setText(category.getRate() + " Ratings");
            }
        }else {
            holder.tv_rating.setVisibility(View.INVISIBLE);
        }

    }


    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements OnItemCountChanged {
        ImageView iv_best, iv_unwish, iv_close;
        TextView tv_pr_name, tv_pr_sub_name, tv_price, tv_discount_price, tv_add, tv_remove, tv_minus, tv_quantity, tv_plus,
                txtDiscountOff, tvPiece, tv_peice, tv_notify, tv_send_notify, tv_star, tv_rating;
        LinearLayout ll_quantity;
        RelativeLayout rl_wish, rl_discount, rl_addCartContainer, rl_weight, rl_view, rl_send_notify, rl_outOfStock, rl_rating;
        ProgressBar progressBar;
        EditText et_notify_email;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_remove = (TextView)itemView.findViewById(R.id.tv_remove);
            iv_best = (ImageView)itemView.findViewById(R.id.iv_best);
            iv_close = (ImageView)itemView.findViewById(R.id.iv_close);
            tvPiece = (TextView)itemView.findViewById(R.id.tvPiece);
            tv_peice = (TextView)itemView.findViewById(R.id.tv_peice);
            tv_pr_name = (TextView)itemView.findViewById(R.id.tv_pr_name);
            tv_pr_sub_name = (TextView)itemView.findViewById(R.id.tv_pr_sub_name);
            tv_price = (TextView)itemView.findViewById(R.id.tv_price);
            tv_discount_price = (TextView)itemView.findViewById(R.id.tv_discount_price);
            tv_add = (TextView)itemView.findViewById(R.id.tv_add);
            tv_minus = (TextView)itemView.findViewById(R.id.tv_minus);
            tv_quantity = (TextView)itemView.findViewById(R.id.tv_quantity);
            tv_plus = (TextView)itemView.findViewById(R.id.tv_plus);
            txtDiscountOff = (TextView)itemView.findViewById(R.id.txtDiscountOff);
            tv_star = (TextView)itemView.findViewById(R.id.tv_star);
            tv_rating = (TextView)itemView.findViewById(R.id.tv_rating);
            ll_quantity = (LinearLayout)itemView.findViewById(R.id.ll_quantity);
            iv_unwish = (ImageView)itemView.findViewById(R.id.iv_unwish);
            rl_wish = (RelativeLayout)itemView.findViewById(R.id.rl_wish);
            rl_discount = (RelativeLayout)itemView.findViewById(R.id.rl_discount);
            rl_weight = (RelativeLayout)itemView.findViewById(R.id.rl_weight);
            rl_addCartContainer = (RelativeLayout)itemView.findViewById(R.id.rl_addCartContainer);
            progressBar = itemView.findViewById(R.id.progressBar);
            tv_notify = (TextView)itemView.findViewById(R.id.tv_notify);
            tv_send_notify = (TextView)itemView.findViewById(R.id.tv_send_notify);
            rl_view = (RelativeLayout)itemView.findViewById(R.id.rl_view);
            rl_send_notify = (RelativeLayout)itemView.findViewById(R.id.rl_send_notify);
            rl_outOfStock = (RelativeLayout)itemView.findViewById(R.id.rl_outOfStock);
            rl_rating = (RelativeLayout)itemView.findViewById(R.id.rl_rating);
            et_notify_email = (EditText)itemView.findViewById(R.id.et_notify_email);

            tv_remove.setVisibility(View.VISIBLE);
            ll_quantity.setVisibility(View.VISIBLE);
            tv_add.setVisibility(View.GONE);

            tv_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(itemView.getContext());
                    builder.setMessage("Are you sure want to remove the product ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            addTocartRemove(0,getAdapterPosition(),mdata,CartAdapter.this,itemView.getContext());
                            dialog.cancel();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            });

            rl_wish.setVisibility(View.GONE);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productListener.productClickLisener(mdata.get(getAdapterPosition()));
                }
            });

            tv_notify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(AppConstant.isLogin(itemView.getContext())){
                        notifyMe();
                    }else{
                        AlertDialog.Builder builder=new AlertDialog.Builder(itemView.getContext());
                        builder.setMessage("Please login to continue");
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
                    //rl_send_notify.setVisibility(View.VISIBLE);
                    //tv_notify.setVisibility(View.GONE);
                }
            });

            tv_send_notify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(TextUtils.isEmpty(et_notify_email.getText().toString())){
                        et_notify_email.setError("Please Enter Email");
                        et_notify_email.requestFocus();
                    }else{
                        rl_send_notify.setVisibility(View.GONE);
                        tv_notify.setVisibility(View.VISIBLE);
                    }
                }
            });

            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rl_send_notify.setVisibility(View.GONE);
                    tv_notify.setVisibility(View.VISIBLE);
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
            int maxQuantity = Integer.parseInt(mdata.get(getAdapterPosition()).getMaxProductQuantity());
            if(qty <= maxQuantity){
                addTocart(qty,getAdapterPosition(),mdata,CartAdapter.this,itemView.getContext(),this);
            }else{
                Toast.makeText(itemView.getContext(), "You can not add any more quantities for this product!", Toast.LENGTH_SHORT).show();
                mdata.get(getAdapterPosition()).setLoading(false);
                notifyItemChanged(getAdapterPosition());
            }
        }


        public void addTocart(int qty, final int position, List<Product> mdata, RecyclerView.Adapter adapter, Context context, final OnItemCountChanged listener){
            JsonObject jsonObject = new JsonObject();
            if(AppConstant.isLogin(null)){
                jsonObject.addProperty("userId", AppUtils.getUserDetails(null).getLoginId());
            }else{
                jsonObject.addProperty("userId", "");
            }
            jsonObject.addProperty("tempUserId", AppController.getInstance().getUniqueID());
            jsonObject.addProperty("productId", mdata.get(position).getProductId());
            jsonObject.addProperty("productVarientId", mdata.get(position).getProductVarientId());
            jsonObject.addProperty("quantity", String.valueOf(qty));

            new RestClient().getApiService().addToCart(jsonObject, new Callback<Product>() {
                @Override
                public void success(Product product, Response response) {
                    if(product.getSuccess().equals("1")){
                        mdata.get(position).setLoading(false);
                        if(product.getQuantity().equals("0")){
                            mdata.remove(position);
                            if(mdata.size()==0){
                                rl_noDataFound.setVisibility(View.VISIBLE);
                            }else{
                                rl_noDataFound.setVisibility(View.GONE);
                            }
                        }
                        if(product.getCartCount()!=null){
                            setCartCount(product.getCartCount());
                        }
                        listener.onSuccess();
                    if(!product.getQuantity().equals("0")){
                        mdata.get(position).setCartQuantity(qty);
                        mdata.get(position).setProductCartAmount(product.getProductCartAmount());
                        mdata.get(position).setProductFinalAmount(product.getProductFinalAmount());
                    }
                        adapter.notifyDataSetChanged();
                        adapter.notifyItemChanged(position);
                        calculateLisener.calculatePlus(mdata);
//                        Toast.makeText(itemView.getContext(), product.getMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, product.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    mdata.get(position).setLoading(false);
                    notifyItemChanged(position);
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


        private void addTocartRemove(int qty, final int position, List<Product> mdata, RecyclerView.Adapter adapter, Context context){
            JsonObject jsonObject = new JsonObject();
            if(AppConstant.isLogin(null)){
                jsonObject.addProperty("userId", AppUtils.getUserDetails(null).getLoginId());
            }else{
                jsonObject.addProperty("userId", "");
            }
            jsonObject.addProperty("tempUserId", AppController.getInstance().getUniqueID());
            jsonObject.addProperty("productId", mdata.get(position).getProductId());
            jsonObject.addProperty("productVarientId", mdata.get(position).getProductVarientId());
            jsonObject.addProperty("quantity", String.valueOf(qty));

            new RestClient().getApiService().addToCart(jsonObject, new Callback<Product>() {
                @Override
                public void success(Product product, Response response) {
                    if(product.getSuccess().equals("1")){
                        if(product.getQuantity().equals("0")){
                            mdata.remove(position);
                            if(mdata.size()==0){
                                rl_noDataFound.setVisibility(View.VISIBLE);
                            }else{
                                rl_noDataFound.setVisibility(View.GONE);
                            }
                        }
                        if(product.getCartCount()!=null){
                            setCartCount(product.getCartCount());
                        }
                        adapter.notifyDataSetChanged();
                        adapter.notifyItemChanged(position);
                        calculateLisener.calculatePlus(mdata);
//                        Toast.makeText(itemView.getContext(), product.getMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, product.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onSuccess() {
            productListener.updateCartCount(null);
        }

        private void notifyMe(){

        }
    }



}
