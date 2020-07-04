package com.app.features.navmenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.app.R;
import com.app.callback.HomeClickLisener;
import com.app.features.home.model.Category;
import com.app.features.product.ProductFragment;
import com.app.features.productdetail.ProductDetailActivity;

public class OfferActivity extends AppCompatActivity implements HomeClickLisener {

    FragmentManager fragmentManager;
    public static TextView tv_toolbar_offer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentManager = getSupportFragmentManager();
        tv_toolbar_offer = (TextView)findViewById(R.id.tv_toolbar_offer);

        if(getIntent().getStringExtra("product").equals("discount")){
            fragmentManager.beginTransaction().replace(R.id.offer_container, new ProductFragment("", "", "", "")).commit();
        }else {
            fragmentManager.beginTransaction().replace(R.id.offer_container, new ProductFragment("", "", "", "")).commit();
        }
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
    public void productClickLisener(Category category) {
        Intent intent = new Intent(OfferActivity.this, ProductDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void orderClickLisener(Category category) {

    }
}
