package com.app.features.navmenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.app.R;
import com.app.callback.HomeClickLisener;
import com.app.features.home.Category;
import com.app.features.home.SubCategory;

public class OrderActivity extends AppCompatActivity implements HomeClickLisener {

    FragmentManager fragmentManager;
    public static TextView tv_toolbar_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_toolbar_order = (TextView)findViewById(R.id.tv_toolbar_order);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.order_container, new OrderFragment()).commit();
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

    }

    @Override
    public void orderClickLisener(Category category) {
        fragmentManager.beginTransaction().replace(R.id.order_container , new OrderDetailFragment())
                .addToBackStack(null)
                .commit();
    }


}
