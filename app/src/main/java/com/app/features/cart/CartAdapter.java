package com.app.features.cart;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;
import com.app.activities.MainActivity;
import com.app.callback.OnItemCountChanged;
import com.app.callback.ProductListener;
import com.app.constant.AppConstant;
import com.app.controller.AppController;
import com.app.features.home.model.Product;
import com.app.features.login.LoginActivity;
import com.app.util.AppUtils;
import com.app.util.PrefUtil;
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


import static com.app.util.AppUtils.setCartCount;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    List<Product> mdata;
    private RelativeLayout rl_noDataFound;
    ProductListener productListener;
    public static final int ADD=1;
    public static final int REMOVE=2;
    public static final int RESET=3;
    boolean flag = false;
    private String wishList="";

    public CartAdapter(ProductListener productListener, List<Product> mdata, RelativeLayout rl_noDataFound) {
        this.productListener = productListener;
        this.mdata = mdata;
        this.rl_noDataFound = rl_noDataFound;
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
        holder.tv_discount_price.setText("\u20B9 "+category.getProductFinalAmount());
        holder.tv_price.setText("\u20B9 "+category.getProductCartAmount());
        holder.txtDiscountOff.setText(category.getDiscount()+"%");
        holder.tv_price.setPaintFlags(holder.tv_price.getPaintFlags()
                | Paint.STRIKE_THRU_TEXT_FLAG);

        if(category.getCartQuantityInteger()<=0){
            holder.tv_add.setVisibility(View.VISIBLE);
            holder.ll_quantity.setVisibility(View.GONE);
        }else{
            holder.ll_quantity.setVisibility(View.VISIBLE);
            holder.tv_add.setVisibility(View.GONE);
            holder.tv_quantity.setText(String.valueOf(category.getCartQuantityInteger()));
        }
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements OnItemCountChanged {
        ImageView iv_best, iv_unwish;
        TextView tv_pr_name, tv_pr_sub_name, tv_price, tv_discount_price, tv_add, tv_remove, tv_minus, tv_quantity, tv_plus, txtDiscountOff;
        LinearLayout ll_quantity;
        RelativeLayout rl_wish;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_remove = (TextView)itemView.findViewById(R.id.tv_remove);
            iv_best = (ImageView)itemView.findViewById(R.id.iv_best);
            tv_pr_name = (TextView)itemView.findViewById(R.id.tv_pr_name);
            tv_pr_sub_name = (TextView)itemView.findViewById(R.id.tv_pr_sub_name);
            tv_price = (TextView)itemView.findViewById(R.id.tv_price);
            tv_discount_price = (TextView)itemView.findViewById(R.id.tv_discount_price);
            tv_add = (TextView)itemView.findViewById(R.id.tv_add);
            tv_minus = (TextView)itemView.findViewById(R.id.tv_minus);
            tv_quantity = (TextView)itemView.findViewById(R.id.tv_quantity);
            tv_plus = (TextView)itemView.findViewById(R.id.tv_plus);
            txtDiscountOff = (TextView)itemView.findViewById(R.id.txtDiscountOff);
            ll_quantity = (LinearLayout)itemView.findViewById(R.id.ll_quantity);
            iv_unwish = (ImageView)itemView.findViewById(R.id.iv_unwish);
            rl_wish = (RelativeLayout)itemView.findViewById(R.id.rl_wish);

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
                            addTocart(0,getAdapterPosition(),mdata,CartAdapter.this,itemView.getContext());
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
            /*iv_unwish.setImageResource(R.drawable.ic_heart);

            iv_unwish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(AppConstant.isLogin(itemView.getContext())){
                        if(flag==false){
                            flag=true;
                            iv_unwish.setImageResource(R.drawable.ic_heart_red);
                            wishList = "yes";
                            addWishList(wishList);
                        }else{
                            flag=false;
                            iv_unwish.setImageResource(R.drawable.ic_heart);
                            wishList = "no";
                            addWishList(wishList);
                        }
                    }else{
                        AlertDialog.Builder builder=new AlertDialog.Builder(itemView.getContext());
                        builder.setMessage("Please login to add product in wishlist");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(itemView.getContext(), LoginActivity.class);
                                intent.putExtra("type","login");
                                itemView.getContext().startActivity(intent);
                                dialog.cancel();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    }
                }
            });*/


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productListener.productClickLisener(mdata.get(getAdapterPosition()));
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
            int qty=mdata.get(adapterPosition).getCartQuantityInteger();
            if(type==ADD) {
                qty = qty + 1;
            }else if(type ==REMOVE) {
                qty = qty - 1;
            }else {
                qty = 0;
            }
            AppUtils.addTocart(qty,getAdapterPosition(),mdata,CartAdapter.this,itemView.getContext(),this);
        }

        private void addTocart(int qty, final int position, List<Product> mdata, RecyclerView.Adapter adapter, Context context){
            JsonObject jsonObject = new JsonObject();
            if(AppConstant.isLogin(null)){
                jsonObject.addProperty("userId", AppUtils.getUserDetails(null).getLoginId());
            }else{
                jsonObject.addProperty("userId", "");
            }
            jsonObject.addProperty("tempUserId", AppController.getInstance().getUniqueID());
            //jsonObject.addProperty("tempUserId", "5478965545");
            jsonObject.addProperty("productId", mdata.get(position).getProductId());
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
    }


}
