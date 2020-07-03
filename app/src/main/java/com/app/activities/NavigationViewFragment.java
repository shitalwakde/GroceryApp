package com.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.R;
import com.app.callback.DrawerItemClickLisener;
import com.app.constant.AppConstant;
import com.app.features.login.LoginActivity;
import com.app.util.AppUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.app.activities.MainActivity.drawerLayout;

public class NavigationViewFragment extends Fragment {

    View rootView;
    RecyclerView mDrawerListView;
    ArrayList<NavMenu> navMenus;
    DrawerItemClickLisener lisener;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_singup)
    TextView tvSingup;
    @BindView(R.id.li_login)
    LinearLayout liLogin;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_email)
    TextView tvUserEmail;
    @BindView(R.id.li_user_details)
    LinearLayout liUserDetails;
    @BindView(R.id.fnd_elv_menu)
    RecyclerView fndElvMenu;
    private NavMenuAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_navigation_view, container, false);
        ButterKnife.bind(this, rootView);
        click();

        mDrawerListView = (RecyclerView) rootView.findViewById(R.id.fnd_elv_menu);
        navMenus = AppConstant.getNavMenuItems(getContext());

        adapter = new NavMenuAdapter(getActivity(), lisener, navMenus);
        mDrawerListView.setAdapter(adapter);
        return rootView;
    }



    private void click() {

        changeLayout();


        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                getActivity().finish();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("type", "login");
                startActivity(intent);
            }
        });

        tvSingup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("type", "singup");
                startActivity(intent);
            }
        });

    }

    private void changeLayout() {
        if(AppConstant.isLogin(getActivity())){
            liUserDetails.setVisibility(View.VISIBLE);
            liLogin.setVisibility(View.GONE);
            if(AppUtils.getUserDetails(getActivity()) != null){
                tvUserName.setText(AppUtils.getUserDetails(getActivity()).getEmail());
                tvUserEmail.setText(AppUtils.getUserDetails(getActivity()).getMobile());
            }
        }else {
            liUserDetails.setVisibility(View.GONE);
            liLogin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            lisener = (DrawerItemClickLisener) context;
        }
    }

    public void changeMenus() {
        if(adapter!=null && navMenus!=null ) {
            navMenus.clear();;
            navMenus.addAll(AppConstant.getNavMenuItems(getContext()));
            adapter.notifyDataSetChanged();
            changeLayout();
        }
    }
}
