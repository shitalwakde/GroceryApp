package com.app.features.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;
import com.app.activities.BaseActivity;
import com.app.activities.MainActivity;
import com.app.activities.NavCategoryFragment;
import com.app.callback.BrandLisener;
import com.app.callback.CategoryListener;
import com.app.callback.ProductListener;
import com.app.callback.SearchLisener;
import com.app.constant.AppConstant;
import com.app.controller.AppController;
import com.app.controller.CheckNetwork;
import com.app.features.home.adapter.Banner;
import com.app.features.home.adapter.BrandAdapter;
import com.app.features.home.adapter.CategoryAdapter;
import com.app.features.home.adapter.HealthAdapter;
import com.app.features.home.model.Brand;
import com.app.features.home.model.Category;
import com.app.features.home.model.HomeModel;
import com.app.features.home.model.Product;
import com.app.features.home.model.SubCategory;
import com.app.features.product.adapter.ViewAllFragment;
import com.app.features.search.SearchModel;
import com.app.util.AppUtils;
import com.app.util.PrefUtil;
import com.app.util.RestClient;
import com.asura.library.posters.Poster;
import com.asura.library.views.PosterSlider;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.app.activities.MainActivity.appBarContainer;
import static com.app.activities.MainActivity.ll_search;

public class HomeFragment extends Fragment implements SearchLisener, ProductListener {

    View rootView;
    PosterSlider poster_slider;
    ArrayList<Poster> posters;
    List<Banner> bannerList;
    List<Category> categoryList;
    List<Product> bestSellingList;
    List<Product> recentlyViewList;
    List<Brand> brandList;
    String immm = "", viewAllType = "";
    DefaultSliderView textSliderView;
    SliderLayout AdimageSlider;
    SliderLayout bannerslider;
    TextView tv_view_recently_product, tv_view_best, tv_view_category, tv_view_brand, tv_view_past;
    RecyclerView rv_category, rvRecently, rvBest, rv_brand, rvPast;
    CategoryListener catLisener;
    BrandLisener brandLisener;
    ProductListener productLisener;
    FragmentManager fragmentManager;
    List<SubCategory> subCatList;
    ProgressBar progressBar;
    ArrayList<SearchModel> searchKeyList;
    NestedScrollView nestedScrollView;
    RelativeLayout rl_network;
    ImageView iv_offer, iv_discount;
    LinearLayout ll_brands, ll_bestSelling, ll_recently, ll_pastOrder, ll_categories;

    boolean isInternetPresent = false;
    public ImageView noNetworkConnectionImageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home_fragment, container, false);
        init(rootView);
        click();

        return rootView;
    }

    private void init(View rootView) {
        fragmentManager = getActivity().getSupportFragmentManager();
        posters = new ArrayList<>();
        subCatList = new ArrayList<>();
        bannerList = new ArrayList<>();
        categoryList = new ArrayList<>();
        bestSellingList = new ArrayList<>();
        recentlyViewList = new ArrayList<>();
        brandList = new ArrayList<>();
        searchKeyList = new ArrayList<>();
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        poster_slider = (PosterSlider) rootView.findViewById(R.id.poster_slider);
        AdimageSlider = (SliderLayout) rootView.findViewById(R.id.fh_slider);
        bannerslider = (SliderLayout) rootView.findViewById(R.id.ad_slider);
        rv_category = (RecyclerView) rootView.findViewById(R.id.rv_category);
        rvRecently = (RecyclerView) rootView.findViewById(R.id.rv_top_details);
        rvBest = (RecyclerView) rootView.findViewById(R.id.rv_health);
        rv_brand = (RecyclerView) rootView.findViewById(R.id.rv_brand);
        rvPast = (RecyclerView) rootView.findViewById(R.id.rv_past);
        tv_view_recently_product = (TextView) rootView.findViewById(R.id.tv_view_recently_product);
        tv_view_best = (TextView) rootView.findViewById(R.id.tv_view_best);
        tv_view_category = (TextView) rootView.findViewById(R.id.tv_view_category);
        tv_view_brand = (TextView) rootView.findViewById(R.id.tv_view_brand);
        tv_view_past = (TextView) rootView.findViewById(R.id.tv_view_past);
        iv_offer = (ImageView)rootView.findViewById(R.id.iv_offer);
        iv_discount = (ImageView)rootView.findViewById(R.id.iv_discount);
        ll_brands = (LinearLayout)rootView.findViewById(R.id.ll_brands);
        ll_bestSelling = (LinearLayout)rootView.findViewById(R.id.ll_bestSelling);
        ll_recently = (LinearLayout)rootView.findViewById(R.id.ll_recently);
        ll_pastOrder = (LinearLayout)rootView.findViewById(R.id.ll_pastOrder);
        ll_categories = (LinearLayout)rootView.findViewById(R.id.ll_categories);

        // Initialization the UI Components
        noNetworkConnectionImageView = (ImageView) rootView.findViewById(R.id.nonetworkconnection);
        nestedScrollView = (NestedScrollView) rootView.findViewById(R.id.nestedScrollView);
        rl_network = (RelativeLayout) rootView.findViewById(R.id.rl_network);
    }

    @Override
    public void onResume() {
        super.onResume();
        getHomeData();
        ((MainActivity)getActivity()).setTitleData();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(networkReceiver, filter);

        nestedScrollView.fullScroll(View.FOCUS_UP);
        nestedScrollView.smoothScrollTo(0,0);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(networkReceiver);
    }

    /*
     * Method for Receiving the Network State
     */
    private BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent bufferIntent) {
            String status = CheckNetwork.getConnectivityStatusString(context);
            if (status.equals("WIFI") || status.equals("MOBILE")) {
                isInternetPresent = true;
            } else if (status.equals("No Connection")) {
                isInternetPresent = false;
            }
            showNetworkState();
        }
    };

    /*
     * Method for display the respective Image depending on the Network State
     */
    public void showNetworkState() {
        if (isInternetPresent) {
            nestedScrollView.setVisibility(View.VISIBLE);
            rl_network.setVisibility(View.GONE);
            noNetworkConnectionImageView.setVisibility(View.GONE);
        } else {
            nestedScrollView.setVisibility(View.GONE);
            rl_network.setVisibility(View.VISIBLE);
            noNetworkConnectionImageView.setVisibility(View.VISIBLE);
        }
    }

    private void click() {
        //getHomeData();
        ll_search.setVisibility(View.VISIBLE);

    }


    private void getHomeData() {
        progressBar.setVisibility(View.VISIBLE);
        noNetworkConnectionImageView.setVisibility(View.GONE);
        rl_network.setVisibility(View.VISIBLE);
        nestedScrollView.setVisibility(View.GONE);
        JsonObject jsonObject = new JsonObject();
        if (AppConstant.isLogin(getContext())) {
            jsonObject.addProperty("userId", AppUtils.getUserDetails(getContext()).getLoginId());
        } else {
            jsonObject.addProperty("userId", "");
        }
        jsonObject.addProperty("tempUserId", AppController.getInstance().getUniqueID());

        new RestClient().getApiService().home(jsonObject, new Callback<HomeModel>() {
            @Override
            public void success(HomeModel modCategory, Response response) {
                progressBar.setVisibility(View.GONE);
                if (modCategory.getSuccess().equals("1")) {
                    rl_network.setVisibility(View.GONE);
                    noNetworkConnectionImageView.setVisibility(View.GONE);
                    nestedScrollView.setVisibility(View.VISIBLE);
                    manageDetails(modCategory);
                    categoryList = modCategory.getCategory();
                    brandList = modCategory.getBrand();
                    bestSellingList = modCategory.getProduct();
                    recentlyViewList = modCategory.getProduct();
                } else {
                    Toast.makeText(getActivity(), modCategory.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                progressBar.setVisibility(View.GONE);
                noNetworkConnectionImageView.setVisibility(View.GONE);
                rl_network.setVisibility(View.VISIBLE);
                nestedScrollView.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void manageDetails(HomeModel modCategory) {
        arrangeCategoryAdpt(modCategory.getCategory());
        arrangePastOrderAdpt(modCategory.getProduct());
        arrangeRecentlyViewAdpt(modCategory.getRecentViewProduct());
        arrangeBestSellingAdpt(modCategory.getBestSellingProduct());
        arrangeBrands(modCategory.getBrand());
        arrangeBanner(modCategory);
        arrangeAdvertise(modCategory.getAdvertisement());
        if (modCategory.getCount_cart() != null) {
            PrefUtil.getInstance(getContext()).putData(AppConstant.PREF_CART_COUNT, modCategory.getCount_cart());
            AppUtils.setCartCount(modCategory.getCount_cart());
            if (getContext() != null) {
                ((MainActivity) getContext()).setCartCount();
            }
        }
        if(modCategory.getOfferImage().equals("")){
            iv_offer.setVisibility(View.GONE);
        }else{
            iv_offer.setVisibility(View.VISIBLE);
            Picasso.with(getActivity()).load(modCategory.getOfferImage()).into(iv_offer);
        }
        if(modCategory.getDicountImage().equals("")){
            iv_discount.setVisibility(View.GONE);
        }else{
            iv_discount.setVisibility(View.VISIBLE);
            Picasso.with(getActivity()).load(modCategory.getDicountImage()).into(iv_discount);
        }

    }

    private void arrangeAdvertise(ArrayList<Banner> advertisement) {
        bannerslider.removeAllSliders();
        if(advertisement.size() == 0){
            bannerslider.setVisibility(View.GONE);
        }else{
            bannerslider.setVisibility(View.VISIBLE);
            for (int i = 0; i < advertisement.size(); i++) {
                String imgAdPath=advertisement.get(i).getImage();
                loadImageAdInSlider(null,imgAdPath);
            }
        }

    }

    private void loadImageAdInSlider(Object o, String imgAdPath) {
        BaseSliderView textSliderView = new DefaultSliderView(getContext());
        textSliderView
                .description("")
                //.image(image)
                .image(imgAdPath)
                .error(R.drawable.logo)
                .empty(R.drawable.background_login)
                .setScaleType(BaseSliderView.ScaleType.Fit)
                .setOnImageLoadListener(new BaseSliderView.ImageLoadListener() {
                    @Override
                    public void onStart(BaseSliderView target) {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onEnd(boolean result, BaseSliderView target) {
                        progressBar.setVisibility(View.GONE);
                        //if(result)
                    }
                });
        textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {
            }
        });
        bannerslider.addSlider(textSliderView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void arrangePastOrderAdpt(ArrayList<Product> product) {
        if(product.size() == 0){
            ll_pastOrder.setVisibility(View.GONE);
        }else{
            ll_pastOrder.setVisibility(View.VISIBLE);
            HealthAdapter adapter1 = new HealthAdapter(productLisener, product, this, "pastProduct");
            rvPast.setAdapter(adapter1);
        }

    }

    private void arrangeRecentlyViewAdpt(ArrayList<Product> recentViewProduct) {
        if(recentViewProduct.size() == 0){
            ll_recently.setVisibility(View.GONE);
        }else{
            ll_recently.setVisibility(View.VISIBLE);
            HealthAdapter adapter1 = new HealthAdapter(productLisener, recentViewProduct, this, "recentProduct");
            rvRecently.setAdapter(adapter1);
        }

    }

    private void arrangeBestSellingAdpt(ArrayList<Product> BestSellingProduct) {
        if(BestSellingProduct.size() == 0){
            ll_bestSelling.setVisibility(View.GONE);
        }else {
            ll_bestSelling.setVisibility(View.VISIBLE);
            HealthAdapter adapter1 = new HealthAdapter(productLisener, BestSellingProduct, this, "bestSellingProduct");
            rvBest.setAdapter(adapter1);
        }

    }

    private void arrangeCategoryAdpt(ArrayList<Category> category) {
        if(NavCategoryFragment.categories.size() == 0){
            ll_categories.setVisibility(View.GONE);
        }else{
            ll_categories.setVisibility(View.VISIBLE);
            if (NavCategoryFragment.categories != null) {
                CategoryAdapter adapter = new CategoryAdapter(catLisener, NavCategoryFragment.categories,
                        AppConstant.FROM_HOME_CATEGORY_PRODUCT);
                rv_category.setAdapter(adapter);
            }
        }

    }

    private void arrangeBrands(ArrayList<Brand> brand) {
        if(brand.size() == 0){
            ll_brands.setVisibility(View.GONE);
        }else {
            ll_brands.setVisibility(View.VISIBLE);
            BrandAdapter adapter = new BrandAdapter(brandLisener, brand);
            rv_brand.setAdapter(adapter);
        }
    }


    private void arrangeBanner(HomeModel modCategory) {
        AdimageSlider.removeAllSliders();
        if(modCategory.getBanner().size() == 0){
            AdimageSlider.setVisibility(View.GONE);
        }else{
            AdimageSlider.setVisibility(View.VISIBLE);
            for (int i = 0; i < modCategory.getBanner().size(); i++) {
                String imgPath=modCategory.getBanner().get(i).getImage();
                loadImageInSlider(null,imgPath);
            }
            AdimageSlider.setPresetTransformer(SliderLayout.Transformer.Default);
            AdimageSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            AdimageSlider.setCustomAnimation(new DescriptionAnimation());
            AdimageSlider.setDuration(3000);
            AdimageSlider.startAutoCycle();
        }


    }

    private void loadImageInSlider(File image,String url) {
        BaseSliderView textSliderView = new DefaultSliderView(getContext());
        textSliderView
                .description("")
                //.image(image)
                .image(url)
                .error(R.drawable.logo)
                .empty(R.drawable.background_login)
                .setScaleType(BaseSliderView.ScaleType.Fit)
                .setOnImageLoadListener(new BaseSliderView.ImageLoadListener() {
                    @Override
                    public void onStart(BaseSliderView target) {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onEnd(boolean result, BaseSliderView target) {
                        progressBar.setVisibility(View.GONE);
                        //if(result)
                    }
                });
        textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {
                    }
                });
        AdimageSlider.addSlider(textSliderView);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof MainActivity){
            brandLisener = (BrandLisener) context;
            catLisener = (CategoryListener) context;
            productLisener = (ProductListener) context;
        }
    }

    @Override
    public void SearchLisener(String key) {
    }

    @Override
    public void productClickLisener(Product product) {

    }

    @Override
    public void updateCartCount(String cartCount) {

    }

    @Override
    public void productImageClickLisener(Product product) {

    }

    @Override
    public void productVieMoreClickLisener(Product product, String type) {
        Fragment fragment = new ViewAllFragment();
        Bundle bundle = new Bundle();
        //bundle.putSerializable("List", (Serializable) recentlyViewList);
        bundle.putString("type", type);
        if(product.getProductId() != null) {
            bundle.putString("productId", product.getProductId());
        }
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(appBarContainer, fragment)
                .addToBackStack(null)
                .commit();
    }
}
