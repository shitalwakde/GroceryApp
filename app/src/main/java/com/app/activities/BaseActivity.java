package com.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.R;
import com.app.features.cart.CartActivity;
import com.app.features.cart.CartFragment;

public class BaseActivity extends AppCompatActivity {

    public static TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_new, menu);

        MenuItem item = menu.findItem(R.id.action_open_cart);
        MenuItemCompat.setActionView(item, R.layout.cart_new_layout);

        tv = (TextView) MenuItemCompat.getActionView(item).findViewById(R.id.actionbar_notifcation_textview);
        //tv.setText(sharedPreferences.getCartCount());
        ImageView iv = (ImageView) MenuItemCompat.getActionView(item).findViewById(R.id.actionbar_notifcation_iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        return true;
    }

}
