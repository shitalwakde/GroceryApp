package com.app.features.product;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.R;
import com.app.callback.HomeClickLisener;
import com.app.features.home.BrandAdapter;
import com.app.features.home.Category;
import com.app.features.home.CategoryAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import static com.app.activities.MainActivity.ll_search;

public class ProductFragment extends Fragment {

    View rootView;
    LinearLayout ll_sort_filter;
    RecyclerView rv_product, rv_product2, rv_product3;
    List<Category> productList;
    List<Category> brandList;
    List<Category> categoryList;
    HomeClickLisener lisener;
    String viewAllType="";

    public ProductFragment(String viewAllType) {
        this.viewAllType = viewAllType;
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

        ll_search.setVisibility(View.GONE);
        //getActivity().setTitle("Products List");

        if(viewAllType.equals("category")){
            ll_sort_filter.setVisibility(View.GONE);
            rv_product2.setVisibility(View.VISIBLE);
            rv_product.setVisibility(View.GONE);
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
            rv_product2.setAdapter(adapter);
        }else if(viewAllType.equals("brand")){
            ll_sort_filter.setVisibility(View.GONE);
            rv_product3.setVisibility(View.VISIBLE);
            rv_product.setVisibility(View.GONE);
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
            rv_product3.setAdapter(adapter3);
        }else {
            Category cate = new Category();
            cate.setIv_best(R.drawable.grapes);
            cate.setTv_pr_name("Nutella");
            cate.setTv_pr_sub_name("HazeInut Spread with Cocoa");

            productList.add(cate);
            productList.add(cate);
            productList.add(cate);
            productList.add(cate);

            ProductAdapter adapter1 = new ProductAdapter(lisener, productList);
            rv_product.setAdapter(adapter1);
        }

        return rootView;
    }


    private void init(View rootView){
        productList = new ArrayList<>();
        brandList = new ArrayList<>();
        categoryList = new ArrayList<>();
        rv_product = (RecyclerView)rootView.findViewById(R.id.rv_product);
        rv_product2 = (RecyclerView)rootView.findViewById(R.id.rv_product2);
        rv_product3 = (RecyclerView)rootView.findViewById(R.id.rv_product3);
        ll_sort_filter = (LinearLayout) rootView.findViewById(R.id.ll_sort_filter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        lisener = (HomeClickLisener) context;
    }
}
