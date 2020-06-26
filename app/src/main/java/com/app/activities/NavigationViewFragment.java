package com.app.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.R;
import com.app.callback.DrawerItemClickLisener;
import com.app.constant.AppConstant;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class NavigationViewFragment extends Fragment {

    View rootView;
    RecyclerView mDrawerListView;
    ArrayList<NavMenu> navMenus;
    DrawerItemClickLisener lisener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_navigation_view, container, false);
        mDrawerListView = (RecyclerView) rootView.findViewById(R.id.fnd_elv_menu);

        navMenus = AppConstant.getNavMenuItems();

        NavMenuAdapter adapter = new NavMenuAdapter(lisener, navMenus);
        mDrawerListView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof MainActivity){
            lisener = (DrawerItemClickLisener) context;
        }
    }
}
