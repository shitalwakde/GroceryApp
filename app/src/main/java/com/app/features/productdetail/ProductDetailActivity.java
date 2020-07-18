package com.app.features.productdetail;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;
import com.app.activities.BaseActivity;
import com.app.activities.MainActivity;
import com.app.callback.ProductListener;
import com.app.constant.AppConstant;
import com.app.controller.AppController;
import com.app.features.home.model.Category;
import com.app.features.home.adapter.HealthAdapter;
import com.app.features.home.model.Product;
import com.app.features.login.LoginActivity;
import com.app.util.AppUtils;
import com.app.util.RestClient;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static java.security.AccessController.getContext;

public class ProductDetailActivity extends BaseActivity implements ProductListener {

    ImageView iv_product, iv_unwish;
    TextView tv_price, tv_rate_product, tv_add, tv_minus, tv_quantity, tv_plus, tv_pr_name, tv_pr_sub_name,
            tv_discount_price, tvPiece, tv_star,tv_rating, tv_peice, tv_offer, tv_description;
    RelativeLayout rl_like, rl_weight;
    LinearLayout ll_quantity;
    ProgressBar progressBar;
    FragmentManager fragmentManager;
    public static TextView tv;
    RecyclerView rv_related_img, rv_related_img1;
    List<Category> bestSellingList;
    public static int productContainer;
    boolean flag;
    Product category;
    public static final int ADD=1;
    public static final int REMOVE=2;
    public static final int RESET=3;
    private String productId="", productVarientId ="";
    String wishList="", selected="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail_fragment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

        productContainer = R.id.productContainer;
    }

    private void init() {
        productId = getIntent().getStringExtra("productId");
        productVarientId = getIntent().getStringExtra("productVarientId");
        fragmentManager = getSupportFragmentManager();
        bestSellingList = new ArrayList<>();
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        iv_product = (ImageView) findViewById(R.id.iv_product);
        iv_unwish = (ImageView) findViewById(R.id.iv_unwish);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_rate_product = (TextView) findViewById(R.id.tv_rate_product);
        tv_add = (TextView) findViewById(R.id.tv_add);
        tv_minus = (TextView)findViewById(R.id.tv_minus);
        tv_quantity = (TextView)findViewById(R.id.tv_quantity);
        tv_plus = (TextView)findViewById(R.id.tv_plus);
        tv_pr_name = (TextView)findViewById(R.id.tv_pr_name);
        tv_pr_sub_name = (TextView)findViewById(R.id.tv_pr_sub_name);
        tv_discount_price = (TextView)findViewById(R.id.tv_discount_price);
        tvPiece = (TextView)findViewById(R.id.tvPiece);
        tv_star = (TextView)findViewById(R.id.tv_star);
        tv_rating = (TextView)findViewById(R.id.tv_rating);
        tv_peice = (TextView)findViewById(R.id.tv_peice);
        tv_offer = (TextView)findViewById(R.id.tv_offer);
        tv_description = (TextView)findViewById(R.id.tv_description);
        rv_related_img = (RecyclerView)findViewById(R.id.rv_related_img);
        rv_related_img1 = (RecyclerView)findViewById(R.id.rv_related_img1);
        rl_like = (RelativeLayout)findViewById(R.id.rl_like);
        rl_weight = (RelativeLayout)findViewById(R.id.rl_weight);
        ll_quantity = (LinearLayout)findViewById(R.id.ll_quantity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProductDetail();
    }

    private void click() {

        rl_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppConstant.isLogin(ProductDetailActivity.this)){
                    if(category.getIsInWishList()!=null && category.getIsInWishList().equalsIgnoreCase("yes")){
                        addWishList("no");
                    }else{
                        addWishList("yes");
                    }
//                    if(!flag){
//                        iv_unwish.setImageResource(R.drawable.ic_heart);
//                        addWishList("no");
//                        flag=false;
//                    }else {
//                        iv_unwish.setImageResource(R.drawable.ic_heart_red);
//                        addWishList("yes");
//                        flag = true;
//                    }
                }else {
                    AlertDialog.Builder builder=new AlertDialog.Builder(ProductDetailActivity.this);
                    builder.setMessage("Please login to add product in wishlist");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(ProductDetailActivity.this, LoginActivity.class);
                            intent.putExtra("type","login");
                            startActivity(intent);
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

        setQty();
        setWishDetails();

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeQty(0,ADD);
                setQty();
            }
        });

        tv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeQty(0,ADD);
                setQty();
            }
        });

        tv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeQty(0,REMOVE);
                setQty();
            }
        });
    }

    private void setWishDetails() {
        if(category.getIsInWishList().equalsIgnoreCase("no")){
            iv_unwish.setImageResource(R.drawable.ic_heart);
            //flag=true;
        }else{
            iv_unwish.setImageResource(R.drawable.ic_heart_red);
            //flag=false;
        }
    }

    private void setQty() {
        if(category.getCartQuantityInteger()<=0){
            tv_add.setVisibility(View.VISIBLE);
            ll_quantity.setVisibility(View.GONE);
        }else{
            ll_quantity.setVisibility(View.VISIBLE);
            tv_add.setVisibility(View.GONE);
            tv_quantity.setText(String.valueOf(category.getCartQuantityInteger()));
        }
    }

    private void changeQty(int adapterPosition,int type) {
        int qty=category.getCartQuantityInteger();
        if(type==ADD) {
            qty = qty + 1;
        }else if(type ==REMOVE) {
            qty = qty - 1;
        }else {
            qty = 0;
        }
        addTocart(qty);

    }

    private void getProductDetail(){
        progressBar.setVisibility(View.VISIBLE);
        JsonObject jsonObject = new JsonObject();
        if(AppConstant.isLogin(this)){
            jsonObject.addProperty("userId", AppUtils.getUserDetails(this).getLoginId());
        }else{
            jsonObject.addProperty("userId", "");
        }
        jsonObject.addProperty("tempUserId", AppController.getInstance().getUniqueID());
        jsonObject.addProperty("productId", productId);

        new RestClient().getApiService().getProductDetail(jsonObject, new Callback<Product>() {
            @Override
            public void success(Product productDetailModel, Response response) {
                progressBar.setVisibility(View.GONE);
                if(productDetailModel.getSuccess().equals("1")){
                    AppUtils.setCartCount(productDetailModel.getCartCount());
                    setCartCount();
                    setProductDetails(productDetailModel);
                }else {
                    Toast.makeText(ProductDetailActivity.this, productDetailModel.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ProductDetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setProductDetails(Product productDetailModel) {
        category=productDetailModel;
        click();
        category.setCartQuantity(category.getCartQuantityInteger());
        arangeResentProduct(productDetailModel.getRecentProduct());
        arangeSimilarProduct(productDetailModel.getSimilarProduct());
        Picasso.with(ProductDetailActivity.this).load(productDetailModel.getImage()).into(iv_product);
        tv_pr_name.setText(productDetailModel.getName());
        tv_pr_sub_name.setText(productDetailModel.getBrandName());
        tv_star.setText("4.5");
        tv_rating.setText(productDetailModel.getRate()+" Rating");
        tv_discount_price.setText("\u20B9 "+productDetailModel.getGrossAmount());
        tv_description.setText(productDetailModel.getDescription());

        if(productDetailModel.getDiscount().equals("0")){
            tv_price.setVisibility(View.GONE);
            tv_offer.setVisibility(View.GONE);
        }else{
            tv_price.setVisibility(View.VISIBLE);
            tv_offer.setVisibility(View.VISIBLE);
            tv_price.setPaintFlags(tv_price.getPaintFlags()
                    | Paint.STRIKE_THRU_TEXT_FLAG);
            tv_price.setText("\u20B9 "+productDetailModel.getFinalAmount());
            tv_offer.setText(productDetailModel.getDiscount()+" % off");
        }

        /*if(productDetailModel.getProductType().equals("Quantity")){
            rl_weight.setVisibility(View.GONE);
            tvPiece.setVisibility(View.VISIBLE);
            tvPiece.setText(productDetailModel.getQuantity()+" Pc");
        }else{
            rl_weight.setVisibility(View.VISIBLE);
            tvPiece.setVisibility(View.GONE);
            tv_peice.setText(productDetailModel.getQuantity()+" Kg");
        }*/

        if(productDetailModel.getIsVarient().equals("Yes")){
            rl_weight.setVisibility(View.VISIBLE);
            tvPiece.setVisibility(View.GONE);
            tv_peice.setText(productDetailModel.getQuantity());
            /*if(category.getProductType().equals("Quantity")){
                holder.tv_peice.setText(category.getQuantity()+" Pc");
            }else{
            }*/
        }else{
            rl_weight.setVisibility(View.GONE);
            tvPiece.setVisibility(View.VISIBLE);
            tvPiece.setText(productDetailModel.getQuantity()+" Pc");
            /*if(category.getProductType().equals("Quantity")){
            }else{
                holder.tvPiece.setText(category.getQuantity()+" Kg");
            }*/
        }

        rl_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> weightStrings = new ArrayList<>();
                for (Product weight : productDetailModel.getVarientList()) {
                    weightStrings.add(weight.getQuantity());
                }
                final CharSequence[] items = weightStrings.toArray(new CharSequence[weightStrings.size()]);

//                new ContextThemeWrapper(context, R.style.AlertDialogCustom)
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProductDetailActivity.this);
                builder.setTitle("Select...");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        selected = items[item].toString();
                        productDetailModel.setWeightSelected(item);
                        getProductDetailsByWeight(selected, productDetailModel);
                    }
                });
                android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void getProductDetailsByWeight(String weight, Product category) {
        for (int i = 0; i < category.getVarientList().size(); i++) {
            if(weight.equals(category.getVarientList().get(i).getQuantity())){
                category.setProductVarientId(category.getVarientList().get(i).getProductVarientId());
                category.setImage(category.getVarientList().get(i).getImage());
                category.setDiscount(category.getVarientList().get(i).getDiscount());
                category.setQuantity(category.getVarientList().get(i).getQuantity());
                category.setFinalAmount(category.getVarientList().get(i).getFinalAmount());
                category.setGrossAmount(category.getVarientList().get(i).getGrossAmount());
//                notifyDataSetChanged();
//                notifyItemChanged(position);
            }

        }
    }

    private void arangeResentProduct(ArrayList<Product> recentProduct){
        HealthAdapter adapter2 = new HealthAdapter(this, recentProduct);
        rv_related_img.setAdapter(adapter2);
    }

    private void arangeSimilarProduct(ArrayList<Product> similarProduct){
        HealthAdapter adapter2 = new HealthAdapter(this, similarProduct);
        rv_related_img1.setAdapter(adapter2);
    }


    private void addTocart(int qty){
        JsonObject jsonObject = new JsonObject();
        if(AppConstant.isLogin(this)){
            jsonObject.addProperty("userId", AppUtils.getUserDetails(this).getLoginId());
        }else{
            jsonObject.addProperty("userId", "");
        }
        jsonObject.addProperty("tempUserId", AppController.getInstance().getUniqueID());
        jsonObject.addProperty("productId", productId);
        jsonObject.addProperty("productVarientId", productVarientId);
        jsonObject.addProperty("quantity", String.valueOf(qty));

        new RestClient().getApiService().addToCart(jsonObject, new Callback<Product>() {
            @Override
            public void success(Product product, Response response) {
                if(product.getSuccess().equals("1")){
                    category.setCartQuantity(qty);

                    if(product.getQuantity().equalsIgnoreCase("0")){
                        tv_add.setVisibility(View.VISIBLE);
                        ll_quantity.setVisibility(View.GONE);
                    }else{
                        tv_add.setVisibility(View.GONE);
                        ll_quantity.setVisibility(View.VISIBLE);
                    }
                    AppUtils.setCartCount(product.getCartCount());
                    setCartCount();
                    //Toast.makeText(ProductDetailActivity.this, product.getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ProductDetailActivity.this, product.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(ProductDetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void addWishList(String wishList){
        JsonObject jsonObject = new JsonObject();
        if(AppConstant.isLogin(this)){
            jsonObject.addProperty("userId", AppUtils.getUserDetails(this).getLoginId());
        }else{
            jsonObject.addProperty("userId", "");
        }
        jsonObject.addProperty("tempUserId", AppController.getInstance().getUniqueID());
        jsonObject.addProperty("productId",productId);
        jsonObject.addProperty("productVarientId", productVarientId);
        jsonObject.addProperty("wishList",wishList);

        new RestClient().getApiService().addWishList(jsonObject, new Callback<Product>() {
            @Override
            public void success(Product product, Response response) {
                if(product.getSuccess().equals("1")){
                    category.setIsInWishList(wishList);
                    setWishDetails();
                    //Toast.makeText(ProductDetailActivity.this, product.getMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ProductDetailActivity.this, product.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(ProductDetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void productClickLisener(Product product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("productId", product.getProductId());
        intent.putExtra("productVarientId", product.getProductVarientId());
        startActivity(intent);
    }

    @Override
    public void updateCartCount(String cartCount) {
        setCartCount();
    }

}
