package com.app.features.signup.mvvm;

import com.app.features.login.ModLogin;
import com.app.util.RestClient;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SignUpInteractorImp implements SignUpMvvm.SignUpInteractor {
    @Override
    public void callApiForSignUp(String name, String email, String mobile, String password, SignUpMvvm.SignUpPresenter presenter) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("mobile", mobile);
        jsonObject.addProperty("password", password);

        new RestClient().getApiService().getSignUp(jsonObject, new Callback<ModLogin>() {
            @Override
            public void success(ModLogin loginModel, Response response) {
                if(loginModel.getSuccess().equals("1")){
                    loginModel=new ModLogin();
                    loginModel.setEmail(email);
                    loginModel.setName(name);
                    loginModel.setMobile(mobile);
                    presenter.onSignUpSuccess(loginModel);
                }else{
                    presenter.onSignUpFail(loginModel.getMsg());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                presenter.onSignUpFail(error.getMessage());
            }
        });
    }
}
