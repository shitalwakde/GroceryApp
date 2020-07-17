package com.app.features.login.mvvm;

import android.content.Context;

import com.app.features.login.ModLogin;
import com.app.util.AppUtils;

public class LoginPresenterImp implements LoginPresenter {
    LoginView loginView;
    LoginInteractor interactor;
    public LoginPresenterImp(LoginView loginView) {
        this.loginView = loginView;
        interactor=new LoginInteractorImp();
    }

    @Override
    public void onLoginClicked(String username, String password) {
        boolean isAllValid=true;
        if(AppUtils.isNullOrEmpty(username)){
            loginView.onUsernameEmpty();
            isAllValid=false;
        }else if(!(AppUtils.isValidEmail(loginView.getContext(),username) || AppUtils.isValidMobile(loginView.getContext(),username))){
            loginView.onUsernameInvalid();
            isAllValid=false;
        }
        if(!AppUtils.isValidPassword(loginView.getContext(),password)) {
            loginView.onPasswordInvalid();
            isAllValid=false;
        }
        if(isAllValid){
            interactor.callApiForLogin(username,password,this);
        }
    }

    @Override
    public void onViewDestroy() {
        loginView=null;
    }

    @Override
    public void onLoginSucess(ModLogin loginModel) {
        if(loginView!=null){
            loginView.onLoginSuccess(loginModel);
        }
    }

    @Override
    public void onLoginFail(String message) {
        if(loginView!=null){
            loginView.onLoginFail(message);
        }
    }
}
