package com.app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;
import com.app.callback.DrawerItemClickLisener;
import com.app.callback.HomePageListener;
import com.app.constant.AppConstant;
import com.app.controller.AppController;
import com.app.databinding.ActivityMainBinding;
import com.app.features.home.model.Brand;
import com.app.features.home.model.Product;
import com.app.features.home.model.SubCategory;
import com.app.features.login.LoginActivity;
import com.app.features.order.OrderActivity;
import com.app.features.navmenu.WishListActivity;
import com.app.features.notification.NotificationActivity;
import com.app.features.product.BrandFragment;
import com.app.features.productdetail.ProductDetailActivity;
import com.app.features.home.model.Category;
import com.app.features.home.HomeFragment;
import com.app.features.product.ProductFragment;
import com.app.features.profile.ProfileActivity;
import com.app.features.wallet.WalletActivity;
import com.app.util.AppUtils;
import com.app.util.PrefUtil;
import com.app.util.RestClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

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
        NavCategoryFragment.categories = new ArrayList<>();
        NavCategoryFragment.categorySubcat = new HashMap<>();
        getCategorySubCategory();

        containNav = R.id.container_nav;
        appBarContainer = R.id.app_bar_container;

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mView.included.bottomNav.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mView.drawer, mView.included.toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mView.drawer.setDrawerListener(toggle);
        toggle.syncState();

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(appBarContainer, new HomeFragment()).commit();
        fragmentManager.beginTransaction().replace(containNav , new NavigationViewFragment(),TAG_DRAWER_FRAGMENT).commit();

        ll_search = (LinearLayout)findViewById(R.id.ll_search);
        SearchView searchView= (SearchView) findViewById(R.id.searchView);

        // Get SearchView autocomplete object.
        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoComplete.setBackgroundColor(Color.WHITE);
        searchAutoComplete.setTextColor(Color.GRAY);
        searchAutoComplete.setTextSize(14);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.white);

        // Create a new ArrayAdapter and add data to search auto complete object.
        String dataArr[] = {"Apple" , "Amazon" , "Amd", "Microsoft", "Microwave", "MicroNews", "Intel", "Intelligence"};
        ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dataArr);
        searchAutoComplete.setAdapter(newsAdapter);

        // Listen to search view item on click event.
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                String queryString=(String)adapterView.getItemAtPosition(itemIndex);
                searchAutoComplete.setText(queryString);
                //Toast.makeText(MainActivity.this, "you clicked " + queryString, Toast.LENGTH_LONG).show();
            }
        });

        // Below event is triggered when submit search query.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setMessage("Search keyword is " + query);
                alertDialog.show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
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
                    if(AppConstant.isLogin(MainActivity.this)){
                        Intent intent1 = new Intent(MainActivity.this, OrderActivity.class);
                        startActivity(intent1);
                    }else{
                        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Please login to see my orders");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                intent.putExtra("type","login");
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    }
                    break;
                case R.id.bn_profile:
                    if(AppConstant.isLogin(MainActivity.this)){
                        Intent intent2 = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(intent2);
                    }else{
                        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Please login to see my profile");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                intent.putExtra("type","login");
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    }

                    break;

                case R.id.bbn_categories:
                    if(AppConstant.isLogin(MainActivity.this)){
                        Intent intent3 = new Intent(MainActivity.this, WalletActivity.class);
                        startActivity(intent3);
                    }else{
                        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Please login to see my wallet");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                intent.putExtra("type","login");
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    }

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
        intent.putExtra("productVarientId", product.getProductVarientId());
        startActivity(intent);
    }

    @Override
    public void updateCartCount(String cartCount) {
        setCartCount();
    }


    private void getCategorySubCategory() {

        try {
            JsonObject jsonObject = new JsonObject();
            if(AppConstant.isLogin(this)){
                jsonObject.addProperty("userId", AppUtils.getUserDetails(this).getLoginId());
            }else{
                jsonObject.addProperty("userId", "");
            }
            jsonObject.addProperty("tempUserId", AppController.getInstance().getUniqueID());

            new RestClient().getApiService().getCategories(jsonObject, new Callback<ModCategory>() {
                @Override
                public void success(ModCategory loginModel, Response response) {
                    if (loginModel.getSuccess().equals("1")) {
                        if (loginModel.getCategories().size() > 0) {
                            NavCategoryFragment.categories=loginModel.getCategories();
                            for(int i=0;i< NavCategoryFragment.categories.size();i++){
                                NavCategoryFragment.categorySubcat.put( NavCategoryFragment.categories.get(i),  NavCategoryFragment.categories.get(i).getSubcategory());
                            }
                        }

                    } else {
                        Toast.makeText(MainActivity.this, loginModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {

        }
    }

}
