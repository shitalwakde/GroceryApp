package com.app.features.otp.mvvm;

import android.content.Context;
import android.util.Log;

import com.app.features.login.ModLogin;
import com.app.util.AppUtils;
import com.app.util.RestClient;
import com.google.gson.JsonObject;
import com.google.gson.annotations.JsonAdapter;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class OtpInteractorImp implements OtpMvvm.OtpInteractor {


    @Override
    public void callApiForOtp(Context context, String otp, OtpMvvm.OtpPresenter presenter) {
        JsonObject jsonObject = new JsonObject();
        if(AppUtils.getUserDetails(context).getEmail() == null){
            jsonObject.addProperty("mobileEmail", AppUtils.getUserDetails(context).getMobile());
        }else {
            jsonObject.addProperty("mobileEmail", AppUtils.getUserDetails(context).getEmail());
        }
        jsonObject.addProperty("otp", otp);

        Log.w("TAG", "mobileEmail : "+ AppUtils.getUserDetails(context).getEmail());

        new RestClient().getApiService().getVerifyOtp(jsonObject, new Callback<ModLogin>() {
            @Override
            public void success(ModLogin loginModel, Response response) {
                if(loginModel.getSuccess().equals("1")){
                    presenter.onOtpSuccess(loginModel);
                }else{
                    presenter.onOtpFail(loginModel.getMsg());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                presenter.onOtpFail(error.getMessage());
            }
        });
    }
}
