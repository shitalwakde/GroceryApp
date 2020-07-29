package com.app.features.productdetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.app.R;
import com.app.activities.BaseActivity;
import com.app.callback.HomePageListener;
import com.app.features.home.model.Brand;
import com.app.features.home.model.Category;
import com.app.features.home.model.Product;
import com.app.features.home.model.SubCategory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

public class ProductDetailActivity extends BaseActivity implements HomePageListener{

    FragmentManager fragmentManager;
    private String productId="", productVarientId ="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productId = getIntent().getStringExtra("productId");
        productVarientId = getIntent().getStringExtra("productVarientId");

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.productContainer, new ProductDetailFragment(productId, productVarientId)).commit();
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
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("productId", product.getProductId());
        intent.putExtra("productVarientId", product.getProductVarientId());
        startActivity(intent);
    }

    @Override
    public void updateCartCount(String cartCount) {
        setCartCount();
    }

    @Override
    public void productImageClickLisener(Product product) {

    }

    @Override
    public void productVieMoreClickLisener(Product product, String type) {
        /*Fragment fragment = new ViewAllFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("List", (Serializable) recentlyViewList);
        bundle.putString("type", "recently");
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.productContainer, fragment)
                .addToBackStack(null)
                .commit();*/
    }

    @Override
    public void brandLisener(Brand brand) {

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

}
