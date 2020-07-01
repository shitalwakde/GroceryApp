package com.app.features.wallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class WalletFragment extends Fragment {

    View rootView;
    RecyclerView rv_wallet;
    CardView cardView;
    FragmentManager fragmentManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.wallet_fragment, container, false);
        init(rootView);
        click();

        return rootView;
    }

    private void init(View rootView){
        fragmentManager = getActivity().getSupportFragmentManager();
        rv_wallet = (RecyclerView)rootView.findViewById(R.id.rv_wallet);
        cardView = (CardView)rootView.findViewById(R.id.cardView);
    }

    private void click(){
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.wallet_container, new AddWalletFragment()).addToBackStack(null).commit();
            }
        });
    }
}
