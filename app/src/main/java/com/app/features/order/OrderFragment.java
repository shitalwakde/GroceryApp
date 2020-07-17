package com.app.features.order;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.R;
import com.app.callback.HomeClickLisener;
import com.app.callback.OrderDetailLisener;
import com.app.features.address.AddressModel;
import com.app.features.product.adapter.ViewAllFragment;
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

import static com.app.activities.MainActivity.appBarContainer;
import static com.app.features.order.OrderActivity.tv_toolbar_order;

public class OrderFragment extends Fragment implements OrderDetailLisener {
    View rootView;
    RecyclerView rv_order;
    FragmentManager fragmentManager;
    RelativeLayout rl_noDataFound;

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
        setDetail();

        return rootView;
    }


    private void init(View rootView){
        fragmentManager = getActivity().getSupportFragmentManager();
        rv_order = (RecyclerView)rootView.findViewById(R.id.rv_order);
        rl_noDataFound = (RelativeLayout)rootView.findViewById(R.id.rl_noDataFound);
    }

    private void setDetail(){
        tv_toolbar_order.setText("My Orders");
    }

    @Override
    public void onResume() {
        super.onResume();
        getOrderList();
    }

    private void getOrderList(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", AppUtils.getUserDetails(getActivity()).getLoginId());

        new RestClient().getApiService().getOrderList(jsonObject, new Callback<OrderModel>() {
            @Override
            public void success(OrderModel addressModel, Response response) {
                if(addressModel.getSuccess().equals("1")){
                    arrangeAdap(addressModel.getOrderList());
                    rl_noDataFound.setVisibility(View.GONE);
                }else{
                    rl_noDataFound.setVisibility(View.VISIBLE);
                    //Toast.makeText(getActivity(), addressModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void arrangeAdap(ArrayList<OrderList> deliveryLocationList){
        OrderAdapter adapter = new OrderAdapter(deliveryLocationList, this);
        rv_order.setAdapter(adapter);
    }

    @Override
    public void orderClickLisener(OrderList orderList) {
        Fragment fragment = new OrderDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("List", (Serializable) orderList);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.order_container ,fragment)
                .addToBackStack(null)
                .commit();
    }


}
