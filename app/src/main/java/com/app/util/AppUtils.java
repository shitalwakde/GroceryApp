package com.app.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import com.app.callback.OnItemCountChanged;
import com.app.constant.AppConstant;
import com.app.controller.AppController;
import com.app.features.cart.CartAdapter;
import com.app.features.home.model.Product;
import com.app.features.login.ModLogin;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import androidx.recyclerview.widget.RecyclerView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class AppUtils {

    public static void showToast(Context context, String message) {
        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean isNullOrEmpty(String input) {
        return input == null || input.isEmpty();
    }

    public static boolean isValidUsername(Context context, String username) {
        return isValidUsername(context, username, "^[a-zA-Z0-9._-]{3,20}$");
    }

    public static boolean isValidUsername(Context context, String username, String regex) {
        if (isNullOrEmpty(username)) {
            showToast(context, "Please enter User name first.");
        } else if (!Pattern.matches(regex, username)) {
            showToast(context, "Please enter a valid User name.");
        } else {
            return true;
        }
        return false;
    }

    public static boolean isValidEmail(Context context, String email) {
        if (isNullOrEmpty(email)) {
            showToast(context, "Please enter Email first.");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast(context, "Please enter a valid Email address.");
        } else {
            return true;
        }
        return false;
    }

    public static boolean isValidMobile(Context context, String mobile) {
        return isValidMobile(context, mobile, "^[0-9]{10}$");
    }

    public static boolean isValidMobile(Context context, String mobile, String regex) {
        if (isNullOrEmpty(mobile)) {
            showToast(context, "Please enter Mobile number first.");
        } else if (!Pattern.matches(regex, mobile)) {
            showToast(context, "Please enter a valid Mobile number.");
        } else {
            return true;
        }
        return false;
    }

    public static boolean isValidPassword(Context context, String password) {
        if (isNullOrEmpty(password)) {
            showToast(context, "Please enter Password first.");
        } /*else if (password.length() < 6) {
            showToast(context, "Password length should not be less than 6 characters");
        } else if (password.length() > 30) {
            showToast(context, "Password length should not be greater than 30 characters");
        }*/ else {
            return true;
        }
        return false;
    }

    public static void setUserDetails(Context context,ModLogin loginModel) {
        if(loginModel!=null) {
            String userDetail = new Gson().toJson(loginModel);
            PrefUtil.getInstance(context).putData(AppConstant.PREF_USER_DATA, userDetail);
        }else {
            PrefUtil.getInstance(context).removeKeyData(AppConstant.PREF_USER_DATA);
        }
    }

    public static void updateUserDetails(Context context,ModLogin loginModel) {
        if(loginModel!=null) {
            String userDetail = new Gson().toJson(loginModel);
            PrefUtil.getInstance(context).putData(AppConstant.PREF_USER_DATA, userDetail);
        }else {
            PrefUtil.getInstance(context).removeKeyData(AppConstant.PREF_USER_DATA);
        }
    }

    public static ModLogin getUserDetails(Context context){
        String userDetail=PrefUtil.getInstance(AppController.getInstance()).getPreferences().getString(AppConstant.PREF_USER_DATA,null);
        ModLogin loginModel=null;
        if(userDetail!=null)
            loginModel=new Gson().fromJson(userDetail,ModLogin.class);
        return loginModel;
    }

    public static String getUniqueId(Context context){
        String uuid= PrefUtil.getInstance(context).getPreferences().getString(AppConstant.UUID,null);
        if(TextUtils.isEmpty(uuid)){
            UUID uniqueId=UUID.randomUUID();
            uuid=uniqueId.toString();
            PrefUtil.getInstance(context).putData(AppConstant.UUID,uuid);
        }
        return uuid;
    }


    public static void setCartCount(String cartCount){
        PrefUtil.getInstance(AppController.getInstance()).putData(AppConstant.PREF_CART_COUNT, cartCount);
    }


    public static String getCartCount(Context context){
        String cartCount = PrefUtil.getInstance(context).getPreferences().getString(AppConstant.PREF_CART_COUNT, null);
        return cartCount;
    }


    public static void addTocart(int qty, final int position, List<Product> mdata, RecyclerView.Adapter adapter, Context context, final OnItemCountChanged listener){
        JsonObject jsonObject = new JsonObject();
        if(AppConstant.isLogin(null)){
            jsonObject.addProperty("userId", AppUtils.getUserDetails(null).getLoginId());
        }else{
            jsonObject.addProperty("userId", "");
        }
        jsonObject.addProperty("tempUserId", AppController.getInstance().getUniqueID());
        //jsonObject.addProperty("tempUserId", "5478965545");
        jsonObject.addProperty("productId", mdata.get(position).getProductId());
        jsonObject.addProperty("quantity", String.valueOf(qty));

        new RestClient().getApiService().addToCart(jsonObject, new Callback<Product>() {
            @Override
            public void success(Product product, Response response) {
                mdata.get(position).setLoading(false);
                if(product.getSuccess().equals("1")){
                    if(product.getCartCount()!=null){
                        setCartCount(product.getCartCount());
                    }
                    listener.onSuccess();
//                    if(!product.getQuantity().equals("0")){
                        mdata.get(position).setCartQuantity(qty);
//                    }
                    adapter.notifyDataSetChanged();
                    adapter.notifyItemChanged(position);
//                        Toast.makeText(itemView.getContext(), product.getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, product.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mdata.get(position).setLoading(false);
                adapter.notifyItemChanged(position);
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static void addWishList(String wishList, final int position, List<Product> mdata, RecyclerView.Adapter adapter, Context context){
        JsonObject jsonObject = new JsonObject();
        if(AppConstant.isLogin(null)){
            jsonObject.addProperty("userId", AppUtils.getUserDetails(null).getLoginId());
        }else{
            jsonObject.addProperty("userId", "");
        }
        jsonObject.addProperty("tempUserId", AppController.getInstance().getUniqueID());
        jsonObject.addProperty("productId",mdata.get(position).getProductId());
        jsonObject.addProperty("wishList",wishList);

        new RestClient().getApiService().addWishList(jsonObject, new Callback<Product>() {
            @Override
            public void success(Product product, Response response) {
                if(product.getSuccess().equals("1")){
                    adapter.notifyDataSetChanged();
                    //Toast.makeText(context, product.getMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, product.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
