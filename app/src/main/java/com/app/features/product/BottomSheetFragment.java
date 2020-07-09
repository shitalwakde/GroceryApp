package com.app.features.product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.R;
import com.app.features.home.model.Category;
import com.app.features.product.adapter.AdpFilterCategory;
import com.app.features.product.adapter.AdpFilterCategoryData;
import com.app.features.product.adapter.AdpSortData;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    View rootView;
    RecyclerView rv_filter_category,rv_filter_data,rv_sort_data;
    List<Category> datalist;
    AdpFilterCategory adpFilterCategory;
    AdpFilterCategoryData adpFilterCategoryData;
    AdpSortData adpSortData;
    String filterType;
    LinearLayout li_filter,li_sort;
    RelativeLayout rl_sort, rl_filter;
    TextView tv_clear_all, tv_apply_filter;

    public BottomSheetFragment(String filterType) {
        this.filterType = filterType;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.bottom_sheet_fragment, container, false);
        init(rootView);
        click();

        return rootView;
    }

    private void init(View rootView){
        datalist = new ArrayList<>();
        tv_apply_filter = (TextView)rootView.findViewById(R.id.tv_apply_filter);
        tv_clear_all = (TextView)rootView.findViewById(R.id.tv_clear_all);
        li_sort = (LinearLayout)rootView.findViewById(R.id.li_sort);
        li_filter = (LinearLayout)rootView.findViewById(R.id.li_filter);
        rl_filter = (RelativeLayout) rootView.findViewById(R.id.rl_filter);
        rl_sort = (RelativeLayout)rootView.findViewById(R.id.rl_sort);
        rv_filter_category = (RecyclerView)rootView.findViewById(R.id.rv_filter_category);
        rv_filter_data = (RecyclerView)rootView.findViewById(R.id.rv_filter_data);
        rv_sort_data = (RecyclerView)rootView.findViewById(R.id.rv_sort_data);

        if(filterType.equals("sort")){
            li_sort.setVisibility(View.VISIBLE);
            li_filter.setVisibility(View.GONE);
        }else{
            li_sort.setVisibility(View.GONE);
            li_filter.setVisibility(View.VISIBLE);
        }

    }

    private void click(){

        rl_sort.setOnClickListener(this);
        rl_filter.setOnClickListener(this);
        tv_clear_all.setOnClickListener(this);
        tv_apply_filter.setOnClickListener(this);

        adpFilterCategory = new AdpFilterCategory();
        rv_filter_category.setAdapter(adpFilterCategory);

        adpFilterCategoryData = new AdpFilterCategoryData(getActivity(), datalist);
        rv_filter_data.setAdapter(adpFilterCategoryData);

        adpSortData = new AdpSortData();
        rv_sort_data.setAdapter(adpSortData);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_sort:
                dismiss();
                break;
            case R.id.rl_filter:
                //callback.onFilterApplied();
                dismiss();
                break;
            case R.id.tv_clear_all:
                //callback.onClearFilter();
                dismiss();
                break;
            case R.id.tv_apply_filter:
                //callback.onFilterApplied();
                dismiss();
                break;

        }
    }
}
