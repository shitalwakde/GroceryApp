package com.app.features.login.mvvm;

public interface LoginPresenter {
    public void onLoginClicked(String username,String password);
    public void onLoginSucess(int userid,String username);
    public void onLoginFail(String message);

    void onViewDestroy();
}
