package com.app.features.login.mvvm;

import android.content.Context;

public interface LoginInteractor {
    void callApiForLogin(String username, String password, LoginPresenter presenter);
}
