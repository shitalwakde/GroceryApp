package com.app.features.login.mvvm;

import android.content.Context;

import com.app.features.login.ModLogin;

public interface LoginPresenter {
    public void onLoginClicked(String username, String password);
    public void onLoginSucess(ModLogin loginModel);
    public void onLoginFail(String message);

    void onViewDestroy();
}
