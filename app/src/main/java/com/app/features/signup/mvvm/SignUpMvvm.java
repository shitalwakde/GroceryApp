package com.app.features.signup.mvvm;

import android.content.Context;

import com.app.features.login.ModLogin;

public class SignUpMvvm {

    public interface SignUpView {
        public void onNameInvalid(String message);
        public void onEmailInvalid(String message);
        public void onMobileInvalid(String message);
        public void onPasswordInvalid(String message);
        public void onConfirmPasswordInvalid(String message);
        public void onSignUpSuccess(ModLogin loginModel);
        public void onSignUpFail(String message);
    }

    public interface SignUpPresenter{
        void onSignUpClicked(Context context, String name, String email, String mobile, String password, String confirmPassword);
        void onSignUpSuccess(ModLogin loginModel);
        void onSignUpFail(String message);
        void onViewDestroy();
    }

    public interface SignUpInteractor{
        void callApiForSignUp(String name, String email, String mobile, String password, SignUpPresenter presenter);
    }
}
