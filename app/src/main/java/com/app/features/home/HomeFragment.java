package com.app.features.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;
import com.app.activities.MainActivity;
import com.app.activities.ModCategory;
import com.app.activities.NavCategoryFragment;
import com.app.activities.SplashScreenActivity;
import com.app.callback.BrandLisener;
import com.app.callback.CategoryListener;
import com.app.callback.HomeClickLisener;
import com.app.callback.ProductListener;
import com.app.constant.AppConstant;
import com.app.features.home.adapter.Banner;
import com.app.features.home.adapter.BeseSellingAdapter;
import com.app.features.home.adapter.BrandAdapter;
import com.app.features.home.adapter.CategoryAdapter;
import com.app.features.home.model.Brand;
import com.app.features.home.model.Category;
import com.app.features.home.model.HomeModel;
import com.app.features.home.model.Product;
import com.app.features.home.model.SubCategory;
import com.app.features.product.ProductFragment;
import com.app.util.RestClient;
import com.asura.library.posters.Poster;
import com.asura.library.posters.RemoteImage;
import com.asura.library.views.PosterSlider;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.app.activities.MainActivity.appBarContainer;
import static com.app.activities.MainActivity.ll_search;

public class HomeFragment extends Fragment {

    View rootView;
    PosterSlider poster_slider;
    List<Banner> bannerList;
    List<Category> categoryList;
    List<Product> bestSellingList;
    List<Brand> brandList;
    String immm="", viewAllType="";
    DefaultSliderView textSliderView;
    SliderLayout imageSlider;
    TextView tv_view_recently_product, tv_view_best, tv_view_category, tv_view_brand;
    RecyclerView rv_category, rv_top_details, rv_health, rv_brand;
    CategoryListener catLisener;
    BrandLisener brandLisener;
    ProductListener productLisener;
    FragmentManager fragmentManager;
    private Category category;
    List<SubCategory> subCatList;

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

        ll_search.setVisibility(View.VISIBLE);

        return rootView;
    }

    private void init(View rootView){
        fragmentManager = getActivity().getSupportFragmentManager();
        subCatList = new ArrayList<>();
        bannerList=new ArrayList<>();
        categoryList=new ArrayList<>();
        bestSellingList=new ArrayList<>();
        brandList=new ArrayList<>();
        poster_slider = (PosterSlider)rootView.findViewById(R.id.poster_slider);
        imageSlider=(SliderLayout)rootView.findViewById(R.id.fh_slider);
        rv_category = (RecyclerView)rootView.findViewById(R.id.rv_category);
        rv_top_details = (RecyclerView)rootView.findViewById(R.id.rv_top_details);
        rv_health = (RecyclerView)rootView.findViewById(R.id.rv_health);
        rv_brand = (RecyclerView)rootView.findViewById(R.id.rv_brand);
        tv_view_recently_product = (TextView)rootView.findViewById(R.id.tv_view_recently_product);
        tv_view_best = (TextView)rootView.findViewById(R.id.tv_view_best);
        tv_view_category = (TextView)rootView.findViewById(R.id.tv_view_category);
        tv_view_brand = (TextView)rootView.findViewById(R.id.tv_view_brand);
    }


    private void click(){
        getHomeData();
        tv_view_recently_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(appBarContainer, new ProductFragment("", "", "", "")).addToBackStack(null).commit();
            }
        });

        tv_view_best.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(appBarContainer, new ProductFragment("", "", "", "")).addToBackStack(null).commit();
            }
        });

        tv_view_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(appBarContainer, new ProductFragment("category", "", "", "")).addToBackStack(null).commit();
            }
        });

        tv_view_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(appBarContainer, new ProductFragment("brand", "", "", "")).addToBackStack(null).commit();
            }
        });
    }


    private void getHomeData(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("tempUserId", "95445566");
        jsonObject.addProperty("userId", "");


        new RestClient().getApiService().home(jsonObject, new Callback<HomeModel>() {
            @Override
            public void success(HomeModel modCategory, Response response) {

                if(modCategory.getSuccess().equals("1")){
                    manageDetails(modCategory);
                }else{
                    Toast.makeText(getActivity(), modCategory.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void manageDetails(HomeModel modCategory) {
        arrangeCategoryAdpt(modCategory.getCategory());
        arrangeBestSellingAdpt(modCategory.getProduct());
        arrangeBrands(modCategory.getBrand());
        arrangeBanner(modCategory);

        ((MainActivity)getContext()).setCartCount(Integer.parseInt(modCategory.getCount_cart()));

    }

    private void arrangeBestSellingAdpt(ArrayList<Product> product) {
        BeseSellingAdapter adapter1 = new BeseSellingAdapter(productLisener, product);
        rv_top_details.setAdapter(adapter1);
        rv_health.setAdapter(adapter1);
    }

    private void arrangeCategoryAdpt(ArrayList<Category> category) {
        CategoryAdapter adapter = new CategoryAdapter(getActivity(),catLisener, NavCategoryFragment.categories,
                AppConstant.FROM_HOME_CATEGORY_PRODUCT);
        rv_category.setAdapter(adapter);
    }

    private void arrangeBrands(ArrayList<Brand> brand) {
        BrandAdapter adapter=new BrandAdapter(getContext(),brandLisener,brand);
        rv_brand.setAdapter(adapter);
    }

    private void arrangeBanner(HomeModel modCategory) {
        ArrayList<Poster> posters = new ArrayList<>();
        for (int i = 0; i < modCategory.getBanner().size(); i++) {
            Banner img = modCategory.getBanner().get(i);
            immm = img.getImage();
                posters.add(new RemoteImage(immm));
        }
        poster_slider.setPosters(posters);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof MainActivity){
            brandLisener= (BrandLisener) context;
            catLisener= (CategoryListener) context;
            productLisener= (ProductListener) context;
        }
    }

}
