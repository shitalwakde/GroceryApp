package com.app.util;

import com.app.features.login.ModLogin;
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
}
