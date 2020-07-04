package com.app.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.R;
import com.app.callback.CategoryListener;
import com.app.features.home.model.Category;
import com.app.features.home.model.SubCategory;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.app.activities.MainActivity.containNav;

public class NavCategoryFragment extends Fragment implements RecyclerViewExpandableItemManager.OnGroupCollapseListener,
        RecyclerViewExpandableItemManager.OnGroupExpandListener{

    View rootView;
    private RecyclerView recyclerView;
    ImageView iv_back;
    FragmentManager fragmentManager;

    private static final String SAVED_STATE_EXPANDABLE_ITEM_MANAGER = "RecyclerViewExpandableItemManager";
    public static ArrayList<Category> categories;
    public static HashMap<Category, ArrayList<SubCategory>> categorySubcat;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManager;
    private AdpCategorySubCategory myItemAdapter;
    CategoryListener lisener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_nav_category, container, false);
        init(rootView);
        click();

        final Parcelable eimSavedState = (savedInstanceState != null) ? savedInstanceState.getParcelable(SAVED_STATE_EXPANDABLE_ITEM_MANAGER) : null;
        mRecyclerViewExpandableItemManager = new RecyclerViewExpandableItemManager(eimSavedState);
        mRecyclerViewExpandableItemManager.setOnGroupExpandListener(this);
        mRecyclerViewExpandableItemManager.setOnGroupCollapseListener(this);
        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();

        animator.setSupportsChangeAnimations(false);
        recyclerView.setItemAnimator(animator);
        recyclerView.setHasFixedSize(false);
        mRecyclerViewExpandableItemManager.attachRecyclerView(recyclerView);

        if (categories.size() > 0) {
            myItemAdapter = new AdpCategorySubCategory(lisener, categories, categorySubcat, ((AppCompatActivity) getActivity()), mRecyclerViewExpandableItemManager);
            mWrappedAdapter = mRecyclerViewExpandableItemManager.createWrappedAdapter(myItemAdapter);
            recyclerView.setAdapter(mWrappedAdapter);
            recyclerView.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    private void init(View rootView){
        recyclerView=(RecyclerView)rootView.findViewById(R.id.fnc_rv_cat);
        iv_back=(ImageView)rootView.findViewById(R.id.iv_back);
        fragmentManager = getActivity().getSupportFragmentManager();
    }

    private void click(){

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(containNav , new NavigationViewFragment()).commit();
            }
        });
    }

    @Override
    public void onGroupCollapse(int groupPosition, boolean fromUser) {

    }

    @Override
    public void onGroupExpand(int groupPosition, boolean fromUser) {

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof MainActivity){
            lisener = (CategoryListener) context;
        }
    }
}
