package com.app.features.home;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;
import com.app.activities.MainActivity;
import com.app.activities.NavCategoryFragment;
import com.app.callback.BrandLisener;
import com.app.callback.CategoryListener;
import com.app.callback.ProductListener;
import com.app.callback.SearchLisener;
import com.app.constant.AppConstant;
import com.app.controller.AppController;
import com.app.features.home.adapter.Banner;
import com.app.features.home.adapter.BestSellingAdapter;
import com.app.features.home.adapter.BrandAdapter;
import com.app.features.home.adapter.CategoryAdapter;
import com.app.features.home.model.Brand;
import com.app.features.home.model.Category;
import com.app.features.home.model.HomeModel;
import com.app.features.home.model.Product;
import com.app.features.home.model.SubCategory;
import com.app.features.product.adapter.ViewAllFragment;
import com.app.features.search.SearchAdapter;
import com.app.features.search.SearchModel;
import com.app.util.AppUtils;
import com.app.util.PrefUtil;
import com.app.util.RestClient;
import com.asura.library.posters.Poster;
import com.asura.library.views.PosterSlider;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.gson.JsonObject;

import java.io.Serializable;
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

public class HomeFragment extends Fragment implements SearchLisener {

    View rootView;
    PosterSlider poster_slider;
    ArrayList <Poster> posters;
    List<Banner> bannerList;
    List<Category> categoryList;
    List<Product> bestSellingList;
    List<Product> recentlyViewList;
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
    List<SubCategory> subCatList;
    ProgressBar progressBar;
    RecyclerView rv_recyclerDialog;
    ArrayList<SearchModel> searchKeyList;

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

    private void init(View rootView){
        fragmentManager = getActivity().getSupportFragmentManager();
        posters=new ArrayList<>();
        subCatList = new ArrayList<>();
        bannerList=new ArrayList<>();
        categoryList=new ArrayList<>();
        bestSellingList=new ArrayList<>();
        recentlyViewList = new ArrayList<>();
        brandList=new ArrayList<>();
        searchKeyList = new ArrayList<>();
        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
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

    @Override
    public void onResume() {
        super.onResume();
        getHomeData();
    }

    private void click(){
        //getHomeData();
        ll_search.setVisibility(View.VISIBLE);

        ll_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getSearchkey();
            }
        });

        tv_view_recently_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new ViewAllFragment();
                Bundle bundle=new Bundle();
                bundle.putSerializable("List", (Serializable) recentlyViewList);
                bundle.putString("type", "recently");
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(appBarContainer ,fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        tv_view_best.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new ViewAllFragment();
                Bundle bundle=new Bundle();
                bundle.putSerializable("List", (Serializable) bestSellingList);
                bundle.putString("type", "bestselling");
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(appBarContainer ,fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        tv_view_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new ViewAllFragment();
                Bundle bundle=new Bundle();
                bundle.putSerializable("List", new ArrayList<>());
                bundle.putString("type", "category");
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(appBarContainer ,fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        tv_view_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new ViewAllFragment();
                Bundle bundle=new Bundle();
                bundle.putSerializable("List", (Serializable) brandList);
                bundle.putString("type", "brand");
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(appBarContainer ,fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void getSearchkey(){
        showDialog(searchKeyList);
    }

    private void showDialog(ArrayList<SearchModel> searchKeyList){
        final Dialog dialog1 = new Dialog(getActivity());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(true);
        dialog1.setContentView(R.layout.dlg_search);
        EditText et_search = (EditText)dialog1.findViewById(R.id.et_search);
        rv_recyclerDialog = (RecyclerView)dialog1.findViewById(R.id.rv_recyclerDialog);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setDialogAdapter(dialog1);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dialog1.show();
    }

    private void setDialogAdapter(Dialog dialog1){
        SearchAdapter adapter = new SearchAdapter(searchKeyList, dialog1, this);
        rv_recyclerDialog.setAdapter(adapter);
    }

    private void getHomeData(){
        progressBar.setVisibility(View.VISIBLE);
        JsonObject jsonObject = new JsonObject();
        if(AppConstant.isLogin(getContext())){
            jsonObject.addProperty("userId", AppUtils.getUserDetails(getContext()).getLoginId());
        }else{
            jsonObject.addProperty("userId", "");
        }
        //jsonObject.addProperty("tempUserId", "1593500024");
        jsonObject.addProperty("tempUserId", AppController.getInstance().getUniqueID());

        new RestClient().getApiService().home(jsonObject, new Callback<HomeModel>() {
            @Override
            public void success(HomeModel modCategory, Response response) {
                progressBar.setVisibility(View.GONE);
                if(modCategory.getSuccess().equals("1")){
                    manageDetails(modCategory);
                    categoryList = modCategory.getCategory();
                    brandList = modCategory.getBrand();
                    bestSellingList = modCategory.getProduct();
                    recentlyViewList = modCategory.getProduct();
                }else{
                    Toast.makeText(getActivity(), modCategory.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void manageDetails(HomeModel modCategory) {
        arrangeCategoryAdpt(modCategory.getCategory());
        arrangeBestSellingAdpt(modCategory.getProduct());
        arrangeBrands(modCategory.getBrand());
        arrangeBanner(modCategory);
        if(modCategory.getCount_cart()!= null) {
            PrefUtil.getInstance(getContext()).putData(AppConstant.PREF_CART_COUNT, modCategory.getCount_cart());
            AppUtils.setCartCount(modCategory.getCount_cart());
            if(getContext()!=null)
                ((MainActivity)getContext()).setCartCount();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void arrangeBestSellingAdpt(ArrayList<Product> product) {
        BestSellingAdapter adapter1 = new BestSellingAdapter(productLisener, product);
        rv_top_details.setAdapter(adapter1);
        rv_health.setAdapter(adapter1);
    }

    private void arrangeCategoryAdpt(ArrayList<Category> category) {
        if(NavCategoryFragment.categories != null) {
            CategoryAdapter adapter = new CategoryAdapter(catLisener, NavCategoryFragment.categories,
                    AppConstant.FROM_HOME_CATEGORY_PRODUCT);
            rv_category.setAdapter(adapter);
        }
    }

    private void arrangeBrands(ArrayList<Brand> brand) {
        BrandAdapter adapter=new BrandAdapter(brandLisener,brand);
        rv_brand.setAdapter(adapter);
    }

    private void arrangeBanner(HomeModel modCategory) {
        posters = new ArrayList<>();
        imageSlider.removeAllSliders();
        for (int i = 0; i < modCategory.getBanner().size(); i++) {
            textSliderView = new DefaultSliderView(getContext());
            textSliderView
                    .description("")
                    .image( modCategory.getBanner().get(i).getImage())
                    .setScaleType(DefaultSliderView.ScaleType.Fit);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", "");
            imageSlider.addSlider(textSliderView);
        }
        imageSlider.startAutoCycle();

        /*for (int i = 0; i < modCategory.getBanner().size(); i++) {
            Banner img = modCategory.getBanner().get(i);
            immm = img.getImage();
            if(!posters.contains(immm)){
                if(modCategory.getBanner().size()>0){
                    posters.add(new RemoteImage(immm));
                }
            }
        }
            poster_slider.removeAllPosters();
            poster_slider.setPosters(posters);*/
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

    @Override
    public void SearchLisener(String key) {
        Fragment fragment=new ViewAllFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("List", searchKeyList);
        bundle.putString("type", "search");
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(appBarContainer ,fragment)
                .addToBackStack(null)
                .commit();
    }

}
