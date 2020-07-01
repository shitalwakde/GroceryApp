package com.app.features.wallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.TextView;

import com.app.R;
import com.app.features.navmenu.OrderFragment;

public class WalletActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    public static TextView tv_toolbar_wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_toolbar_wallet = (TextView)findViewById(R.id.tv_toolbar_wallet);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.wallet_container, new WalletFragment()).commit();
    }
}
