package com.app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.R;
import com.app.callback.BrandLisener;
import com.app.callback.CategoryListener;
import com.app.callback.HomeClickLisener;
import com.app.callback.DrawerItemClickLisener;
import com.app.callback.HomePageListener;
import com.app.constant.AppConstant;
import com.app.databinding.ActivityMainBinding;
import com.app.features.home.model.Brand;
import com.app.features.home.model.Product;
import com.app.features.home.model.SubCategory;
import com.app.features.navmenu.OrderActivity;
import com.app.features.navmenu.WishListActivity;
import com.app.features.notification.NotificationActivity;
import com.app.features.product.BrandFragment;
import com.app.features.productdetail.ProductDetailActivity;
import com.app.features.home.model.Category;
import com.app.features.home.HomeFragment;
import com.app.features.product.ProductFragment;
import com.app.features.profile.ProfileActivity;
import com.app.features.wallet.WalletActivity;
import com.app.util.PrefUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends BaseActivity implements DrawerItemClickLisener, HomePageListener {
    private static final String TAG_DRAWER_FRAGMENT = "drawerFragment";
    public static TextView tv;
    private ActivityMainBinding mView;
    FragmentManager fragmentManager;
    public static int containNav, appBarContainer;
    public static LinearLayout ll_search;
    public static DrawerLayout drawerLayout;
    ArrayList<Category> category = new ArrayList<>();

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
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        mView.included.bottomNav.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mView.drawer, mView.included.toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mView.drawer.setDrawerListener(toggle);
        toggle.syncState();

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(appBarContainer, new HomeFragment()).commit();
        fragmentManager.beginTransaction().replace(containNav , new NavigationViewFragment(),TAG_DRAWER_FRAGMENT).commit();
    }

    @Override
    public void drawerItemClickLisener(@NonNull NavMenu navMenu) {
        switch (navMenu.getMenu()){
            case Home:
                if (mView.drawer.isDrawerOpen(GravityCompat.START)) {
                    mView.drawer.closeDrawer(GravityCompat.START);
                }
                category = NavCategoryFragment.categories;
                Fragment fragment=new HomeFragment();
                Bundle bundle=new Bundle();
                bundle.putSerializable(AppConstant.EXTRA_PROD_CATEGORY, (Serializable) category);
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(appBarContainer , fragment)
                        .commit();
                //fragmentManager.beginTransaction().replace(appBarContainer, new HomeFragment()).commit();
                break;
            case ShopByCategory:
                fragmentManager.beginTransaction().replace(containNav , new NavCategoryFragment()).addToBackStack(null).commit();
                break;
            case Offers:
                break;
            case MyOrders:
                if (mView.drawer.isDrawerOpen(GravityCompat.START)) {
                    mView.drawer.closeDrawer(GravityCompat.START);
                }
                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(intent);
                break;
            case MyWishlist:
                if (mView.drawer.isDrawerOpen(GravityCompat.START)) {
                    mView.drawer.closeDrawer(GravityCompat.START);
                }
                Intent intent1 = new Intent(MainActivity.this, WishListActivity.class);
                startActivity(intent1);
                break;
            case MyNotifications:
                Intent intent3 = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent3);
                break;
            case FAQs:
                break;
            case HelpCenter:
                Intent intent2 = new Intent(MainActivity.this, WalletActivity.class);
                startActivity(intent2);
                break;
            case Logout:
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setMessage("Are you sure want to Logout ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PrefUtil.getInstance(MainActivity.this).removeKeyData(AppConstant.PREF_USER_ID);
                        PrefUtil.getInstance(MainActivity.this).removeKeyData(AppConstant.PREF_USER_DATA);
                        changeDrawerItems();
                        drawerLayout.closeDrawers();
                        invalidateOptionsMenu();
                        dialog.cancel();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                break;
        }
    }

    private void changeDrawerItems() {
        Fragment fragment=fragmentManager.findFragmentByTag(TAG_DRAWER_FRAGMENT);
        if(fragment instanceof NavigationViewFragment){
            NavigationViewFragment navigationViewFragment= (NavigationViewFragment) fragment;
            navigationViewFragment.changeMenus();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.bn_home:
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.bbn_search:
                    Intent intent1 = new Intent(MainActivity.this, OrderActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.bn_profile:
                    Intent intent2 = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent2);
                    break;
            }
            return false;
        }
    };


    @Override
    public void categoryClickLisener(Category category) {
        Fragment fragment=new ProductFragment("", category.getCategoryId(), "", "");
        Bundle bundle=new Bundle();
        bundle.putSerializable(AppConstant.EXTRA_PROD_CATEGORY, (Serializable) category);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(appBarContainer , fragment)
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

    @Override
    public void orderClickLisener(Category category) {

    }

    @Override
    public void subcategoryClickLisener(Category category,SubCategory subCategories) {
        Fragment fragment=new ProductFragment("", subCategories.getCategoryId(), subCategories.getSubCategoryId(), subCategories.getName());
        Bundle bundle=new Bundle();
        bundle.putSerializable(AppConstant.EXTRA_PROD_CATEGORY, (Serializable) category);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(appBarContainer ,fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void brandLisener(Brand brand) {
        fragmentManager.beginTransaction().replace(appBarContainer, new BrandFragment(brand.getBrandId())).addToBackStack(null).commit();
    }

    @Override
    public void productClickLisener(Product product) {
        Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
        intent.putExtra("productId", product.getProductId());
        startActivity(intent);
    }

    @Override
    public void updateCartCount(String cartCount) {
        setCartCount();
    }
}
