package com.app.activities;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.R;
import com.app.callback.CategoryListener;
import com.app.callback.HomeClickLisener;
import com.app.features.home.Category;
import com.app.features.home.SubCategory;
import com.app.util.ExpandableItemIndicator;
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.utils.RecyclerViewAdapterUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;

import static com.app.activities.MainActivity.drawerLayout;

public class AdpCategorySubCategory extends AbstractExpandableItemAdapter<AdpCategorySubCategory.MyGroupViewHolder, AdpCategorySubCategory.MyChildViewHolder> {
    private static final String TAG = "SubcategoryAdapter";
    private static final int NOTIFICATION = 543;
    private final List<Category> data;
    HashMap<Category, ArrayList<SubCategory>> CategoryDocuments;
    private AppCompatActivity context;
    public static String id;
    CategoryListener lisener;

    private RecyclerViewExpandableItemManager mExpandableItemManager;

    private View.OnClickListener mItemOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onClickItemView(v);
        }
    };

    private interface Expandable extends ExpandableItemConstants {
    }

    public AdpCategorySubCategory(CategoryListener lisener, final List<Category> data, HashMap<Category, ArrayList<SubCategory>> CategoryDocuments, AppCompatActivity context, RecyclerViewExpandableItemManager expandableItemManager) {
        this.lisener = lisener;
        this.data = data;
        mExpandableItemManager = expandableItemManager;
        this.CategoryDocuments = CategoryDocuments;
        setHasStableIds(true);
        this.context = context;

    }

    public static class MyGroupViewHolder extends AbstractExpandableItemViewHolder {
        public ExpandableItemIndicator mIndicator;
        public TextView tvName;
        public TextView tvCount;
        public FrameLayout mmContainer;
        public LinearLayout afgl_ll_parent;
        public LinearLayout llViewAll;

        public MyGroupViewHolder(View v, View.OnClickListener clickListener) {
            super(v);
            mIndicator = (ExpandableItemIndicator) v.findViewById(R.id.indicator);
            tvName = (TextView) v.findViewById(R.id.acgl_tv_cat_name);
            afgl_ll_parent = (LinearLayout) v.findViewById(R.id.afgl_ll_parent);
            mmContainer = (FrameLayout) v.findViewById(R.id.container);
            mmContainer.setOnClickListener(clickListener);

        }
    }


    public static class MyChildViewHolder extends AbstractExpandableItemViewHolder {
        public TextView tvParticulars;
        LinearLayout llName;
        public MyChildViewHolder(View v) {
            super(v);
            tvParticulars = (TextView) v.findViewById(R.id.accl_tv_subcat_name);
            llName = (LinearLayout) v.findViewById(R.id.accl_ll_name);
        }
    }


    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return CategoryDocuments.get(data.get(groupPosition)).size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        return 0;
    }

    @Override
    public int getChildItemViewType(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public MyGroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.adapter_category_group_layout, parent, false);
        return new MyGroupViewHolder(v, mItemOnClickListener);
    }

    @Override
    public MyChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.adapter_category_child_layout, parent, false);
        return new MyChildViewHolder(v);
    }

    @Override
    public void onBindGroupViewHolder(final MyGroupViewHolder holder, final int groupPosition, int viewType) {
        final Category cat = data.get(groupPosition);
        holder.tvName.setText(cat.getCategoryName());
    }

    @Override
    public void onBindChildViewHolder(final MyChildViewHolder holder, final int groupPosition, final int childPosition, int viewType) {

        final SubCategory subcategory = CategoryDocuments.get(data.get(groupPosition)).get(childPosition);

        holder.tvParticulars.setText(subcategory.getName());

        holder.llName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                Category cat = data.get(groupPosition);
                Log.w("TAG", " catid = " + cat.getCategoryId());
                Log.w("TAG", " subcategory.getId() = " + subcategory.getSubCategoryId());
                lisener.subcategoryClickLisener(cat, CategoryDocuments.get(data.get(groupPosition)).get(childPosition));

                /*Intent intent = new Intent(context, ActContainer.class);
                intent.putExtra("flag","Shop by SubCategory");
                intent.putExtra("sub_id",subcategory.getPrdsubcat_id());
                intent.putExtra("title",subcategory.getPrdsubcat_name());
                intent.putExtra("id",cat.getCat_id());
                context.startActivity(intent);*/
            }
        });

    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(MyGroupViewHolder holder, int groupPosition,
                                                   int x, int y, boolean expand) {
        // check the item is *not* pinned
        /*if (data.get(groupPosition).isPinned()) {
            // return false to raise View.OnClickListener#onClick() event
            return false;
        }

        // check is enabled
        if (!(holder.itemView.isEnabled() && holder.itemView.isClickable())) {
            return false;
        }*/

        return false;
    }

    void onClickItemView(View v) {
        RecyclerView.ViewHolder vh = RecyclerViewAdapterUtils.getViewHolder(v);
        int flatPosition = vh.getAdapterPosition();

        if (flatPosition == RecyclerView.NO_POSITION) {
            return;
        }

        long expandablePosition = mExpandableItemManager.getExpandablePosition(flatPosition);
        int groupPosition = RecyclerViewExpandableItemManager.getPackedPositionGroup(expandablePosition);
        int childPosition = RecyclerViewExpandableItemManager.getPackedPositionChild(expandablePosition);

        switch (v.getId()) {
            case R.id.container:
                if (childPosition == RecyclerView.NO_POSITION) {
                    Log.d(TAG, "handleOnClickGroupItemContainerView");
                    handleOnClickGroupItemContainerView(groupPosition);
                } else {
                    handleOnClickChildItemContainerView(groupPosition, childPosition);
                }
                break;
        }
    }

    private void handleOnClickChildItemContainerView(int groupPosition, int childPosition) {
    }

    private void handleOnClickGroupItemContainerView(int groupPosition) {
        if (isGroupExpanded(groupPosition)) {
            mExpandableItemManager.collapseGroup(groupPosition);
        } else {
            mExpandableItemManager.collapseAll();
            mExpandableItemManager.expandGroup(groupPosition);
            int childItemHeight = context.getResources().getDimensionPixelSize(R.dimen.list_item_height);
            int topMargin = (int) (context.getResources().getDisplayMetrics().density * 16); // top-spacing: 16dp
            int bottomMargin = topMargin; // bottom-spacing: 16dp
            mExpandableItemManager.scrollToGroup(groupPosition, childItemHeight, topMargin, bottomMargin);
        }
    }

    private boolean isGroupExpanded(int groupPosition) {
        return mExpandableItemManager.isGroupExpanded(groupPosition);
    }

}