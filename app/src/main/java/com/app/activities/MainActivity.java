package com.app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.R;
import com.app.callback.HomeClickLisener;
import com.app.callback.DrawerItemClickLisener;
import com.app.databinding.ActivityMainBinding;
import com.app.features.productdetail.ProductDetailActivity;
import com.app.features.home.Category;
import com.app.features.home.HomeFragment;
import com.app.features.product.ProductFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BaseActivity implements DrawerItemClickLisener, HomeClickLisener {
    public static TextView tv;
    private ActivityMainBinding mView;
    FragmentManager fragmentManager;
    public static int containNav, appBarContainer;
    public static LinearLayout ll_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mView = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mView.included.toolbar);
        containNav = R.id.container_nav;
        appBarContainer = R.id.app_bar_container;
        ll_search = (LinearLayout)findViewById(R.id.ll_search);
        mView.included.bottomNav.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mView.drawer, mView.included.toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mView.drawer.setDrawerListener(toggle);
        toggle.syncState();

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(appBarContainer, new HomeFragment()).commit();
        fragmentManager.beginTransaction().replace(containNav , new NavigationViewFragment()).commit();
    }

    @Override
    public void drawerItemClickLisener(@NonNull NavMenu navMenu) {
        switch (navMenu.getMenu()){
            case Home:
                if (mView.drawer.isDrawerOpen(GravityCompat.START)) {
                    mView.drawer.closeDrawer(GravityCompat.START);
                }
                fragmentManager.beginTransaction().replace(appBarContainer, new HomeFragment()).commit();
                break;
            case ShopByCategory:
                fragmentManager.beginTransaction().replace(containNav , new NavCategoryFragment()).addToBackStack(null).commit();
                break;
            case Offers:
                break;
            case MyOrders:
                break;
            case MyWishlist:
                break;
            case MyNotifications:
                break;
            case FAQs:
                break;
            case HelpCenter:
                break;
            case Logout:
                break;
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.bn_home:
                    break;
                case R.id.bn_profile:
                    break;
            }
            return false;
        }
    };


   /* @Override
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
                *//*Intent intent = new Intent(getApplicationContext(), ActCart.class);
                intent.putExtra("source","dashboard");
                startActivity(intent);*//*
            }
        });

        return true;
    }*/


    @Override
    public void categoryClickLisener(Category category) {
        fragmentManager.beginTransaction().replace(appBarContainer , new ProductFragment(""))
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void productClickLisener(Category category) {
        Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
        startActivity(intent);
        /*fragmentManager.beginTransaction().replace(R.id.app_bar_container , new ProductDetailFragment())
                .addToBackStack(null)
                .commit();*/
    }

}
