package com.app.features.cart;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.util.Log;
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
import com.app.callback.CalculateLisener;
import com.app.callback.OnItemCountChanged;
import com.app.callback.ProductListener;
import com.app.constant.AppConstant;
import com.app.controller.AppController;
import com.app.features.home.model.Product;
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

        if(category.getIsVarient().equals("Yes")){
            holder.rl_weight.setVisibility(View.VISIBLE);
            holder.tvPiece.setVisibility(View.GONE);
            holder.tv_peice.setText(category.getQuantity());
            /*if(category.getProductType().equals("Quantity")){
                holder.tv_peice.setText(category.getQuantity()+" Pc");
            }else{
            }*/
        }else{
            holder.rl_weight.setVisibility(View.GONE);
            holder.tvPiece.setVisibility(View.VISIBLE);
            holder.tvPiece.setText(category.getQuantity()+" Pc");
            /*if(category.getProductType().equals("Quantity")){
            }else{
                holder.tvPiece.setText(category.getQuantity()+" Kg");
            }*/
        }

        holder.rl_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> weightStrings = new ArrayList<>();
                for (Product weight : category.getVarientList()) {
                    weightStrings.add(weight.getQuantity());
                }
                final CharSequence[] items = weightStrings.toArray(new CharSequence[weightStrings.size()]);

//                new ContextThemeWrapper(context, R.style.AlertDialogCustom)
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("Select...");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        selected = items[item].toString();
                        category.setWeightSelected(item);
                        category.setProductVarientId(category.getProductVarientId());
                        getProductDetailsByWeight(position, selected, category);
                    }
                });
                android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void getProductDetailsByWeight(int position, String weight, Product category) {
        for (int i=0; i<category.getVarientList().size(); i++){
            if(weight.equals(category.getVarientList().get(i).getQuantity())){
                category.setProductVarientId(category.getVarientList().get(i).getProductVarientId());
                category.setImage(category.getVarientList().get(i).getImage());
                category.setDiscount(category.getVarientList().get(i).getDiscount());
                category.setQuantity(category.getVarientList().get(i).getQuantity());
                category.setFinalAmount(category.getVarientList().get(i).getFinalAmount());
                category.setGrossAmount(category.getVarientList().get(i).getGrossAmount());
                notifyDataSetChanged();
                notifyItemChanged(position);
            }

        }
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements OnItemCountChanged {
        ImageView iv_best, iv_unwish;
        TextView tv_pr_name, tv_pr_sub_name, tv_price, tv_discount_price, tv_add, tv_remove, tv_minus, tv_quantity, tv_plus, txtDiscountOff, tvPiece, tv_peice;
        LinearLayout ll_quantity;
        RelativeLayout rl_wish, rl_discount, rl_addCartContainer, rl_weight;
        ProgressBar progressBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_remove = (TextView)itemView.findViewById(R.id.tv_remove);
            iv_best = (ImageView)itemView.findViewById(R.id.iv_best);
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
            ll_quantity = (LinearLayout)itemView.findViewById(R.id.ll_quantity);
            iv_unwish = (ImageView)itemView.findViewById(R.id.iv_unwish);
            rl_wish = (RelativeLayout)itemView.findViewById(R.id.rl_wish);
            rl_discount = (RelativeLayout)itemView.findViewById(R.id.rl_discount);
            rl_weight = (RelativeLayout)itemView.findViewById(R.id.rl_weight);
            rl_addCartContainer = (RelativeLayout)itemView.findViewById(R.id.rl_addCartContainer);
            progressBar = itemView.findViewById(R.id.progressBar);

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
            addTocart(qty,getAdapterPosition(),mdata,CartAdapter.this,itemView.getContext(),this);
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

    }


}
