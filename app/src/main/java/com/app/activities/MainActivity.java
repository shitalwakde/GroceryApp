package com.app.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;
import com.app.callback.DrawerItemClickLisener;
import com.app.callback.HomePageListener;
import com.app.constant.AppConstant;
import com.app.controller.AppController;
import com.app.databinding.ActivityMainBinding;
import com.app.features.address.BottomSheetLocationFragment;
import com.app.features.home.HomeFragment;
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
import com.app.features.product.ProductFragment;
import com.app.features.profile.ProfileActivity;
import com.app.features.refer.ReferFriendActivity;
import com.app.features.wallet.WalletActivity;
import com.app.util.AppUtils;
import com.app.util.PrefUtil;
import com.app.util.RestClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.schibstedspain.leku.LocationPickerActivity;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.provider.ContactsContract.CommonDataKinds.Email.ADDRESS;
import static com.schibstedspain.leku.LocationPickerActivityKt.LATITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.LOCATION_ADDRESS;
import static com.schibstedspain.leku.LocationPickerActivityKt.LONGITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.TIME_ZONE_DISPLAY_NAME;
import static com.schibstedspain.leku.LocationPickerActivityKt.TIME_ZONE_ID;
import static com.schibstedspain.leku.LocationPickerActivityKt.TRANSITION_BUNDLE;
import static com.schibstedspain.leku.LocationPickerActivityKt.ZIPCODE;


public class MainActivity extends BaseActivity implements DrawerItemClickLisener, HomePageListener, MultiplePermissionsListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG_DRAWER_FRAGMENT = "drawerFragment";
    public static TextView tv;
    private ActivityMainBinding mView;
    FragmentManager fragmentManager;
    public static int containNav, appBarContainer;
    public static LinearLayout ll_search;
    public static DrawerLayout drawerLayout;
    ArrayList<Category> category = new ArrayList<>();
    public static TextView tv_toolbaar, tv_location;
    public static ImageView iv_edit;
    RelativeLayout rl_location;
    public static final int MAP_BUTTON_REQUEST_CODE = 1;
    public static final int MAP_POIS_BUTTON_REQUEST_CODE = 2;
    BottomSheetLocationFragment bottomSheetLocationFragment;
    private static final int MY_PERMISSIONS = 65;
    public static double latitudePickUp = 0.0;
    public static double longitudePickUp = 0.0;
    LocationManager manager;
    Location location;
    LatLng currentLocation;
    FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mView = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mView.included.toolbar);
        NavCategoryFragment.categories = new ArrayList<>();
        NavCategoryFragment.categorySubcat = new HashMap<>();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(20 * 1000);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                //for (Location location : locationResult.getLocations()) {
                    if (locationResult.getLocations()!=null && locationResult.getLocations().size()>0) {
                        location=locationResult.getLocations().get(0);
                        latitudePickUp = location.getLatitude();
                        longitudePickUp = location.getLongitude();
                        editLocation(latitudePickUp, longitudePickUp);
                        fusedLocationClient.removeLocationUpdates(locationCallback);

                    }
               // }
            }
        };
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

        iv_edit = (ImageView)findViewById(R.id.iv_edit);
        tv_toolbaar = (TextView)findViewById(R.id.tv_toolbaar);
        tv_location = (TextView)findViewById(R.id.tv_location);
        rl_location = (RelativeLayout)findViewById(R.id.rl_location);
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

        rl_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               checkLocationPermission(MainActivity.this);
            }
        });
       // AppUtils.changeColor("#FF0000",mView.included.toolbar,null,null,ll_search);

    }


    public void setTitleData() {
        //iv_edit.setVisibility(View.VISIBLE);
        tv_location.setVisibility(View.VISIBLE);
        if(AppUtils.getAddress()!= null){
            tv_toolbaar.setText(AppUtils.getAddress());
            tv_toolbaar.setTextSize(11);
            getSupportActionBar().setTitle(AppUtils.getAddress());
        }else{
            tv_toolbaar.setText("Laxminagar, Nagpur - 440022");
            if(bottomSheetLocationFragment==null || !bottomSheetLocationFragment.isVisible()) {
                bottomSheetLocationFragment = new BottomSheetLocationFragment();
                bottomSheetLocationFragment.show(getSupportFragmentManager(), bottomSheetLocationFragment.getTag());
            }
        }

    }

    public void getLocation() {
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Log.w("TAG", "inside location != null");
                            latitudePickUp = location.getLatitude();
                            longitudePickUp = location.getLongitude();
                            editLocation(latitudePickUp, longitudePickUp);
                        } else {
//                            Log.w("TAG", "waiting for location");
 //                           Toast.makeText(MainActivity.this, "waiting for location", Toast.LENGTH_SHORT).show();
                            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

                        }
                    }
                });

    }


    private void editLocation(double latitudePickUp, double longitudePickUp){
        if(bottomSheetLocationFragment != null){
            bottomSheetLocationFragment.dismiss();
        }
        Intent locationPickerIntent = new  LocationPickerActivity.Builder()
                .withLocation(latitudePickUp, longitudePickUp)
                .withGeolocApiKey("AIzaSyCL43mx5EANHEYYPv71t-1SMFgqloqmSCs")
                .withSearchZone("in")
                //.withSearchZone(SearchZoneRect(LatLng(26.525467, -18.910366), LatLng(43.906271, 5.394197)))
                .withDefaultLocaleSearchZone()
                .shouldReturnOkOnBackPressed()
                .withStreetHidden()
                .withCityHidden()
                .withZipCodeHidden()
                .withSatelliteViewHidden()
                //.withGooglePlacesEnabled()
                .withGoogleTimeZoneEnabled()
                .withVoiceSearchHidden()
                .withUnnamedRoadHidden()
                .build(this);

        startActivityForResult(locationPickerIntent,
                MainActivity.MAP_BUTTON_REQUEST_CODE);

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
            case Wallet:
                Intent intent2 = new Intent(MainActivity.this, WalletActivity.class);
                startActivity(intent2);
                break;
            case ReferFriend:
                Intent intent4 = new Intent(MainActivity.this, ReferFriendActivity.class);
                startActivity(intent4);
                break;
            case Logout:
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setMessage("Are you sure want to Logout ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PrefUtil.getInstance(MainActivity.this).removeKeyData(AppConstant.PREF_USER_ID);
                        PrefUtil.getInstance(MainActivity.this).removeKeyData(AppConstant.PREF_USER_DATA);
                        PrefUtil.getInstance(MainActivity.this).removeKeyData(AppConstant.PREF_CART_COUNT);
                        PrefUtil.getInstance(MainActivity.this).removeKeyData(AppConstant.UUID);
                        BaseActivity.tv.setVisibility(View.GONE);
                        changeDrawerItems();
                        drawerLayout.closeDrawers();
                        invalidateOptionsMenu();
                        dialog.dismiss();
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
                        builder.setMessage("Please login to continue");
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
                        builder.setMessage("Please login to continue");
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
                        builder.setMessage("Please login to continue");
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
        if(product.getProductId() != null) {
            Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
            intent.putExtra("productId", product.getProductId());
            intent.putExtra("productVarientId", product.getProductVarientId());
            startActivity(intent);
        }
    }

    @Override
    public void updateCartCount(String cartCount) {
        setCartCount();

    }

    @Override
    public void productImageClickLisener(Product product) {

    }

    @Override
    public void productVieMoreClickLisener(Product product, String type) {

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            //bottomSheetLocationFragment.dismissAllowingStateLoss();
            Log.w("RESULT****", "OK - " + requestCode);
            if (requestCode == 1) {
                double latitude = data.getDoubleExtra(LATITUDE, 0.0);
                AppUtils.setLatitude(String.valueOf(latitude));
                Log.d("LATITUDE****", String.valueOf(latitude));
                double longitude = data.getDoubleExtra(LONGITUDE, 0.0);
                AppUtils.setLongitude(String.valueOf(longitude));
                Log.d("LONGITUDE****", String.valueOf(longitude));
                String address = data.getStringExtra(LOCATION_ADDRESS);
                AppUtils.setAddress(address);
                Log.d("ADDRESS****", address);
                String postalcode = data.getStringExtra(ZIPCODE);
                Log.d("POSTALCODE****", postalcode);
                Bundle bundle = data.getBundleExtra(TRANSITION_BUNDLE);
                //Log.d("BUNDLE TEXT****", bundle.getString("test"));
                List<Address> fullAddress = data.getParcelableExtra(ADDRESS);
                if (fullAddress != null) {
                    Log.d("FULL ADDRESS****", fullAddress.toString());
                }
                String timeZoneId = data.getStringExtra(TIME_ZONE_ID);
            //    Log.d("TIME ZONE ID****", timeZoneId);
                String timeZoneDisplayName = data.getStringExtra(TIME_ZONE_DISPLAY_NAME);
            //    Log.d("TIME ZONE NAME****", timeZoneDisplayName);
            } else if (requestCode == 2) {
                double latitude = data.getDoubleExtra(LATITUDE, 0.0);
                Log.d("LATITUDE****", String.valueOf(latitude));
                double longitude = data.getDoubleExtra(LONGITUDE, 0.0);
                Log.d("LONGITUDE****", String.valueOf(longitude));
                String address = data.getStringExtra(LOCATION_ADDRESS);
                Log.d("ADDRESS****", address);
//                LekuPoi lekuPoi = data.getParcelableExtra<LekuPoi>(LEKU_POI)
//                        Log.d("LekuPoi****", lekuPoi.toString())
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.d("RESULT****", "CANCELLED");
        }

        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                //editLocation();
                getLocation();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                if (Build.VERSION.SDK_INT >= 23) {
                    takePermissionsForMarsh();
                }
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setMessage("Products and offers are location specific, Please turn on your location to continue");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EnableGPSAutoMatically();
                        dialog.dismiss();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                //Write your code if there's no result
            }
        }
    }

    @Override
    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
        if( multiplePermissionsReport.areAllPermissionsGranted()) {
            EnableGPSAutoMatically();
        }else{
            //AppUtils.setAddress("Laxminagar, Nagpur - 440022");
        }
    }

    @Override
    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

    }

    private void EnableGPSAutoMatically() {
        GoogleApiClient googleApiClient = null;
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            //editLocation();
                            getLocation();
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling
                                // startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(MainActivity.this, 1000);

                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            toast("Setting change not allowed");
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }
    }


    public void takePermissionsForMarsh() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.w("TAG", "goToLogin from onRequestPermissionsResult");
                    Log.w("TAG", "GRANT RESULT " + grantResults.toString());

                } else {

                }
                return;
            }

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        toast("Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        toast("Failed");
    }
    private void toast(String message) {
        try {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            //log("Window has been closed");
        }
    }
}
