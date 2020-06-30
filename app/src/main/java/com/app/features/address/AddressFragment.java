package com.app.features.address;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.R;
import com.app.features.checkout.CheckOutFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import static com.app.features.cart.CartActivity.cartContainer;
import static com.app.features.cart.CartActivity.tv_toolbar_cart;

public class AddressFragment extends Fragment {

    View rootView;
    TextView tv_save;
    FragmentManager fragmentManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.address_fragment, container, false);
        init(rootView);
        click();
        return rootView;
    }

    private void init(View rootView){
        fragmentManager = getActivity().getSupportFragmentManager();
        tv_save = (TextView)rootView.findViewById(R.id.tv_save);
    }

    private void click(){
        tv_toolbar_cart.setText("Add Address");

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(cartContainer, new CheckOutFragment()).addToBackStack(null).commit();
            }
        });
    }
}
