package com.app.features.signup.mvvm;

import android.content.Context;

import com.app.features.login.ModLogin;
import com.app.features.signup.SignupFragment;
import com.app.util.AppUtils;

public class SignUpPresenterImp implements SignUpMvvm.SignUpPresenter {
    SignUpMvvm.SignUpView signUpView;
    SignUpMvvm.SignUpInteractor interactor;

    public SignUpPresenterImp(SignUpMvvm.SignUpView signUpView) {
        this.signUpView = signUpView;
        interactor = new SignUpInteractorImp();
    }

    @Override
    public void onSignUpClicked(Context context, String name, String email, String mobile, String password, String confirmPassword) {
         boolean isAllValid=true;
         if(AppUtils.isNullOrEmpty(name)){
             signUpView.onNameInvalid("Please Enter Full Name");
             isAllValid=false;
         }
         if(AppUtils.isNullOrEmpty(email)) {
             signUpView.onEmailInvalid("Please Enter Email Address");
             isAllValid=false;
         }else if(!AppUtils.isValidEmail(context, email)){
             signUpView.onEmailInvalid("Please Enter Valid Email Address");
             isAllValid=false;
         }

         if(AppUtils.isNullOrEmpty(mobile)){
             signUpView.onMobileInvalid("Please Enter Mobile Number");
             isAllValid=false;
         }else if(!AppUtils.isValidMobile(context,mobile)){
             signUpView.onMobileInvalid("Please Enter Valid Mobile Number");
             isAllValid=false;
         }

         if(AppUtils.isNullOrEmpty(password)){
             signUpView.onPasswordInvalid("Please Enter Password");
             isAllValid=false;
         }

         if(AppUtils.isNullOrEmpty(confirmPassword)){
             signUpView.onConfirmPasswordInvalid("Please Enter Confirm Password");
             isAllValid=false;
         }

         if(!AppUtils.isNullOrEmpty(password) && !AppUtils.isNullOrEmpty(confirmPassword) && !password.equals(confirmPassword)){
             signUpView.onPasswordInvalid("Password does not match");
             isAllValid=false;
         }

         if (isAllValid){
             interactor.callApiForSignUp(name, email, mobile, password, this);
         }
    }

    @Override
    public void onSignUpSuccess(ModLogin loginModel) {
        if(signUpView != null){
            signUpView.onSignUpSuccess(loginModel);
        }
    }

    @Override
    public void onSignUpFail(String message) {
        if(signUpView != null){
            signUpView.onSignUpFail(message);
        }
    }

    @Override
    public void onViewDestroy() {
        signUpView = null;
    }
}
