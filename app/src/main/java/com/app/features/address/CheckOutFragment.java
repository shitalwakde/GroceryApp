package com.app.features.address;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;
import com.app.activities.MainActivity;
import com.app.util.AppUtils;
import com.app.util.RestClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.app.features.cart.CartActivity.tv_toolbar_cart;

public class CheckOutFragment extends Fragment {

    View rootView;
    TextView tv_order, tv_change;
    FragmentManager fragmentManager;
    ArrayList<String> cartStringList = new ArrayList<>();
    String isWallet="";

    String deliveryId;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.cityPincode)
    TextView cityPincode;
    @BindView(R.id.tv_change)
    TextView tvChange;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_deliveryCharge)
    TextView tvDeliveryCharge;
    @BindView(R.id.tv_payable_amount)
    TextView tvPayableAmount;
    @BindView(R.id.rbCashOnDelivery)
    RadioButton rbCashOnDelivery;
    @BindView(R.id.tv_total_payable_amount)
    TextView tvTotalPayableAmount;
    @BindView(R.id.tv_continue)
    TextView tvContinue;
    @BindView(R.id.tv_order)
    TextView tvOrder;
    @BindView(R.id.bottom_button)
    LinearLayout bottomButton;
    @BindView(R.id.tv_gst)
    TextView tvGst;
    @BindView(R.id.tv_slot)
    TextView tvSlot;
    @BindView(R.id.rl_slot)
    RelativeLayout rlSlot;
    @BindView(R.id.cbWallet)
    CheckBox cbWallet;
    @BindView(R.id.tv_wallet)
    TextView tvWallet;
    @BindView(R.id.ll_wallet)
    LinearLayout llWallet;

    public CheckOutFragment(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.checkout_fragment, container, false);
        ButterKnife.bind(this, rootView);
        init(rootView);
        click();

        return rootView;
    }

    private void init(View rootView) {
        fragmentManager = getActivity().getSupportFragmentManager();
        tv_order = (TextView) rootView.findViewById(R.id.tv_order);
        tv_change = (TextView) rootView.findViewById(R.id.tv_change);
    }

    private void click() {
        tv_toolbar_cart.setText("Checkout");
        addToOrderSummery();
        tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddressActivity.class);
                intent.putExtra("address", "list");
                intent.putExtra("deliveryId", "");
                intent.putExtra("go", "");
                startActivity(intent);
                getActivity().finish();
//                fragmentManager.beginTransaction().replace(cartContainer, new AddressListFragment()).addToBackStack(null).commit();
            }
        });

        rlSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSlotDialog();
            }
        });

    }


    public void showSlotDialog() {
        final Dialog dialog1 = new Dialog(getActivity());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(true);
        dialog1.setContentView(R.layout.dlg_slot);
        TextView tv_slot = (TextView) dialog1.findViewById(R.id.tv_slot);

        tv_slot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        dialog1.show();
    }


    public void showRatingDialog(String orderId) {
        final Dialog dialog1 = new Dialog(getActivity());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(true);
        dialog1.setContentView(R.layout.dlg_thankyou);
        TextView tv_submit = (TextView) dialog1.findViewById(R.id.tv_submit);
        TextView tv_message = (TextView) dialog1.findViewById(R.id.tv_message);

        tv_message.setText("Your order No. is " + orderId + " & Your order will be delivered within 2-3 days");

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finishAffinity();
            }
        });

        dialog1.show();
    }


    private void addToOrderSummery() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", AppUtils.getUserDetails(getActivity()).getLoginId());
        jsonObject.addProperty("deliveryId", deliveryId);

        new RestClient().getApiService().addToOrderSummery(jsonObject, new Callback<AddressModel>() {
            @Override
            public void success(AddressModel addressModel, Response response) {
                if (addressModel.getSuccess().equals("1")) {
                    String cartId = "";
                    for (int i = 0; i < addressModel.getCartList().size(); i++) {
                        cartId = addressModel.getCartList().get(i).getCardId();
                        cartStringList.add(cartId);
//                        if (cartStringList.contains(cartId)) {
//                            cartStringList.set(i, cartId);
//                        } else {
//                            cartStringList.add(cartId);
//                        }
                    }
                    setOrderDetail(addressModel, cartStringList);
                    //Toast.makeText(getActivity(), addressModel.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), addressModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setOrderDetail(AddressModel addressModel, ArrayList<String> cartStringList) {
        tvName.setText(addressModel.getName());
        tvMobile.setText(addressModel.getMobile());
        tvAddress.setText(addressModel.getHouseNo() + ", " + addressModel.getArea() + ", " + addressModel.getLandmark() + ",");
        cityPincode.setText(addressModel.getCity() + " " + addressModel.getPincode() + ".");
        tvPrice.setText("Price " + "(" + addressModel.getCartCount() + " items)");
        tvPayableAmount.setText("\u20B9 " + addressModel.getTotalFinalAmount());
        tvTotalPayableAmount.setText("PRICE : \u20B9 " + addressModel.getTotalFinalAmount());
        tvTotalPrice.setText("\u20B9 " + addressModel.getTotalSum());

        if (addressModel.getDeliveryCharges().equals("0")) {
            tvDeliveryCharge.setText("FREE");
        } else {
            tvDeliveryCharge.setText("\u20B9 " + addressModel.getDeliveryCharges());
        }

        if (addressModel.getGst().equals("0")) {
            tvGst.setText("-  ");
        } else {
            tvGst.setText("\u20B9 " + addressModel.getGst());
        }

        tv_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder(addressModel, cartStringList);
            }
        });

    }


    private void placeOrder(AddressModel addressModel, ArrayList<String> cartStringList) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", AppUtils.getUserDetails(getActivity()).getLoginId());
        jsonObject.addProperty("deliveryId", addressModel.getDeliveryId());
        jsonObject.addProperty("totalFinalAmount", addressModel.getTotalFinalAmount());
        jsonObject.addProperty("paymentType", "COD");
        if(cbWallet.isChecked()){
            isWallet = "Yes";
        }else{
            isWallet = "No";
        }
        Gson gsonAssignee = new GsonBuilder().create();
        JsonArray catList = gsonAssignee.toJsonTree(cartStringList).getAsJsonArray();
        jsonObject.add("cardIds", catList);

        new RestClient().getApiService().placeOrder(jsonObject, new Callback<AddressModel>() {
            @Override
            public void success(AddressModel addressModel, Response response) {
                if (addressModel.getSuccess().equals("1")) {
                    showRatingDialog(addressModel.getOrderId());
                } else {
                    Toast.makeText(getActivity(), addressModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
