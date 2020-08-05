package com.app.features.cart;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;
import com.app.callback.CalculateLisener;
import com.app.callback.ProductListener;
import com.app.constant.AppConstant;
import com.app.controller.AppController;
import com.app.features.address.AddressActivity;
import com.app.features.address.AddressFragment;
import com.app.features.address.AddressListFragment;
import com.app.features.address.AddressModel;

import com.app.features.home.model.Product;
import com.app.features.login.LoginActivity;
import com.app.util.AppUtils;
import com.app.util.RestClient;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.app.features.cart.CartActivity.cartContainer;
import static com.app.features.cart.CartActivity.tv_toolbar_cart;

public class CartFragment extends Fragment implements CalculateLisener {

    View rootView;
    RecyclerView rv_cart;
    List<Product> productList;
    ProductListener productListener;
    TextView tv_checkout, tv_total_price;
    FragmentManager fragmentManager;
    RelativeLayout rl_noDataFound;
    ProgressBar progressBar;

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
        setAdapters();

        return rootView;
    }


    private void init(View rootView){
        fragmentManager = getActivity().getSupportFragmentManager();
        productList = new ArrayList<>();
        rv_cart = (RecyclerView)rootView.findViewById(R.id.rv_cart);
        tv_checkout = (TextView)rootView.findViewById(R.id.tv_checkout);
        tv_total_price = (TextView)rootView.findViewById(R.id.tv_total_price);
        rl_noDataFound = (RelativeLayout)rootView.findViewById(R.id.rl_noDataFound);
        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
    }

    private void setAdapters(){
        getCartList();
        tv_toolbar_cart.setText("My Cart");
    }


    private void getCartList(){
        progressBar.setVisibility(View.VISIBLE);
        JsonObject jsonObject = new JsonObject();
        if(AppConstant.isLogin(getContext())){
            jsonObject.addProperty("userId", AppUtils.getUserDetails(getContext()).getLoginId());
        }else{
            jsonObject.addProperty("userId", "");
        }
        jsonObject.addProperty("tempUserId", AppController.getInstance().getUniqueID());

        new RestClient().getApiService().getCartList(jsonObject, new Callback<Cart>() {
            @Override
            public void success(Cart cartModel, Response response) {
                progressBar.setVisibility(View.GONE);
                if(cartModel.getSuccess().equals("1")){
                    tv_total_price.setText("PRICE : \u20B9 "+cartModel.getTotalSum());
                    arrangeCartAdap(cartModel.getCart());
                    rl_noDataFound.setVisibility(View.GONE);
                    tv_checkout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(cartModel.getCart().size() == 0){
                                Toast.makeText(getActivity(), "Cart should not be empty", Toast.LENGTH_SHORT).show();
                            }else{
                                if(AppConstant.isLogin(getContext())){
                                    getDeliveryLocation();
                                }else {
                                    AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                                    builder.setMessage("Please login to place order");
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(getContext(), LoginActivity.class);
                                            intent.putExtra("type","login");
                                            getContext().startActivity(intent);
                                            dialog.cancel();
                                        }
                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                                }
                            }
                        }
                    });
                }else{
                    rl_noDataFound.setVisibility(View.VISIBLE);
                    //Toast.makeText(getActivity(), cartModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void getDeliveryLocation(){
        JsonObject jsonObject = new JsonObject();
        if(AppConstant.isLogin(getContext())){
            jsonObject.addProperty("userId", AppUtils.getUserDetails(getContext()).getLoginId());
        }else{
            jsonObject.addProperty("userId", "");
        }
        jsonObject.addProperty("tempUserId", AppController.getInstance().getUniqueID());

        new RestClient().getApiService().getDeliveryLocation(jsonObject, new Callback<AddressModel>() {
            @Override
            public void success(AddressModel addressModel, Response response) {
                if(addressModel.getSuccess().equals("1")){
                    if(addressModel.getDeliveryLocationList().size() == 0){
                        //fragmentManager.beginTransaction().replace(cartContainer, new AddressFragment()).addToBackStack(null).commit();
                    }else{
                        Intent intent = new Intent(getActivity(), AddressActivity.class);
                        intent.putExtra("address", "checkout");
                        intent.putExtra("deliveryId", "");
                        intent.putExtra("go", "");
                        startActivity(intent);
                        //fragmentManager.beginTransaction().replace(cartContainer, new CheckOutFragment("")).addToBackStack(null).commit();
                    }
                }else{
                    Intent intent = new Intent(getActivity(), AddressActivity.class);
                    intent.putExtra("address", "add");
                    intent.putExtra("deliveryId", "");
                    intent.putExtra("go", "");
                    startActivity(intent);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void arrangeCartAdap(ArrayList<Product> cart) {
        CartAdapter adapter1 = new CartAdapter(productListener, cart, rl_noDataFound, this);
        rv_cart.setAdapter(adapter1);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof CartActivity){
            productListener = (ProductListener) context;
        }
    }


    private void calculateAmountPlus(List<Product> product) {
        float amount = 0.0f;
        float quantity = 0.0f;
        float calculateAmountPlus = 0.0f;
        for (int i=0; i<product.size(); i++){
            amount = Float.parseFloat(product.get(i).getProductCartAmount());
            //quantity = Float.parseFloat(product.get(i).getCartQuantity());
            //calculateAmountPlus += (amount * quantity);
            calculateAmountPlus += (amount);
        }

        //tv_total_price.setText("PRICE : \u20B9 " + String.format("%.2f", calculateAmountPlus));
        tv_total_price.setText("PRICE : \u20B9 " + Math.round(calculateAmountPlus));
    }


    @Override
    public void calculatePlus(List<Product> product) {
        calculateAmountPlus(product);
    }

}
