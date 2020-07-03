package com.app.features.product;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.R;
import com.app.callback.CategoryListener;
import com.app.callback.HomeClickLisener;
import com.app.constant.AppConstant;
import com.app.features.home.BrandAdapter;
import com.app.features.home.Category;
import com.app.features.home.CategoryAdapter;
import com.app.features.home.SubCategory;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import static com.app.activities.MainActivity.ll_search;

public class ProductFragment extends Fragment {

    View rootView;
    LinearLayout ll_sort_filter, ll_sort_data, ll_filter;
    RecyclerView rv_product, rv_product2, rv_product3, rv_subCat;
    List<Category> productList;
    List<Category> brandList;
    List<Category> categoryList;
    List<SubCategory> subCatList;
    CategoryListener lisener;
    String viewAllType="", categoryId="", subCategoryId="", subCategoryName="";
    private Category category;

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
            if (category != null)
                subCatList=category.getSubcategory();
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
        //getActivity().setTitle("Products List");

        return rootView;
    }


    private void init(View rootView){
        productList = new ArrayList<>();
        brandList = new ArrayList<>();
        categoryList = new ArrayList<>();
        rv_product = (RecyclerView)rootView.findViewById(R.id.rv_product);
        rv_product2 = (RecyclerView)rootView.findViewById(R.id.rv_product2);
        rv_product3 = (RecyclerView)rootView.findViewById(R.id.rv_product3);
        rv_subCat = (RecyclerView)rootView.findViewById(R.id.rv_subCat);
        ll_sort_filter = (LinearLayout) rootView.findViewById(R.id.ll_sort_filter);
        ll_sort_data = (LinearLayout) rootView.findViewById(R.id.ll_sort_data);
        ll_filter = (LinearLayout) rootView.findViewById(R.id.ll_filter);
    }


    private void click(){

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

            //============subcategory===============
            if(subCatList==null)
                throw new IllegalStateException("Sub category list is null, please check bundle from onCreate");

            SubCatAdapter adapter2 = new SubCatAdapter(lisener,category, subCatList, subCategoryName);
            rv_subCat.setAdapter(adapter2);

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

    }


    private void showBottomSheetFragment(String filterType){
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment(filterType);
        bottomSheetFragment.show(getActivity().getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        lisener = (CategoryListener) context;
    }
}
