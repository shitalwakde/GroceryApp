package com.app.features.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.app.features.wallet.WalletActivity.tv_toolbar_wallet;

public class AddWalletFragment extends Fragment {

    View rootView;
    TextView tv_add_money;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.add_wallet_fragment, container, false);

        tv_toolbar_wallet.setText("Add Wallet Money");
        tv_add_money = (TextView)rootView.findViewById(R.id.tv_add_money);

        tv_add_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WalletActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return rootView;
    }
}
