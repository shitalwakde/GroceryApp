package com.app.features.address;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;
import com.app.callback.AddressListLisener;
import com.app.constant.AppConstant;
import com.app.controller.AppController;
import com.app.features.checkout.CheckOutFragment;
import com.app.util.AppUtils;
import com.app.util.RestClient;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.app.features.cart.CartActivity.cartContainer;
import static com.app.features.cart.CartActivity.tv_toolbar_cart;

public class AddressListFragment extends Fragment implements AddressListLisener {

    View rootView;
    TextView tv_deliver;
    CardView card_new_addr;
    RecyclerView rv_addressList;
    FragmentManager fragmentManager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.address_list_fragment, container, false);
        init(rootView);
        click();
        return rootView;
    }


    private void init(View rootView){
        fragmentManager = getActivity().getSupportFragmentManager();
        tv_deliver = (TextView)rootView.findViewById(R.id.tv_deliver);
        card_new_addr = (CardView)rootView.findViewById(R.id.card_new_addr);
        rv_addressList = (RecyclerView)rootView.findViewById(R.id.rv_addressList);
    }

    private void click(){
        tv_toolbar_cart.setText("Address List");
        getDeliveryLocation();

        card_new_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddressActivity.class);
                intent.putExtra("address", "add");
                intent.putExtra("deliveryId", "");
                startActivity(intent);
                getActivity().finish();
//                fragmentManager.beginTransaction().replace(cartContainer, new AddressFragment()).addToBackStack(null).commit();
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
                    arrangaddress(addressModel.getDeliveryLocationList());
                }else{
                    Toast.makeText(getActivity(), addressModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void arrangaddress(ArrayList<AddressModel> deliveryLocationList){
        AddressListAdapter adapter = new AddressListAdapter(deliveryLocationList, this);
        rv_addressList.setAdapter(adapter);
    }

    @Override
    public void AddressListLisenerClick(String deliveryId) {
        //fragmentManager.beginTransaction().replace(R.id.address_container, new CheckOutFragment(deliveryId)).commit();
        Intent intent = new Intent(getActivity(), AddressActivity.class);
        intent.putExtra("address", "checkout");
        intent.putExtra("deliveryId", deliveryId);
        startActivity(intent);
        getActivity().finish();
    }


}
