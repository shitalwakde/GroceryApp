package com.app.features.otp.mvvm;

import android.content.Context;

import com.app.features.login.ModLogin;

public class OtpMvvm {

    public interface OtpView {
        void onOtpInvalid(String message);
        void onOtpSuccess(ModLogin loginModel);
        void onOtpFail(String message);
    }


    public interface OtpPresenter{
        void onVerifyClicked(Context context, String otp);
        void onOtpSuccess(ModLogin loginModel);
        void onOtpFail(String message);
        void onViewDestroy();
    }

    public interface OtpInteractor{
        void callApiForOtp(Context context, String otp, OtpPresenter presenter);
    }
}
