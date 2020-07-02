package com.app.features.otp.mvvm;

import android.content.Context;

import com.app.features.login.ModLogin;
import com.app.util.AppUtils;

public class OtpPresenterImp implements OtpMvvm.OtpPresenter {
    OtpMvvm.OtpView otpView;
    OtpMvvm.OtpInteractor interactor;

    public OtpPresenterImp(OtpMvvm.OtpView otpView) {
        this.otpView = otpView;
        interactor = new OtpInteractorImp();
    }

    @Override
    public void onVerifyClicked(Context context, String otp) {
        boolean isValidAll=true;

        if(AppUtils.isNullOrEmpty(otp)){
            otpView.onOtpInvalid("Please Enter Otp");
            isValidAll=false;
        }

        if(isValidAll){
            interactor.callApiForOtp(context, otp, this);
        }
    }

    @Override
    public void onOtpSuccess(ModLogin loginModel) {
        if(otpView != null){
            otpView.onOtpSuccess(loginModel);
        }
    }

    @Override
    public void onOtpFail(String message) {
        if(otpView != null){
            otpView.onOtpFail(message);
        }
    }

    @Override
    public void onViewDestroy() {
        otpView = null;
    }
}
