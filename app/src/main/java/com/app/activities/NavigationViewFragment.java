package com.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.R;
import com.app.callback.DrawerItemClickLisener;
import com.app.constant.AppConstant;
import com.app.features.login.LoginActivity;

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
    TextView tv_login, tv_singup;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_navigation_view, container, false);
        init(rootView);
        click();
        mDrawerListView = (RecyclerView) rootView.findViewById(R.id.fnd_elv_menu);

        navMenus = AppConstant.getNavMenuItems();

        NavMenuAdapter adapter = new NavMenuAdapter(getActivity(), lisener, navMenus);
        mDrawerListView.setAdapter(adapter);
        return rootView;
    }


    private void init(View rootView){
        tv_login = (TextView)rootView.findViewById(R.id.tv_login);
        tv_singup = (TextView)rootView.findViewById(R.id.tv_singup);
    }

    private void click(){
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("type", "login");
                startActivity(intent);
            }
        });

        tv_singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("type", "singup");
                startActivity(intent);
            }
        });

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
