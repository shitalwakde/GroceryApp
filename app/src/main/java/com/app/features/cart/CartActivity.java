package com.app.features.cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.app.R;
import com.app.callback.ProductListener;
import com.app.features.home.model.Product;
import com.app.features.productdetail.ProductDetailActivity;

public class CartActivity extends AppCompatActivity implements ProductListener {

    FragmentManager fragmentManager;
    public static int cartContainer;
    public static TextView tv_toolbar_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_toolbar_cart = (TextView)findViewById(R.id.tv_toolbar_cart);
        cartContainer = R.id.cart_container;
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(cartContainer, new CartFragment()).commit();
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
        Intent intent = new Intent(CartActivity.this, ProductDetailActivity.class);
        intent.putExtra("productId", product.getProductId());
        startActivity(intent);
    }

    @Override
    public void updateCartCount(String cartCount) {
        //((new CartActivity())).setCartCount(Integer.parseInt(cartCount));
    }

    @Override
    public void productImageClickLisener(Product product) {

    }

    @Override
    public void productVieMoreClickLisener(Product product, String type) {

    }
}
