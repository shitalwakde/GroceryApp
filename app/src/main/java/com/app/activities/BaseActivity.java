package com.app.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.R;
import com.app.constant.AppConstant;
import com.app.features.address.BottomSheetLocationFragment;
import com.app.features.cart.CartActivity;
import com.app.features.cart.CartFragment;
import com.app.features.login.LoginActivity;
import com.app.util.AppUtils;
import com.app.util.PrefUtil;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

public class BaseActivity extends AppCompatActivity {

    public static TextView tv;
    public ImageView iv;

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
        iv = (ImageView) MenuItemCompat.getActionView(item).findViewById(R.id.actionbar_notifcation_iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        /*Intent sendIntent = new Intent("android.intent.action.MAIN");
        sendIntent.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));
        // sendIntent.putExtra("jid",     PhoneNumberUtils.stripSeparators(getIntent().getStringExtra("mobile"))+"@s.whatsapp.net");//phone number without "+" prefix
        //  sendIntent.putExtra("jid",     PhoneNumberUtils.stripSeparators("91"+"8962370822")+"@s.whatsapp.net");//phone number without "+" prefix
        sendIntent.putExtra("jid",     PhoneNumberUtils.stripSeparators("91"+"9999999999")+"@s.whatsapp.net");//phone number without "+" prefix

        try {
            startActivity(sendIntent);
        } catch (android.content.ActivityNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.whatsapp")));
        }*/

        return true;
    }


    public void setCartCount() {
        if (AppUtils.getCartCount(this).equals("0")) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(AppUtils.getCartCount(this));
        }
    }

    public void checkLocationPermission(MultiplePermissionsListener listener) {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ).withListener(listener).check();

    }
}
