package com.app.features.navmenu;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
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
import com.app.features.home.adapter.WeighAdapter;
import com.app.features.home.model.Product;
import com.app.features.login.LoginActivity;
import com.app.util.AppUtils;
import com.app.util.RestClient;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.MyViewHolder> {
    List<Product> mdata;
    ProductListener productListener;

    boolean flag = false;
    public static final int ADD=1;
    public static final int REMOVE=2;
    public static final int RESET=3;
    private WeakReference<WishListFragment> weakWishlistFragment;
    String selected="";

    public WishListAdapter(ProductListener productListener, List<Product> mdata, WishListFragment wishListFragment) {
        this.productListener = productListener;
        this.mdata = mdata;
        weakWishlistFragment=new WeakReference<WishListFragment>(wishListFragment);
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
        holder.tv_pr_name.setText(category.getProductName());
        holder.tv_pr_sub_name.setText(category.getBrandName());
        holder.txtDiscountOff.setText(category.getDiscount()+"%");
        holder.tv_discount_price.setText("\u20B9 "+(category.getFinalAmount()));

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
            holder.tv_quantity.setText(String.valueOf(category.getCartQuantityInteger()));
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

        /*if(category.getRating() == 0){
            holder.rl_rating.setVisibility(View.INVISIBLE);
        }else{
            holder.rl_rating.setVisibility(View.VISIBLE);
            holder.tv_star.setText(String.valueOf(category.getRating()));
        }*/
        holder.tv_star.setText(String.valueOf(category.getRating()));
        if(category.getRate().equals("")){
            holder.tv_rating.setVisibility(View.INVISIBLE);
        }else{
            holder.tv_rating.setVisibility(View.VISIBLE);
            holder.tv_rating.setText(category.getRate() + " Ratings");
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

        tv_item_name.setText(category.getProductName());

        WeighAdapter adapter = new WeighAdapter(items, new OnWeightItemCLickListener() {
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
        TextView tv_pr_name, tv_pr_sub_name, tv_price, tv_discount_price, tv_add, tv_remove, tv_minus, tv_quantity, tv_plus,
                txtDiscountOff, tvPiece, tv_peice, tv_notify, tv_send_notify, tv_star, tv_rating;
        LinearLayout ll_quantity;
        RelativeLayout rl_wish, rl_discount, rl_addCartContainer, rl_weight, rl_view, rl_send_notify, rl_outOfStock, rl_rating;
        ProgressBar progressBar;
        EditText et_notify_email;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_best = (ImageView)itemView.findViewById(R.id.iv_best);
            iv_unwish = (ImageView)itemView.findViewById(R.id.iv_unwish);
            iv_close = (ImageView)itemView.findViewById(R.id.iv_close);
            tvPiece = (TextView)itemView.findViewById(R.id.tvPiece);
            tv_peice = (TextView)itemView.findViewById(R.id.tv_peice);
            tv_pr_name = (TextView)itemView.findViewById(R.id.tv_pr_name);
            tv_pr_sub_name = (TextView)itemView.findViewById(R.id.tv_pr_sub_name);
            tv_price = (TextView)itemView.findViewById(R.id.tv_price);
            tv_discount_price = (TextView)itemView.findViewById(R.id.tv_discount_price);
            tv_add = (TextView)itemView.findViewById(R.id.tv_add);
            tv_remove = (TextView)itemView.findViewById(R.id.tv_remove);
            tv_minus = (TextView)itemView.findViewById(R.id.tv_minus);
            tv_quantity = (TextView)itemView.findViewById(R.id.tv_quantity);
            tv_plus = (TextView)itemView.findViewById(R.id.tv_plus);
            txtDiscountOff = (TextView)itemView.findViewById(R.id.txtDiscountOff);
            tv_star = (TextView)itemView.findViewById(R.id.tv_star);
            tv_rating = (TextView)itemView.findViewById(R.id.tv_rating);
            ll_quantity = (LinearLayout)itemView.findViewById(R.id.ll_quantity);
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
            rl_wish.setVisibility(View.GONE);

            tv_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(itemView.getContext());
                    builder.setMessage("Are you sure want to remove the product ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            addWishList("no", getAdapterPosition(),mdata,WishListAdapter.this,itemView.getContext());
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


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productListener.productClickLisener(mdata.get(getAdapterPosition()));
                    productListener.updateCartCount(mdata.get(getAdapterPosition()).getCartCount());
                }
            });

            tv_notify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //rl_send_notify.setVisibility(View.VISIBLE);
                    //tv_notify.setVisibility(View.GONE);
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

        private void notifyMe() {
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
                AppUtils.addTocart(qty,getAdapterPosition(),mdata,WishListAdapter.this,itemView.getContext(),this);
            }else{
                Toast.makeText(itemView.getContext(), "You can not add any more quantities for this product!", Toast.LENGTH_SHORT).show();
                mdata.get(getAdapterPosition()).setLoading(false);
                notifyItemChanged(getAdapterPosition());
            }
        }


        public void addWishList(String wishList, final int position, List<Product> mdata, RecyclerView.Adapter adapter, Context context){
            JsonObject jsonObject = new JsonObject();
            if(AppConstant.isLogin(null)){
                jsonObject.addProperty("userId", AppUtils.getUserDetails(null).getLoginId());
            }else{
                jsonObject.addProperty("userId", "");
            }
            jsonObject.addProperty("tempUserId", AppController.getInstance().getUniqueID());
            jsonObject.addProperty("productId",mdata.get(position).getProductId());
            jsonObject.addProperty("productVarientId", mdata.get(position).getProductVarientId());
            jsonObject.addProperty("wishList",wishList);

            new RestClient().getApiService().addWishList(jsonObject, new Callback<Product>() {
                @Override
                public void success(Product product, Response response) {
                    if(product.getSuccess().equals("1")){
                        mdata.remove(position);
                        adapter.notifyDataSetChanged();
                        if(weakWishlistFragment.get()!=null)
                            if(mdata.size()==0){
                                weakWishlistFragment.get().rl_noDataFound.setVisibility(View.VISIBLE);
                            }else{
                                weakWishlistFragment.get().rl_noDataFound.setVisibility(View.GONE);
                            }
                        //Toast.makeText(context, product.getMessage(), Toast.LENGTH_SHORT).show();
                    }else{
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

    public interface OnWeightItemCLickListener{
        void onWeightClicked(Product product);
    }
}
