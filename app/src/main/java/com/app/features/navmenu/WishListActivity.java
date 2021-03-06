package com.app.features.navmenu;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.app.R;
import com.app.activities.BaseActivity;
import com.app.callback.ProductListener;
import com.app.features.home.model.Product;
import com.app.features.productdetail.ProductDetailActivity;

public class WishListActivity extends BaseActivity implements ProductListener {

    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.wishlist_container, new WishListFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //onBackPressed();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void productClickLisener(Product product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("productId", product.getProductId());
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

    }
}
