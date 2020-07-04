package com.app.features.navmenu;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.R;
import com.app.callback.HomeClickLisener;
import com.app.features.home.model.Category;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class WishListFragment extends Fragment {
    View rootView;
    RecyclerView rv_wish;
    List<Category> productList;
    HomeClickLisener lisener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.wishlist_fragment, container, false);
        init(rootView);
        click();

        return rootView;
    }

    private void init(View rootView){
        productList = new ArrayList<>();
        rv_wish = (RecyclerView)rootView.findViewById(R.id.rv_wish);
    }

    private void click(){
        Category cate = new Category();
        cate.setIv_best(R.drawable.grapes);
        cate.setTv_pr_name("Nutella");
        cate.setTv_pr_sub_name("HazeInut Spread with Cocoa");

        productList.add(cate);
        productList.add(cate);
        productList.add(cate);
        productList.add(cate);

        WishListAdapter adapter1 = new WishListAdapter(lisener, productList);
        rv_wish.setAdapter(adapter1);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof WishListActivity){
            lisener = (HomeClickLisener) context;
        }
    }
}
