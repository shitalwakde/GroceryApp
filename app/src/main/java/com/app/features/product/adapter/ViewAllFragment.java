package com.app.features.product.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.R;
import com.app.activities.NavCategoryFragment;
import com.app.callback.BrandLisener;
import com.app.callback.CategoryListener;
import com.app.callback.ProductListener;
import com.app.constant.AppConstant;
import com.app.features.home.adapter.BrandAdapter;
import com.app.features.home.adapter.CategoryAdapter;
import com.app.features.home.model.Brand;
import com.app.features.home.model.Product;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class ViewAllFragment extends Fragment {

    LinearLayout ll_sort_filter;
    BrandLisener brandLisener;
    CategoryListener catLisener;
    ProductListener lisener;
    RecyclerView rv_product, rv_product2;
    ArrayList<Brand> brands;
    ArrayList<Product> bestSellingList;
    ArrayList<Product> recentlyViewList;
    View rootView;
    String type ="";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!= null){
            brands = (ArrayList<Brand>) getArguments().getSerializable("List");
            bestSellingList = (ArrayList<Product>) getArguments().getSerializable("List");
            recentlyViewList = (ArrayList<Product>) getArguments().getSerializable("List");
            type = getArguments().getString("type");
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
        ll_sort_filter = (LinearLayout)rootView.findViewById(R.id.ll_sort_filter);
        rv_product2 = (RecyclerView)rootView.findViewById(R.id.rv_product2);
        rv_product = (RecyclerView)rootView.findViewById(R.id.rv_product);
    }

    private void setAdapters(){
        if(type.equals("category")){
            ll_sort_filter.setVisibility(View.GONE);
            rv_product2.setVisibility(View.VISIBLE);
            rv_product.setVisibility(View.GONE);
            CategoryAdapter adapter = new CategoryAdapter(catLisener, NavCategoryFragment.categories,
                    AppConstant.FROM_CATEGORY_PRODUCT);
            rv_product2.setAdapter(adapter);
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
        }else {
            ll_sort_filter.setVisibility(View.VISIBLE);
            rv_product2.setVisibility(View.GONE);
            rv_product.setVisibility(View.VISIBLE);
            ProductAdapter adapter1 = new ProductAdapter(lisener, recentlyViewList);
            rv_product.setAdapter(adapter1);
        }

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        brandLisener = (BrandLisener) context;
        catLisener = (CategoryListener) context;
        lisener = (ProductListener) context;
    }

}
