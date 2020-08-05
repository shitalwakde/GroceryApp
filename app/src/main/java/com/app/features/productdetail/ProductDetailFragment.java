package com.app.features.productdetail;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.app.callback.ProductListener;
import com.app.constant.AppConstant;
import com.app.controller.AppController;
import com.app.features.home.adapter.HealthAdapter;
import com.app.features.home.model.Category;
import com.app.features.home.model.Product;
import com.app.features.login.LoginActivity;
import com.app.features.home.adapter.WeighAdapter;
import com.app.features.navmenu.WishListAdapter;
import com.app.features.product.adapter.ViewAllFragment;
import com.app.util.AppUtils;
import com.app.util.RestClient;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProductDetailFragment extends Fragment implements ProductListener {
    View rootView;
    ProductListener listener;
    FragmentManager fragmentManager;
    ImageView iv_product, iv_unwish, iv_close, iv_thumb, iv_similarProduct, iv_recentlyProduct;
    TextView tv_price, tv_rate_product, tv_add, tv_minus, tv_quantity, tv_plus, tv_pr_name, tv_pr_sub_name,
            tv_discount_price, tvPiece, tv_star,tv_rating, tv_peice, tv_offer, tv_description, tv_notify, tv_send_notify,
            tv_reviewLike, tv_reviewCount, rating_title, tv_star_line, rating_comment, tv_star1, tv_total_rating, tv_view_all;
    RelativeLayout rl_like, rl_weight, rl_view, rl_send_notify, rl_outOfStock, rl_addCart, rl_rating, rl_thumb, rl_review, rl_view_all_rating;
    LinearLayout ll_quantity;
    ProgressBar progressBar;
    public static TextView tv;
    RecyclerView rv_related_img, rv_related_img1, rv_product_img;
    List<Category> bestSellingList;
    boolean flag;
    Product category;
    public static final int ADD=1;
    public static final int REMOVE=2;
    public static final int RESET=3;
    String wishList="", selected="";
    List<Product> recentlyViewList;
    ArrayList<Product> productsReviewList;
    String productId;
    String productVarientId;
    EditText et_notify_email;

    public ProductDetailFragment(String productId, String productVarientId) {
        this.productId = productId;
        this.productVarientId = productVarientId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.product_detail_fragment, container, false);

        init();
        return rootView;
    }

    private void init(){
        fragmentManager = getActivity().getSupportFragmentManager();
        bestSellingList = new ArrayList<>();
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        iv_product = (ImageView) rootView.findViewById(R.id.iv_product);
        iv_unwish = (ImageView) rootView.findViewById(R.id.iv_unwish);
        iv_close = (ImageView) rootView.findViewById(R.id.iv_close);
        iv_thumb = (ImageView) rootView.findViewById(R.id.iv_thumb);
        iv_similarProduct = (ImageView) rootView.findViewById(R.id.iv_similarProduct);
        iv_recentlyProduct = (ImageView) rootView.findViewById(R.id.iv_recentlyProduct);
        tv_price = (TextView) rootView.findViewById(R.id.tv_price);
        tv_rate_product = (TextView) rootView.findViewById(R.id.tv_rate_product);
        tv_add = (TextView) rootView.findViewById(R.id.tv_add);
        tv_minus = (TextView) rootView.findViewById(R.id.tv_minus);
        tv_quantity = (TextView) rootView.findViewById(R.id.tv_quantity);
        tv_plus = (TextView) rootView.findViewById(R.id.tv_plus);
        tv_pr_name = (TextView) rootView.findViewById(R.id.tv_pr_name);
        tv_pr_sub_name = (TextView) rootView.findViewById(R.id.tv_pr_sub_name);
        tv_discount_price = (TextView) rootView.findViewById(R.id.tv_discount_price);
        tvPiece = (TextView) rootView.findViewById(R.id.tvPiece);
        tv_star = (TextView) rootView.findViewById(R.id.tv_star);
        tv_rating = (TextView) rootView.findViewById(R.id.tv_rating);
        tv_peice = (TextView) rootView.findViewById(R.id.tv_peice);
        tv_offer = (TextView) rootView.findViewById(R.id.tv_offer);
        tv_description = (TextView) rootView.findViewById(R.id.tv_description);
        tv_reviewLike = (TextView) rootView.findViewById(R.id.tv_reviewLike);
        tv_reviewCount = (TextView) rootView.findViewById(R.id.tv_reviewCount);
        rating_title = (TextView) rootView.findViewById(R.id.rating_title);
        tv_star_line = (TextView) rootView.findViewById(R.id.tv_star_line);
        rating_comment = (TextView) rootView.findViewById(R.id.rating_comment);
        tv_star1 = (TextView) rootView.findViewById(R.id.tv_star1);
        tv_total_rating = (TextView) rootView.findViewById(R.id.tv_total_rating);
        tv_view_all = (TextView) rootView.findViewById(R.id.tv_view_all);
        rating_comment = (TextView) rootView.findViewById(R.id.rating_comment);
        rv_related_img = (RecyclerView) rootView.findViewById(R.id.rv_related_img);
        rv_related_img1 = (RecyclerView) rootView.findViewById(R.id.rv_related_img1);
        rv_product_img = (RecyclerView) rootView.findViewById(R.id.rv_product_img);
        rl_like = (RelativeLayout) rootView.findViewById(R.id.rl_like);
        rl_weight = (RelativeLayout) rootView.findViewById(R.id.rl_weight);
        ll_quantity = (LinearLayout) rootView.findViewById(R.id.ll_quantity);
        tv_notify = (TextView) rootView.findViewById(R.id.tv_notify);
        tv_send_notify = (TextView) rootView.findViewById(R.id.tv_send_notify);
        rl_view = (RelativeLayout) rootView.findViewById(R.id.rl_view);
        rl_send_notify = (RelativeLayout) rootView.findViewById(R.id.rl_send_notify);
        rl_outOfStock = (RelativeLayout) rootView.findViewById(R.id.rl_outOfStock);
        rl_rating = (RelativeLayout) rootView.findViewById(R.id.rl_rating);
        rl_addCart = (RelativeLayout) rootView.findViewById(R.id.rl_addCart);
        rl_thumb = (RelativeLayout) rootView.findViewById(R.id.rl_thumb);
        rl_review = (RelativeLayout) rootView.findViewById(R.id.rl_review);
        rl_view_all_rating = (RelativeLayout) rootView.findViewById(R.id.rl_view_all_rating);
        et_notify_email = (EditText) rootView.findViewById(R.id.et_notify_email);
    }

    @Override
    public void onResume() {
        super.onResume();
        getProductDetail();
    }

    private void click() {

        rl_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppConstant.isLogin(getActivity())){
                    if(category.getIsInWishList()!=null && category.getIsInWishList().equalsIgnoreCase("yes")){
                        addWishList("no");
                    }else{
                        addWishList("yes");
                    }
                }else {
                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    builder.setMessage("Please login to add product in wishlist");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
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
        int maxQuantity = Integer.parseInt(category.getMaxProductQuantity());
        if(qty <= maxQuantity){
            addTocart(qty);
        }else{
            Toast.makeText(getActivity(), "You can not add any more quantities for this product!", Toast.LENGTH_SHORT).show();
            //category.setLoading(false);
        }

    }



    private void addLikeDislike(String dislike, String reviewId){
        JsonObject jsonObject = new JsonObject();
        if(AppConstant.isLogin(getActivity())){
            jsonObject.addProperty("userId", AppUtils.getUserDetails(getActivity()).getLoginId());
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
                    category=product;
                    category.setLikeDisLike(dislike);
                    setReviewLike();
                }else{
                    Toast.makeText(getActivity(), product.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getProductDetail(){
        progressBar.setVisibility(View.VISIBLE);
        JsonObject jsonObject = new JsonObject();
        if(AppConstant.isLogin(getActivity())){
            jsonObject.addProperty("userId", AppUtils.getUserDetails(getActivity()).getLoginId());
        }else{
            jsonObject.addProperty("userId", "");
        }
        jsonObject.addProperty("tempUserId", AppController.getInstance().getUniqueID());
        jsonObject.addProperty("productId", productId);
        jsonObject.addProperty("productVarientId", productVarientId);

        new RestClient().getApiService().getProductDetail(jsonObject, new Callback<Product>() {
            @Override
            public void success(Product productDetailModel, Response response) {
                progressBar.setVisibility(View.GONE);
                if(productDetailModel.getSuccess().equals("1")){
                    AppUtils.setCartCount(productDetailModel.getCartCount());
                    if (getContext() != null)
                        ((ProductDetailActivity) getContext()).setCartCount();
                    category=productDetailModel;
                    productsReviewList = category.getProductsReview();
                    setProductDetails();
                    recentlyViewList = productDetailModel.getRecentViewProduct();
                }else {
                    Toast.makeText(getActivity(), productDetailModel.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setProductDetails() {
        click();
        //category.setCartQuantity(category.getCartQuantityInteger());
        arangeResentProduct(category.getRecentViewProduct());
        arangeSimilarProduct(category.getSimilarProductList());
        arrangeProductImage(category.getProductsImageList());
        arrangeProductReview(category.getProductsReview());
        Picasso.with(getActivity()).load(category.getImage()).into(iv_product);
        tv_pr_name.setText(category.getName());
        tv_pr_sub_name.setText(category.getBrandName());
        Picasso.with(getActivity()).load(category.getRecentlyViewBgImage()).into(iv_recentlyProduct);
        Picasso.with(getActivity()).load(category.getSimilarProductBgImage()).into(iv_similarProduct);
        /*if(category.getRating() == 0){
            rl_rating.setVisibility(View.INVISIBLE);
        }else{
            rl_rating.setVisibility(View.VISIBLE);
            tv_star.setText(String.valueOf(category.getRating()));
        }*/
        tv_star.setText(String.valueOf(category.getRating()));
        if(category.getRate().equals("")){
            tv_rating.setVisibility(View.INVISIBLE);
        }else{
            tv_rating.setVisibility(View.VISIBLE);
            tv_rating.setText(category.getRate()+" Ratings");
        }
        tv_star1.setText(String.valueOf(category.getRating()));
        if(category.getRate().equals("")){
            tv_total_rating.setVisibility(View.INVISIBLE);
        }else{
            tv_total_rating.setVisibility(View.VISIBLE);
            tv_total_rating.setText(category.getRate()+" Ratings");
        }
        tv_view_all.setText("All "+category.getRate()+ " reviewes");
        tv_discount_price.setText("\u20B9 "+category.getFinalAmount());
        tv_description.setText(category.getDescription());

        if(category.getDiscount().equals("0")){
            tv_price.setVisibility(View.GONE);
            tv_offer.setVisibility(View.GONE);
        }else{
            tv_price.setVisibility(View.VISIBLE);
            tv_offer.setVisibility(View.VISIBLE);
            tv_price.setPaintFlags(tv_price.getPaintFlags()
                    | Paint.STRIKE_THRU_TEXT_FLAG);
            tv_price.setText("\u20B9 "+category.getGrossAmount());
            tv_offer.setText(category.getDiscount()+" % off");
        }

        if(category.getMaxProductQuantity().equals("0")){
            rl_outOfStock.setVisibility(View.VISIBLE);
            tv_notify.setVisibility(View.VISIBLE);
        }else {
            tv_notify.setVisibility(View.GONE);
            rl_outOfStock.setVisibility(View.GONE);
            rl_view.setVisibility(View.VISIBLE);
        }

        tv_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppConstant.isLogin(getContext())){
                    notifyMe();
                }else{
                    AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                    builder.setMessage("Please login to continue");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            intent.putExtra("type","login");
                            startActivity(intent);
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

        if(category.getIsVarient().equals("Yes")){
            rl_weight.setVisibility(View.VISIBLE);
            tvPiece.setVisibility(View.GONE);
            tv_peice.setText(category.getQuantity());
        }else{
            rl_weight.setVisibility(View.GONE);
            tvPiece.setVisibility(View.VISIBLE);
            tvPiece.setText(category.getQuantity());
        }

        rl_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        rl_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(category.getLikeDisLike().equals("Like")){
                    addLikeDislike("Dislike", category.getReviewId());
                }else {
                    addLikeDislike("Like", category.getReviewId());
                }
            }
        });
    }

    private void notifyMe() {
    }

    private void arrangeProductReview(ArrayList<Product> productsReview) {
        if(productsReview != null){
            rl_review.setVisibility(View.VISIBLE);
            category.setReviewId(productsReview.get(0).getReviewId());
            tv_star_line.setText(String.valueOf(productsReview.get(0).getRating()));
            rating_title.setText(productsReview.get(0).getRatingComment());
            rating_comment.setText(productsReview.get(0).getComment());
            tv_reviewCount.setText(productsReview.get(0).getLike());

            rl_view_all_rating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getActivity(), ProductReviewActivity.class);
                    intent.putExtra("List", productsReviewList);
                    startActivity(intent);

//                    Fragment fragment = new ProductReviewFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("List", (Serializable) productsReviewList);
//                    fragment.setArguments(bundle);
//                    fragmentManager.beginTransaction().replace(R.id.productContainer, fragment)
//                            .addToBackStack(null)
//                            .commit();
                }
            });
        }else {
            rl_review.setVisibility(View.GONE);
        }
    }

    private void setReviewLike(){
        if(category.getLikeDisLike().equals("Like")){
            tv_reviewCount.setText(category.getLikeCount());
            tv_reviewLike.setText("Liked");
            tv_reviewLike.setTextColor(Color.parseColor("#2196F3"));
        }else{
            tv_reviewLike.setText("Like");
        }
    }

    private void showDialog() {
        final Dialog dialog1 = new Dialog(getActivity());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(true);
        dialog1.setContentView(R.layout.dlg_varient);
        TextView tv_item_name = (TextView) dialog1.findViewById(R.id.tv_item_name);
        RecyclerView rv_item = (RecyclerView) dialog1.findViewById(R.id.rv_item);

        tv_item_name.setText(category.getName());

        WeighAdapter adapter = new WeighAdapter(category.getVarientList(), new WishListAdapter.OnWeightItemCLickListener() {
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
                setProductDetails();
            }
        });
        rv_item.setAdapter(adapter);

        dialog1.show();
    }

    private void arangeResentProduct(ArrayList<Product> recentProduct){
        HealthAdapter adapter2 = new HealthAdapter(listener, recentProduct, this, "recentProduct");
        rv_related_img.setAdapter(adapter2);
    }

    private void arangeSimilarProduct(ArrayList<Product> similarProduct){
        HealthAdapter adapter2 = new HealthAdapter(listener, similarProduct, this, "similarProduct");
        rv_related_img1.setAdapter(adapter2);
    }

    private void arrangeProductImage(ArrayList<Product> productsImageList) {
        ProductImageAdapter adapter = new ProductImageAdapter(this, productsImageList);
        rv_product_img.setAdapter(adapter);
    }


    private void addTocart(int qty){
        JsonObject jsonObject = new JsonObject();
        if(AppConstant.isLogin(getActivity())){
            jsonObject.addProperty("userId", AppUtils.getUserDetails(getActivity()).getLoginId());
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
                    if (getContext() != null)
                        ((ProductDetailActivity) getContext()).setCartCount();
                    //Toast.makeText(ProductDetailActivity.this, product.getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), product.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void addWishList(String wishList){
        JsonObject jsonObject = new JsonObject();
        if(AppConstant.isLogin(getActivity())){
            jsonObject.addProperty("userId", AppUtils.getUserDetails(getActivity()).getLoginId());
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
                    Toast.makeText(getActivity(), product.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof ProductDetailActivity){
            listener= (ProductListener) context;
        }
    }


    @Override
    public void productClickLisener(Product product) {
        if(product.getProductId() != null) {
            Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
            intent.putExtra("productId", product.getProductId());
            intent.putExtra("productVarientId", product.getProductVarientId());
            startActivity(intent);
        }
    }

    @Override
    public void updateCartCount(String cartCount) {

    }

    @Override
    public void productImageClickLisener(Product product) {
        Picasso.with(getActivity()).load(product.getImage()).into(iv_product);
    }

    @Override
    public void productVieMoreClickLisener(Product product, String type) {
        Fragment fragment = new ViewAllFragment();
        Bundle bundle = new Bundle();
        //bundle.putSerializable("List", (Serializable) recentlyViewList);
        bundle.putString("type", type);
        if(product.getProductId() != null) {
            bundle.putString("productId", product.getProductId());
        }
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.productContainer, fragment)
                .addToBackStack(null)
                .commit();
    }


}
