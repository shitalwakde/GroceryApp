package com.app.features.cart;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.R;
import com.app.callback.HomeClickLisener;
import com.app.features.address.AddressFragment;
import com.app.features.home.model.Category;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.app.features.cart.CartActivity.cartContainer;
import static com.app.features.cart.CartActivity.tv_toolbar_cart;

public class CartFragment extends Fragment {

    View rootView;
    RecyclerView rv_cart;
    List<Category> productList;
    HomeClickLisener lisener;
    TextView tv_checkout;
    FragmentManager fragmentManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.cart_fragment, container, false);
        init(rootView);
        click();

        return rootView;
    }


    private void init(View rootView){
        fragmentManager = getActivity().getSupportFragmentManager();
        productList = new ArrayList<>();
        rv_cart = (RecyclerView)rootView.findViewById(R.id.rv_cart);
        tv_checkout = (TextView)rootView.findViewById(R.id.tv_checkout);
    }

    private void click(){

        tv_toolbar_cart.setText("My Cart");

        Category cate = new Category();
        cate.setIv_best(R.drawable.grapes);
        cate.setTv_pr_name("Nutella");
        cate.setTv_pr_sub_name("HazeInut Spread with Cocoa");

        productList.add(cate);
        productList.add(cate);
        productList.add(cate);
        productList.add(cate);

        CartAdapter adapter1 = new CartAdapter(lisener, productList);
        rv_cart.setAdapter(adapter1);

        tv_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(cartContainer, new AddressFragment()).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof CartActivity){
            lisener = (HomeClickLisener) context;
        }
    }
}
