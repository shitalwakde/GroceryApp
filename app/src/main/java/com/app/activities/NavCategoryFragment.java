package com.app.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.app.activities.MainActivity.containNav;

public class NavCategoryFragment extends Fragment {

    View rootView;
    private RecyclerView recyclerView;
    ImageView iv_back;
    FragmentManager fragmentManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_nav_category, container, false);

        recyclerView=(RecyclerView)rootView.findViewById(R.id.fnc_rv_cat);
        iv_back=(ImageView)rootView.findViewById(R.id.iv_back);
        fragmentManager = getActivity().getSupportFragmentManager();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(containNav , new NavigationViewFragment()).commit();
            }
        });

        return rootView;
    }
}
