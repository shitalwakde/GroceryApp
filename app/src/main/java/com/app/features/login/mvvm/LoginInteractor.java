package com.app.features.login.mvvm;

public interface LoginInteractor {
    void callApiForLogin(String username, String password, LoginPresenter presenter);
}
