package com.app.features.navmenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;
import com.app.activities.BaseActivity;
import com.app.activities.MainActivity;
import com.app.callback.CategoryListener;
import com.app.callback.HomeClickLisener;
import com.app.callback.HomePageListener;
import com.app.callback.ProductListener;
import com.app.constant.AppConstant;
import com.app.controller.AppController;
import com.app.features.home.model.Brand;
import com.app.features.home.model.Category;
import com.app.features.home.model.HomeModel;
import com.app.features.home.model.Product;
import com.app.features.home.model.SubCategory;
import com.app.features.product.ProductFragment;
import com.app.features.product.adapter.ViewAllFragment;
import com.app.features.productdetail.ProductDetailActivity;
import com.app.util.AppUtils;
import com.app.util.PrefUtil;
import com.app.util.RestClient;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;

import static com.app.activities.MainActivity.appBarContainer;

public class OfferActivity extends BaseActivity implements HomePageListener {

    FragmentManager fragmentManager;
    public static TextView tv_toolbar_offer;
    ProgressBar progressBar;
    ArrayList<Product> recentlyViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentManager = getSupportFragmentManager();
        tv_toolbar_offer = (TextView)findViewById(R.id.tv_toolbar_offer);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        getHomeData();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                //finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void productClickLisener(Product product) {
        Intent intent = new Intent(OfferActivity.this, ProductDetailActivity.class);
        intent.putExtra("productId", product.getProductId());
        startActivity(intent);
    }

    @Override
    public void updateCartCount(String cartCount) {
        setCartCount();
    }

    @Override
    public void categoryClickLisener(Category category) {

    }

    @Override
    public void subcategoryClickLisener(Category category, SubCategory subCategories) {

    }

    @Override
    public void productClickLisener(Category category) {

    }

    @Override
    public void orderClickLisener(Category category) {

    }


    private void getHomeData(){
        progressBar.setVisibility(View.VISIBLE);
        JsonObject jsonObject = new JsonObject();
        if(AppConstant.isLogin(this)){
            jsonObject.addProperty("userId", AppUtils.getUserDetails(this).getLoginId());
        }else{
            jsonObject.addProperty("userId", "");
        }
        jsonObject.addProperty("tempUserId", AppController.getInstance().getUniqueID());

        new RestClient().getApiService().home(jsonObject, new Callback<HomeModel>() {
            @Override
            public void success(HomeModel modCategory, Response response) {
                progressBar.setVisibility(View.GONE);
                if(modCategory.getSuccess().equals("1")){
                    if(modCategory.getCount_cart()!= null) {
                        PrefUtil.getInstance(OfferActivity.this).putData(AppConstant.PREF_CART_COUNT, modCategory.getCount_cart());
                        AppUtils.setCartCount(modCategory.getCount_cart());
                        setCartCount();
                    }
                    recentlyViewList = modCategory.getProduct();
                    if(getIntent().getStringExtra("product").equals("discount")){
                        Fragment fragment=new ViewAllFragment();
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("List", (Serializable) recentlyViewList);
                        bundle.putString("type", "recently");
                        fragment.setArguments(bundle);
                        fragmentManager.beginTransaction().replace(R.id.offer_container ,fragment)
                                .commit();
                    }else {
                        Fragment fragment=new ViewAllFragment();
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("List", (Serializable) recentlyViewList);
                        bundle.putString("type", "recently");
                        fragment.setArguments(bundle);
                        fragmentManager.beginTransaction().replace(R.id.offer_container ,fragment)
                                .commit();
                    }
                }else{
                    Toast.makeText(OfferActivity.this, modCategory.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(OfferActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void brandLisener(Brand brand) {

    }
}
