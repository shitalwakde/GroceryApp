package com.app.features.product;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.R;
import com.app.callback.CategoryListener;
import com.app.callback.ProductListener;
import com.app.constant.AppConstant;
import com.app.controller.AppController;
import com.app.features.home.model.HomeModel;
import com.app.features.home.model.Product;
import com.app.features.product.adapter.ProductAdapter;
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

public class BrandFragment extends Fragment {

    View rootView;
    RecyclerView rv_product;
    ProgressBar progressBar;
    RelativeLayout rl_noDataFound;
    ProductListener lisener;
    String brandId;

    public BrandFragment(String brandId) {
        this.brandId = brandId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.product_fragment, container, false);
        init(rootView);
        click();
        return rootView;
    }

    private void init(View rootView){
        rv_product = (RecyclerView) rootView.findViewById(R.id.rv_product);
        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
        rl_noDataFound = (RelativeLayout)rootView.findViewById(R.id.rl_noDataFound);
    }


    private void click(){
        getProductbyCategory(brandId);
    }


    private void getProductbyCategory(String brandId){
        progressBar.setVisibility(View.VISIBLE);
        JsonObject jsonObject = new JsonObject();
        if(AppConstant.isLogin(getContext())){
            jsonObject.addProperty("userId", AppUtils.getUserDetails(getContext()).getLoginId());
        }else{
            jsonObject.addProperty("userId", "");
        }
        jsonObject.addProperty("tempUserId", AppController.getInstance().getUniqueID());
        jsonObject.addProperty("brandId", brandId);
        jsonObject.addProperty("categoryId", "");
        jsonObject.addProperty("subCategoryId", "");

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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        lisener = (ProductListener) context;
    }

}
