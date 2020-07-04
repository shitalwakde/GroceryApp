package com.app.features.navmenu;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.R;
import com.app.callback.HomeClickLisener;
import com.app.features.home.model.Category;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import static com.app.features.navmenu.OrderActivity.tv_toolbar_order;

public class OrderFragment extends Fragment {
    View rootView;
    HomeClickLisener lisener;
    List<Category> orderList;
    RecyclerView rv_order;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.order_fragment, container, false);
        init(rootView);
        click();

        Category cat = new Category();
        cat.setTv_orderId("AA0001");
        cat.setTv_orderDate("27 Jun 2020");
        cat.setTv_deliveryDate("28 Jun 2020");
        cat.setTv_orderAmount("\u20B9 5,764");
        cat.setTv_orderStatus("Pending");
        cat.setTv_payment_status("Unpaid");

        orderList.add(cat);
        orderList.add(cat);
        orderList.add(cat);
        orderList.add(cat);

        OrderAdapter adapter = new OrderAdapter(getActivity(), lisener, orderList);
        rv_order.setAdapter(adapter);

        return rootView;
    }


    private void init(View rootView){
        orderList = new ArrayList<>();
        rv_order = (RecyclerView)rootView.findViewById(R.id.rv_order);
    }

    private void click(){
        tv_toolbar_order.setText("My Orders");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OrderActivity){
            lisener = (HomeClickLisener) context;
        }
    }
}
