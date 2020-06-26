package com.app.features.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.R;
import com.app.activities.MainActivity;
import com.app.callback.HomeClickLisener;
import com.app.features.product.ProductFragment;
import com.asura.library.views.PosterSlider;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.app.activities.MainActivity.appBarContainer;
import static com.app.activities.MainActivity.ll_search;

public class HomeFragment extends Fragment {

    View rootView;
    PosterSlider poster_slider;
    List<Integer> bannerList;
    List<Category> categoryList;
    List<Category> bestSellingList;
    List<Category> brandList;
    String immm="", viewAllType="";
    DefaultSliderView textSliderView;
    SliderLayout imageSlider;
    TextView tv_view_recently_product, tv_view_best, tv_view_category, tv_view_brand;
    RecyclerView rv_category, rv_top_details, rv_health, rv_brand;
    HomeClickLisener lisener;
    FragmentManager fragmentManager;

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
        bannerList.add(R.drawable.banner2);
        bannerList.add(R.drawable.banner2);
        bannerList.add(R.drawable.banner2);
        bannerList.add(R.drawable.banner2);

        for (int i = 0; i < bannerList.size(); i++) {
            textSliderView = new DefaultSliderView(getContext());
            textSliderView
                    .description("")
                    .image(bannerList.get(i))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", "");
            imageSlider.addSlider(textSliderView);
        }
        imageSlider.startAutoCycle();

        Category cat = new Category();
        cat.setIv_category(R.drawable.grapes);
        cat.setTv_category_name("Fruits & Vegetables");

        Category cat1 = new Category();
        cat1.setIv_category(R.drawable.flower);
        cat1.setTv_category_name("Fruits & Vegetables");

        categoryList.add(cat);
        categoryList.add(cat1);
        categoryList.add(cat1);
        categoryList.add(cat);
        categoryList.add(cat);
        categoryList.add(cat1);

        CategoryAdapter adapter = new CategoryAdapter(lisener,categoryList);
        rv_category.setAdapter(adapter);

        Category cate = new Category();
        cate.setIv_best(R.drawable.aata);
        cate.setTv_pr_name("Fortune");
        cate.setTv_pr_sub_name("Sunlife");

        Category cate1 = new Category();
        cate1.setIv_best(R.drawable.soup);
        cate1.setTv_pr_name("Soup");
        cate1.setTv_pr_sub_name("Manchow Veg");

        bestSellingList.add(cate);
        bestSellingList.add(cate1);
        bestSellingList.add(cate1);
        bestSellingList.add(cate);

        BeseSellingAdapter adapter1 = new BeseSellingAdapter(lisener, bestSellingList);
        rv_top_details.setAdapter(adapter1);
        rv_health.setAdapter(adapter1);

        /*HealthAdapter adapter2 = new HealthAdapter(bestSellingList);
        rv_health.setAdapter(adapter2);*/

        Category catBrand = new Category();
        catBrand.setIv_brand(R.drawable.dettol);

        Category catBrand1 = new Category();
        catBrand1.setIv_brand(R.drawable.amul);

        brandList.add(catBrand);
        brandList.add(catBrand1);
        brandList.add(catBrand);
        brandList.add(catBrand1);
        brandList.add(catBrand);
        brandList.add(catBrand1);

        BrandAdapter adapter3 = new BrandAdapter(lisener, brandList);
        rv_brand.setAdapter(adapter3);

        return rootView;
    }

    private void init(View rootView){
        fragmentManager = getActivity().getSupportFragmentManager();
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

        tv_view_recently_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(appBarContainer, new ProductFragment("recently_product")).addToBackStack(null).commit();
            }
        });

        tv_view_best.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(appBarContainer, new ProductFragment("best_product")).addToBackStack(null).commit();
            }
        });

        tv_view_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(appBarContainer, new ProductFragment("category")).addToBackStack(null).commit();
            }
        });

        tv_view_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(appBarContainer, new ProductFragment("brand")).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof MainActivity){
            lisener = (HomeClickLisener) context;
        }
    }

}
