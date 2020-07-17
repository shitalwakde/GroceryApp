package com.app.util;

import com.app.activities.ModCategory;
import com.app.features.address.AddressModel;
import com.app.features.cart.Cart;
import com.app.features.home.model.HomeModel;
import com.app.features.home.model.Product;
import com.app.features.login.ModLogin;
import com.app.features.navmenu.WishList;
import com.app.features.order.OrderModel;
import com.app.features.productdetail.ProductDetailModel;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

public interface ApiInterface {

    @POST("/Api/Login")
    void getLogin(@Body JsonObject jsonObject, Callback<ModLogin> signUpModelCallback);

    @POST("/Api/signUp")
    void getSignUp(@Body JsonObject jsonObject, Callback<ModLogin> signUpModelCallback);

    @POST("/Api/verifyOtp")
    void getVerifyOtp(@Body JsonObject jsonObject, Callback<ModLogin> signUpModelCallback);

    @POST("/Api/reSendOtp")
    void getResendOtp(@Body JsonObject jsonObject, Callback<ModLogin> signUpModelCallback);

    @POST("/Profile/updateProfile")
    void updateProfile(@Body JsonObject jsonObject, Callback<ModLogin> signUpModelCallback);

    @POST("/Profile/changePass")
    void changePassword(@Body JsonObject jsonObject, Callback<ModLogin> signUpModelCallback);

    @POST("/Profile/updateProfile")
    void forgotPassword(@Body JsonObject jsonObject, Callback<ModLogin> signUpModelCallback);


    @POST("/Api/getCategories")
    void getCategories(@Body JsonObject jsonObject, Callback<ModCategory> signUpModelCallback);


    @POST("/Api/home")
    void home(@Body JsonObject jsonObject, Callback<HomeModel> signUpModelCallback);


    @POST("/Api/getProduct")
    void getProductCategory(@Body JsonObject jsonObject, Callback<HomeModel> signUpModelCallback);


    @POST("/Api/getProductsList")
    void getProductDetail(@Body JsonObject jsonObject, Callback<Product> signUpModelCallback);


    @POST("/Api/addToCart")
    void addToCart(@Body JsonObject jsonObject, Callback<Product> signUpModelCallback);


    @POST("/Api/addWishList")
    void addWishList(@Body JsonObject jsonObject, Callback<Product> signUpModelCallback);


    @POST("/Api/getCartList")
    void getCartList(@Body JsonObject jsonObject, Callback<Cart> signUpModelCallback);


    @POST("/Api/getWishList")
    void getWishList(@Body JsonObject jsonObject, Callback<WishList> signUpModelCallback);


    @POST("/Api/addDeliveryLocation")
    void addDeliveryLocation(@Body JsonObject jsonObject, Callback<AddressModel> signUpModelCallback);


    @POST("/Api/getDeliveryLocation")
    void getDeliveryLocation(@Body JsonObject jsonObject, Callback<AddressModel> signUpModelCallback);


    @POST("/Api/addToOrderSummery")
    void addToOrderSummery(@Body JsonObject jsonObject, Callback<AddressModel> signUpModelCallback);


    @POST("/Api/placeOrder")
    void placeOrder(@Body JsonObject jsonObject, Callback<AddressModel> signUpModelCallback);


    @POST("/Api/removeAndSetDeliveryLocation")
    void setDefaultRemove(@Body JsonObject jsonObject, Callback<AddressModel> signUpModelCallback);


    @POST("/Api/getOrderList")
    void getOrderList(@Body JsonObject jsonObject, Callback<OrderModel> signUpModelCallback);

    @POST("/Api/orderCancel")
    void orderCancel(@Body JsonObject jsonObject, Callback<OrderModel> signUpModelCallback);
}
