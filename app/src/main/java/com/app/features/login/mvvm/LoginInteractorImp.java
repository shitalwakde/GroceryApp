package com.app.features.login.mvvm;

import android.widget.Toast;

import com.app.features.login.ModLogin;
import com.app.util.RestClient;
import com.app.util.Utility;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginInteractorImp implements LoginInteractor {

    @Override
    public void callApiForLogin(String username, String password, final LoginPresenter presenter) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("emailMobile", username);
        jsonObject.addProperty("password", password);

        new RestClient().getApiService().getLogin(jsonObject, new Callback<ModLogin>() {
            @Override
            public void success(ModLogin modLogin, Response response) {
                if(modLogin.getSuccess().equals("1")){
                    presenter.onLoginSucess(modLogin);
                }else{
                    presenter.onLoginFail(modLogin.getMsg());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                presenter.onLoginFail(error.getMessage());
            }
        });
        
    }

}
