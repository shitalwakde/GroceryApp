package com.app.features.navmenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.R;
import com.app.activities.MainActivity;
import com.app.features.home.model.Category;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import static com.app.features.navmenu.OrderActivity.tv_toolbar_order;

public class OrderDetailFragment extends Fragment {

    View rootView;
    TextView tv_continue;
    RecyclerView rv_order_view;
    List<Category> orderDetailList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.order_detail_fragment, container, false);
        init(rootView);
        click();

        return rootView;
    }

    private void init(View rootView){
        orderDetailList = new ArrayList<>();
        tv_continue = (TextView)rootView.findViewById(R.id.tv_continue);
        rv_order_view = (RecyclerView)rootView.findViewById(R.id.rv_order_view);
    }

    private void click(){

        tv_toolbar_order.setText("My Order Detail");

        Category cate = new Category();
        cate.setIv_best(R.drawable.grapes);
        cate.setTv_pr_name("Nutella");
        cate.setTv_pr_sub_name("HazeInut Spread with Cocoa");

        orderDetailList.add(cate);
        orderDetailList.add(cate);
        orderDetailList.add(cate);
        orderDetailList.add(cate);

        OrderDetailAdapter adapter1 = new OrderDetailAdapter(getActivity(), orderDetailList);
        rv_order_view.setAdapter(adapter1);

        tv_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
