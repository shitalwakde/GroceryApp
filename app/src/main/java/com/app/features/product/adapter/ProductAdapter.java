package com.app.features.product.adapter;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
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
import com.app.features.home.adapter.WeighAdapter;
import com.app.features.navmenu.WishListAdapter;
import com.app.util.AppUtils;
import com.app.util.RestClient;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    private static final String TAG = "ProductAdapter";
    
    ArrayList<Product> mdata;
    ProductListener lisener;
    boolean flag = false;
    public static final int ADD=1;
    public static final int REMOVE=2;
    public static final int RESET=3;
    String wishList="", selected="";


    public ProductAdapter(ProductListener lisener, ArrayList<Product> mdata) {
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
        Product category = mdata.get(position);
        Picasso.with(holder.itemView.getContext()).load(category.getImage()).into(holder.iv_best);
        holder.tv_pr_name.setText(category.getName());
        holder.tv_pr_sub_name.setText(category.getBrandName());
        holder.tv_star.setText("4.5");
        holder.tv_rating.setText(category.getRate()+" Rating");
        holder.tv_discount_price.setText("\u20B9 "+category.getFinalAmount());

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
            holder.tv_price.setText("\u20B9 "+category.getGrossAmount());
            holder.txtDiscountOff.setText(category.getDiscount()+"%");
        }

        if(category.getCartQuantityInteger()<=0){
            holder.tv_add.setVisibility(View.VISIBLE);
            holder.ll_quantity.setVisibility(View.GONE);
        }else{
            holder.ll_quantity.setVisibility(View.VISIBLE);
            holder.tv_add.setVisibility(View.GONE);
            Log.w("TAG", "cartQuantity"+category.getCartQuantity());
            holder.tv_quantity.setText(String.valueOf(category.getCartQuantityInteger()));
        }

        if(category.getIsInWishList().equalsIgnoreCase("no")){
            holder.iv_unwish.setImageResource(R.drawable.ic_heart);
        }else{
            holder.iv_unwish.setImageResource(R.drawable.ic_heart_red);
        }


        if(category.getIsVarient().equals("Yes")){
            holder.rl_weight.setVisibility(View.VISIBLE);
            holder.tvPiece.setVisibility(View.GONE);
            holder.tv_peice.setText(category.getQuantity());
        }else{
            holder.rl_weight.setVisibility(View.GONE);
            holder.tvPiece.setVisibility(View.VISIBLE);
            holder.tvPiece.setText(category.getQuantity());
        }

        holder.rl_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(category.getVarientList(), holder, category);
            }
        });

    }

    private void showDialog(ArrayList<Product> items, MyViewHolder holder, Product category) {
        final Dialog dialog1 = new Dialog(holder.itemView.getContext());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(true);
        dialog1.setContentView(R.layout.dlg_varient);
        TextView tv_item_name = (TextView) dialog1.findViewById(R.id.tv_item_name);
        RecyclerView rv_item = (RecyclerView) dialog1.findViewById(R.id.rv_item);

        tv_item_name.setText(category.getName());

        WeighAdapter adapter = new WeighAdapter(items, new WishListAdapter.OnWeightItemCLickListener() {
            @Override
            public void onWeightClicked(Product product) {
                dialog1.dismiss();
                category.setProductVarientId(product.getProductVarientId());
                category.setImage(product.getImage());
                category.setDiscount(product.getDiscount());
                category.setQuantity(product.getQuantity());
                category.setFinalAmount(product.getFinalAmount());
                category.setGrossAmount(product.getGrossAmount());
                category.setMaxProductQuantity(product.getMaxProductQuantity());
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
        rv_item.setAdapter(adapter);

        dialog1.show();
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements OnItemCountChanged {
        ImageView iv_best, iv_unwish, iv_close;
        TextView tv_pr_name, tv_pr_sub_name, tv_price, tv_discount_price, tv_add, tv_minus, tv_quantity, tv_plus, tvPiece, tv_star,tv_rating,
                txtDiscountOff, tv_peice, tv_notify, tv_send_notify;
        LinearLayout ll_quantity;
        RelativeLayout rl_wish, rl_weight, rl_discount, rl_addCartContainer, rl_view, rl_send_notify, rl_outOfStock;
        ProgressBar progressBar;
        EditText et_notify_email;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_best = (ImageView)itemView.findViewById(R.id.iv_best);
            iv_unwish = (ImageView)itemView.findViewById(R.id.iv_unwish);
            iv_close = (ImageView) itemView.findViewById(R.id.iv_close);
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
            rl_wish = (RelativeLayout)itemView.findViewById(R.id.rl_wish);
            rl_weight = (RelativeLayout)itemView.findViewById(R.id.rl_weight);
            rl_discount = (RelativeLayout)itemView.findViewById(R.id.rl_discount);
            rl_addCartContainer = (RelativeLayout)itemView.findViewById(R.id.rl_addCartContainer);
            progressBar = itemView.findViewById(R.id.progressBar);
            tv_notify = (TextView)itemView.findViewById(R.id.tv_notify);
            tv_send_notify = (TextView)itemView.findViewById(R.id.tv_send_notify);
            rl_view = (RelativeLayout)itemView.findViewById(R.id.rl_view);
            rl_send_notify = (RelativeLayout)itemView.findViewById(R.id.rl_send_notify);
            rl_outOfStock = (RelativeLayout)itemView.findViewById(R.id.rl_outOfStock);
            et_notify_email = (EditText)itemView.findViewById(R.id.et_notify_email);


            rl_wish.setVisibility(View.VISIBLE);
            iv_unwish.setImageResource(R.drawable.ic_heart);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lisener.productClickLisener(mdata.get(getAdapterPosition()));
                    lisener.updateCartCount(mdata.get(getAdapterPosition()).getCartCount());
                }
            });


            tv_notify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rl_send_notify.setVisibility(View.VISIBLE);
                    tv_notify.setVisibility(View.GONE);
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

            iv_unwish.setOnClickListener(new View.OnClickListener() {
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
                AppUtils.addTocart(qty,getAdapterPosition(),mdata,ProductAdapter.this,itemView.getContext(),this);
            }else{
                Toast.makeText(itemView.getContext(), "You can not add any more quantities for this product!", Toast.LENGTH_SHORT).show();
                mdata.get(getAdapterPosition()).setLoading(false);
                notifyItemChanged(getAdapterPosition());
            }
        }


        @Override
        public void onSuccess() {
            lisener.updateCartCount(null);
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
            jsonObject.addProperty("productVarientId", mdata.get(position).getProductVarientId());
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
