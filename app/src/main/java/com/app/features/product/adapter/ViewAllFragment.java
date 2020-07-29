package com.app.features.product.adapter;

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
import com.app.activities.MainActivity;
import com.app.activities.NavCategoryFragment;
import com.app.callback.BrandLisener;
import com.app.callback.CategoryListener;
import com.app.callback.ProductListener;
import com.app.constant.AppConstant;
import com.app.controller.AppController;
import com.app.features.home.adapter.BrandAdapter;
import com.app.features.home.adapter.CategoryAdapter;
import com.app.features.home.model.Brand;
import com.app.features.home.model.Product;
import com.app.util.AppUtils;
import com.app.util.RestClient;
import com.google.gson.JsonObject;

import java.util.ArrayList;

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

public class ViewAllFragment extends Fragment {

    LinearLayout ll_sort_filter;
    BrandLisener brandLisener;
    CategoryListener catLisener;
    ProductListener lisener;
    RecyclerView rv_product, rv_product2;
    RelativeLayout rl_noDataFound;
    ProgressBar progressBar;
    ArrayList<Brand> brands;
    ArrayList<Product> bestSellingList;
    ArrayList<Product> recentlyViewList;
    ArrayList<Product> searchKeyList;
    View rootView;
    String type ="", productId = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!= null){
//            brands = (ArrayList<Brand>) getArguments().getSerializable("List");
//            bestSellingList = (ArrayList<Product>) getArguments().getSerializable("List");
//            recentlyViewList = (ArrayList<Product>) getArguments().getSerializable("List");
            type = getArguments().getString("type");
            productId = getArguments().getString("productId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.view_all_fragment, container, false);
        init(rootView);
        setAdapters();

        return rootView;
    }

    private void init(View rootView){
        ll_search.setVisibility(View.GONE);
        iv_edit.setVisibility(View.GONE);
        tv_location.setVisibility(View.GONE);
        tv_toolbaar.setText("Products");
        tv_toolbaar.setTextSize(14);
        ll_sort_filter = (LinearLayout)rootView.findViewById(R.id.ll_sort_filter);
        ll_sort_filter.setVisibility(View.VISIBLE);
        rv_product2 = (RecyclerView)rootView.findViewById(R.id.rv_product2);
        rv_product = (RecyclerView)rootView.findViewById(R.id.rv_product);
        rl_noDataFound = (RelativeLayout)rootView.findViewById(R.id.rl_noDataFound);
        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
    }

    private void setAdapters(){
        rv_product.setVisibility(View.VISIBLE);
        viewAllProduct();
        /*if(type.equals("category")){
            ll_sort_filter.setVisibility(View.GONE);
            rv_product2.setVisibility(View.VISIBLE);
            rv_product.setVisibility(View.GONE);
            if(NavCategoryFragment.categories != null){
                CategoryAdapter adapter = new CategoryAdapter(catLisener, NavCategoryFragment.categories,
                        AppConstant.FROM_CATEGORY_PRODUCT);
                rv_product2.setAdapter(adapter);
            }
        }else if(type.equals("brand")){
            ll_sort_filter.setVisibility(View.GONE);
            rv_product2.setVisibility(View.VISIBLE);
            rv_product.setVisibility(View.GONE);
            BrandAdapter adapter=new BrandAdapter(brandLisener,brands);
            rv_product2.setAdapter(adapter);
        }else if(type.equals("bestselling")){
            ll_sort_filter.setVisibility(View.VISIBLE);
            rv_product2.setVisibility(View.GONE);
            rv_product.setVisibility(View.VISIBLE);
            ProductAdapter adapter1 = new ProductAdapter(lisener, bestSellingList);
            rv_product.setAdapter(adapter1);
        }else if(type.equals("recently")){
            ll_sort_filter.setVisibility(View.VISIBLE);
            rv_product2.setVisibility(View.GONE);
            rv_product.setVisibility(View.VISIBLE);
            ProductAdapter adapter1 = new ProductAdapter(lisener, recentlyViewList);
            rv_product.setAdapter(adapter1);
        }else{
            ll_sort_filter.setVisibility(View.VISIBLE);
            rv_product2.setVisibility(View.GONE);
            rv_product.setVisibility(View.VISIBLE);
            ProductAdapter adapter1 = new ProductAdapter(lisener, recentlyViewList);
            rv_product.setAdapter(adapter1);
        }*/

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        brandLisener = (BrandLisener) context;
        catLisener = (CategoryListener) context;
        lisener = (ProductListener) context;
    }


    private void viewAllProduct(){
        progressBar.setVisibility(View.VISIBLE);
        JsonObject jsonObject = new JsonObject();
        if(AppConstant.isLogin(getActivity())){
            jsonObject.addProperty("userId", AppUtils.getUserDetails(getActivity()).getLoginId());
        }else{
            jsonObject.addProperty("userId", "");
        }
        jsonObject.addProperty("tempUserId", AppController.getInstance().getUniqueID());
        jsonObject.addProperty("productId", productId);
        jsonObject.addProperty("flag", type);

        new RestClient().getApiService().viewAllProduct(jsonObject, new Callback<Product>() {
            @Override
            public void success(Product product, Response response) {
                progressBar.setVisibility(View.GONE);
                if(product.getSuccess().equals("1")){
                    arrangeProductAdap(product.getProductList());
                    rl_noDataFound.setVisibility(View.GONE);
                }else{
                    rl_noDataFound.setVisibility(View.VISIBLE);
                    //Toast.makeText(getActivity(), product.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void arrangeProductAdap(ArrayList<Product> productList) {
        ProductAdapter adapter1 = new ProductAdapter(lisener, productList);
        rv_product.setAdapter(adapter1);
    }

}
