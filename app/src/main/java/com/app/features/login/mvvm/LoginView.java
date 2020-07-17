package com.app.features.login.mvvm;

import android.content.Context;

import com.app.features.login.ModLogin;

public interface LoginView {
           void onUsernameEmpty();
    public void onUsernameInvalid();
    public void onPasswordInvalid();
    public void onLoginSuccess(ModLogin loginModel);
    public void onLoginFail(String message);

    Context getContext();
}
