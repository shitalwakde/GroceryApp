package com.app.features.login.mvvm;

import android.content.Context;

public interface LoginView {
    public void onUsernameInvalid();
    public void onPasswordInvalid();
    public void onLoginSuccess(int userId,String name);
    public void onLoginFail(String message);

    Context getContext();
}
