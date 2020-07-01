package com.app.features.login.mvvm;

public class LoginInteractorImp implements LoginInteractor {



    @Override
    public void callApiForLogin(String username, String password, LoginPresenter presenter) {
        presenter.onLoginSucess(123,"swapnil");
    }

}
