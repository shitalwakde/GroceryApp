package com.app.features.refer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.app.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class RewardFragment extends Fragment {

    View rootView;
    RelativeLayout rl_noDataFound;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.reward_fragment, container, false);
        init(rootView);
        return rootView;
    }

    private void init(View rootView){
        rl_noDataFound = (RelativeLayout)rootView.findViewById(R.id.rl_noDataFound);
    }

    @Override
    public void onResume() {
        super.onResume();
        //((ReferFriendActivity) getContext()).fabButton(false);
    }
}
