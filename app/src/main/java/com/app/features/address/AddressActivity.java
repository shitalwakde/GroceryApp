package com.app.features.address;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.app.R;
import com.app.features.cart.CartFragment;
import com.app.features.checkout.CheckOutFragment;

import static com.app.features.cart.CartActivity.cartContainer;

public class AddressActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    public static TextView tv_toolbar_address;
    String deliveryId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_toolbar_address = (TextView)findViewById(R.id.tv_toolbar_address);
        fragmentManager = getSupportFragmentManager();
        deliveryId = getIntent().getStringExtra("deliveryId");

        if(getIntent().getStringExtra("address").equals("add")) {
            fragmentManager.beginTransaction().replace(R.id.address_container, new AddressFragment()).commit();
        }else if(getIntent().getStringExtra("address").equals("checkout")){
            fragmentManager.beginTransaction().replace(R.id.address_container, new CheckOutFragment(deliveryId)).commit();
        }else{
            fragmentManager.beginTransaction().replace(R.id.address_container, new AddressListFragment()).commit();
        }


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
}
