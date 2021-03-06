package com.app.features.navmenu;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.R;
import com.app.activities.MainActivity;
import com.app.callback.HomeClickLisener;
import com.app.callback.ProductListener;
import com.app.constant.AppConstant;
import com.app.controller.AppController;
import com.app.features.home.model.Category;
import com.app.features.home.model.Product;
import com.app.util.AppUtils;
import com.app.util.RestClient;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class WishListFragment extends Fragment {
    View rootView;
    RecyclerView rv_wish;
    List<Product> productList;
    ProductListener productListener;
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
        rootView = inflater.inflate(R.layout.wishlist_fragment, container, false);
        init(rootView);
        setAdapters();

        return rootView;
    }

    private void init(View rootView){
        productList = new ArrayList<>();
        rv_wish = (RecyclerView)rootView.findViewById(R.id.rv_wish);
        rl_noDataFound = (RelativeLayout)rootView.findViewById(R.id.rl_noDataFound);
        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
    }

    private void setAdapters(){

        getWishList();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof WishListActivity){
            productListener = (ProductListener) context;
        }
    }


    private void getWishList(){
        progressBar.setVisibility(View.VISIBLE);
        JsonObject jsonObject = new JsonObject();
        if(AppConstant.isLogin(getContext())){
            jsonObject.addProperty("userId", AppUtils.getUserDetails(getContext()).getLoginId());
        }else{
            jsonObject.addProperty("userId", "");
        }
        jsonObject.addProperty("tempUserId", AppController.getInstance().getUniqueID());
        //jsonObject.addProperty("tempUserId", "5478965545");

        new RestClient().getApiService().getWishList(jsonObject, new Callback<WishList>() {
            @Override
            public void success(WishList wishList, Response response) {
                progressBar.setVisibility(View.GONE);
                if(wishList.getSuccess().equals("1")){
                    AppUtils.setCartCount(wishList.getCartCount());
                    ((WishListActivity)getContext()).setCartCount();
                    arrangeWishAdap(wishList.getWishList());
                    rl_noDataFound.setVisibility(View.GONE);
                }else {
                    rl_noDataFound.setVisibility(View.VISIBLE);
                    //Toast.makeText(getActivity(), wishList.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void arrangeWishAdap(ArrayList<Product> wishList) {
        WishListAdapter adapter1 = new WishListAdapter(productListener, wishList,this);
        rv_wish.setAdapter(adapter1);
    }
}
