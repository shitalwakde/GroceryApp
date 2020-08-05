package com.app.features.order;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.R;
import com.app.callback.OrderDetailLisener;
import com.app.util.AppUtils;
import com.app.util.RestClient;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.app.features.order.OrderActivity.tv_toolbar_order;

public class OrderFragment extends Fragment implements OrderDetailLisener{
    View viewRoot;
    RelativeLayout rl_noDataFound;
    RecyclerView rv_order;
    FragmentManager fragmentManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.order_fragment, container, false);
        init(viewRoot);
        getOrderList();
        return viewRoot;
    }

    private void init(View viewRoot){
        tv_toolbar_order.setText("My Orders");
        fragmentManager = getActivity().getSupportFragmentManager();
        rl_noDataFound = (RelativeLayout) viewRoot.findViewById(R.id.rl_noDataFound);
        rv_order = (RecyclerView) viewRoot.findViewById(R.id.rv_order);
    }

    private void getOrderList(){
        JsonObject jsonObject= new JsonObject();
        jsonObject.addProperty("userId", AppUtils.getUserDetails(getActivity()).getLoginId());

        new RestClient().getApiService().getOrderList(jsonObject, new Callback<OrderModel>() {
            @Override
            public void success(OrderModel orderModel, Response response) {
                if(orderModel.getSuccess().equals("1")){
                    arrangeOrderAdap(orderModel.getOrderList());
                    rl_noDataFound.setVisibility(View.GONE);
                }else{
                    rl_noDataFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void arrangeOrderAdap(ArrayList<OrderList> orderList){
        OrderAdapter adapter = new OrderAdapter(orderList, this);
        rv_order.setAdapter(adapter);
    }


    @Override
    public void orderClickLisener(OrderList orderList) {
        Fragment fragment = new OrderDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("List", (Serializable) orderList);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.order_container, fragment)
                .addToBackStack(null)
                .commit();
    }

}
