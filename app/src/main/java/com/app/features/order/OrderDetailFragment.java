package com.app.features.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;
import com.app.features.cart.CartActivity;
import com.app.util.RestClient;
import com.google.gson.JsonObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.app.features.order.OrderActivity.tv_toolbar_order;

public class OrderDetailFragment extends Fragment {

    View rootView;
    TextView tv_continue;
    RecyclerView rv_order_view;
    OrderList orderList;
    @BindView(R.id.tv_orderAmount)
    TextView tvOrderAmount;
    @BindView(R.id.tv_deliveryDate)
    TextView tvDeliveryDate;
    @BindView(R.id.tv_orderStatus)
    TextView tvOrderStatus;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_orderDate)
    TextView tvOrderDate;
    @BindView(R.id.tv_purchaseAmount)
    TextView tvPurchaseAmount;
    @BindView(R.id.tv_deliveryCharge)
    TextView tvDeliveryCharge;
    @BindView(R.id.tv_gst)
    TextView tvGst;
    @BindView(R.id.tv_totalFinalAmount)
    TextView tvTotalFinalAmount;
    @BindView(R.id.tv_paymentStatus)
    TextView tvPaymentStatus;
    @BindView(R.id.rv_order_view)
    RecyclerView rvOrderView;
    @BindView(R.id.tv_deliveryDateTop)
    TextView tvDeliveryDateTop;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_reorder)
    TextView tvReorder;
    @BindView(R.id.tv_delivery)
    TextView tvDelivery;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.order_detail_fragment, container, false);
        ButterKnife.bind(this, rootView);
        init(rootView);
        setDetail();

        return rootView;
    }

    private void init(View rootView) {
        if (getArguments() != null) {
            orderList = (OrderList) getArguments().getSerializable("List");
        }
        rv_order_view = (RecyclerView) rootView.findViewById(R.id.rv_order_view);
    }


    private void setDetail() {
        tv_toolbar_order.setText("My Order Detail");

        tvOrderAmount.setText("\u20B9 " + orderList.getOrderAmount());
        Log.w("TAG", "order amount in orderList : "+orderList.getOrderAmount());
        tvDeliveryDate.setText(orderList.getOrderDate());
        tvDeliveryDateTop.setText(orderList.getOrderDate());
        tvOrderDate.setText(orderList.getOrderDate());
        if (orderList.getOrderStatus().equals("Pending")) {
            tvOrderStatus.setTextColor(Color.parseColor("#f47443"));
        } else if (orderList.getOrderStatus().equals("Cancelled")) {
            tvOrderStatus.setTextColor(Color.parseColor("#E72408"));
        } else {
            tvOrderStatus.setTextColor(Color.parseColor("#4d8603"));
        }
        tvOrderStatus.setText(orderList.getOrderStatus());
        if (orderList.getDeliveryName().equals("")) {
            tvDelivery.setVisibility(View.GONE);
            tvAddress.setVisibility(View.GONE);
        } else{
            tvDelivery.setVisibility(View.VISIBLE);
            tvAddress.setVisibility(View.VISIBLE);
            tvAddress.setText(orderList.getDeliveryName()+ ", " + orderList.getDeliveryHouseNo() + ", " +
                    orderList.getDeliveryArea() + ", " + orderList.getDeliveryState() + ", " + orderList.getDeliveryCity() + ", " + orderList.getDeliveryPincode());
        }
        tvPurchaseAmount.setText("\u20B9 " + orderList.getOrderAmount());

        if (orderList.getDeliveryCharges().equals("0")) {
            tvDeliveryCharge.setText("FREE");
        } else {
            tvDeliveryCharge.setText("\u20B9 " + orderList.getDeliveryCharges());
        }

        if (orderList.getDeliveryCharges().equals("0")) {
            tvGst.setText("-  ");
        } else {
            tvGst.setText("\u20B9 " + orderList.getDeliveryCharges());
        }
        tvTotalFinalAmount.setText("\u20B9 " + orderList.getOrderAmount());
        tvPaymentStatus.setText(orderList.getPaymentType());

        OrderDetailAdapter adapter1 = new OrderDetailAdapter(orderList.getOrderDetail());
        rv_order_view.setAdapter(adapter1);


        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to cancel this order ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelOrder();
                        dialog.dismiss();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });

        tvReorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reOrder();
//                Intent intent = new Intent(getContext(), CartActivity.class);
//                getContext().startActivity(intent);
            }
        });
    }

    private void cancelOrder() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("orderId", orderList.getOrderId());

        new RestClient().getApiService().orderCancel(jsonObject, new Callback<OrderModel>() {
            @Override
            public void success(OrderModel orderModel, Response response) {
                if (orderModel.getSuccess().equals("1")) {
                    getActivity().onBackPressed();
//                    mdata.remove(getAdapterPosition());
//                    notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), orderModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void reOrder(){

    }

}
