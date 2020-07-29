package com.app.features.product;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.R;
import com.app.callback.CategoryListener;
import com.app.callback.ProductListener;
import com.app.constant.AppConstant;
import com.app.controller.AppController;
import com.app.features.home.model.Brand;
import com.app.features.home.model.Category;
import com.app.features.home.model.HomeModel;
import com.app.features.home.model.Product;
import com.app.features.home.model.SubCategory;
import com.app.features.product.adapter.ProductAdapter;
import com.app.features.product.adapter.SubCatAdapter;
import com.app.util.AppUtils;
import com.app.util.RestClient;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.app.activities.MainActivity.iv_edit;
import static com.app.activities.MainActivity.ll_search;
import static com.app.activities.MainActivity.tv_location;
import static com.app.activities.MainActivity.tv_toolbaar;


public class ProductFragment extends Fragment {

    View rootView;
    LinearLayout ll_sort_filter, ll_sort_data, ll_filter;
    RelativeLayout rl_noDataFound;
    RecyclerView rv_product, rv_product2, rv_product3, rv_subCat;
    ArrayList<Product> productList;
    ArrayList<Brand> brandList;
    ArrayList<Category> categoryList;
    ArrayList<SubCategory> subCatList= new ArrayList<>();
    List<SubCategory> subCat;
    ProductListener lisener;
    CategoryListener categoryListener;
    SubCategory subCategory;
    String viewAllType="", categoryId="", subCategoryId="", subCategoryName="";
    private Category category;
    ProgressBar progressBar;

    public ProductFragment(String viewAllType, String categoryId, String subCategoryId, String subCategoryName) {
        this.viewAllType = viewAllType;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.subCategoryName = subCategoryName;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null) {
            category = (Category) getArguments().getSerializable(AppConstant.EXTRA_PROD_CATEGORY);
            if (category != null) {
                subCatList = category.getSubcategory();
//                subCategory = new SubCategory();
//                subCategory.setName("All");
//                subCatList.add(subCategory);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.product_fragment, container, false);
        init(rootView);
        click();

        ll_search.setVisibility(View.GONE);
        iv_edit.setVisibility(View.GONE);
        tv_location.setVisibility(View.GONE);
        tv_toolbaar.setText(category.getCategoryName());
        tv_toolbaar.setTextSize(14);
        //getActivity().setTitle("Products List");

        return rootView;
    }


    private void init(View rootView){
        productList = new ArrayList<Product>();
        brandList = new ArrayList<>();
        categoryList = new ArrayList<>();
        progressBar= (ProgressBar)rootView.findViewById(R.id.progressBar);
        rv_product = (RecyclerView)rootView.findViewById(R.id.rv_product);
        rv_product2 = (RecyclerView)rootView.findViewById(R.id.rv_product2);
        rv_product3 = (RecyclerView)rootView.findViewById(R.id.rv_product3);
        rv_subCat = (RecyclerView)rootView.findViewById(R.id.rv_subCat);
        ll_sort_filter = (LinearLayout) rootView.findViewById(R.id.ll_sort_filter);
        ll_sort_data = (LinearLayout) rootView.findViewById(R.id.ll_sort_data);
        ll_filter = (LinearLayout) rootView.findViewById(R.id.ll_filter);
        rl_noDataFound = (RelativeLayout) rootView.findViewById(R.id.rl_noDataFound);
    }


    private void click(){

        getProductbyCategory(categoryId, subCategoryId);

            //============subcategory====================

            if(subCatList==null) {
                throw new IllegalStateException("Sub category list is null, please check bundle from onCreate");
            }else{
                SubCatAdapter adapter2 = new SubCatAdapter(categoryListener,category, subCatList, subCategoryName);
                rv_subCat.setAdapter(adapter2);
            }

            //============BottomSheetFragment============

            ll_sort_data.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBottomSheetFragment("sort");
                }
            });

            ll_filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBottomSheetFragment("filter");
                }
            });
        }


    private void showBottomSheetFragment(String filterType){
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment(filterType);
        bottomSheetFragment.show(getActivity().getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        lisener = (ProductListener) context;
        categoryListener = (CategoryListener) context;
    }

    private void getProductbyCategory(String categoryId, String subCategoryId){
        progressBar.setVisibility(View.VISIBLE);
        JsonObject jsonObject = new JsonObject();
        if(AppConstant.isLogin(getContext())){
            jsonObject.addProperty("userId", AppUtils.getUserDetails(getContext()).getLoginId());
        }else{
            jsonObject.addProperty("userId", "");
        }
        jsonObject.addProperty("tempUserId", AppController.getInstance().getUniqueID());
        jsonObject.addProperty("categoryId", categoryId);
        jsonObject.addProperty("subCategoryId", subCategoryId);

        new RestClient().getApiService().getProductCategory(jsonObject, new Callback<HomeModel>() {
            @Override
            public void success(HomeModel homeModel, Response response) {
                progressBar.setVisibility(View.GONE);
                if(homeModel.getSuccess().equals("1")){
                    arrangeProductAdpt(homeModel.getProductsList());
                    rl_noDataFound.setVisibility(View.GONE);
                }else{
                    rl_noDataFound.setVisibility(View.VISIBLE);
                    //Toast.makeText(getActivity(), homeModel.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

        private void arrangeProductAdpt(ArrayList<Product> product) {
            ProductAdapter adapter1 = new ProductAdapter(lisener, product);
            rv_product.setAdapter(adapter1);
    }
}
