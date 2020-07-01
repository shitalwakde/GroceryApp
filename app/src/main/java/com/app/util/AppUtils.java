package com.app.util;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

import com.app.constant.AppConstant;
import com.app.features.login.ModLogin;
import com.google.gson.Gson;

import java.util.regex.Pattern;

public class AppUtils {

    public static void showToast(Context context, String message) {
        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean isNullOrEmpty(String input) {
        return input == null || input.isEmpty();
    }

    public static boolean isValidUsername(Context context, String username) {
        return isValidUsername(context, username, "^[a-zA-Z0-9._-]{3,20}$");
    }

    public static boolean isValidUsername(Context context, String username, String regex) {
        if (isNullOrEmpty(username)) {
            showToast(context, "Please enter User name first.");
        } else if (!Pattern.matches(regex, username)) {
            showToast(context, "Please enter a valid User name.");
        } else {
            return true;
        }
        return false;
    }

    public static boolean isValidEmail(Context context, String email) {
        if (isNullOrEmpty(email)) {
            showToast(context, "Please enter Email first.");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast(context, "Please enter a valid Email address.");
        } else {
            return true;
        }
        return false;
    }

    public static boolean isValidMobile(Context context, String mobile) {
        return isValidMobile(context, mobile, "^[0-9]{10}$");
    }

    public static boolean isValidMobile(Context context, String mobile, String regex) {
        if (isNullOrEmpty(mobile)) {
            showToast(context, "Please enter Mobile number first.");
        } else if (!Pattern.matches(regex, mobile)) {
            showToast(context, "Please enter a valid Mobile number.");
        } else {
            return true;
        }
        return false;
    }

    public static boolean isValidPassword(Context context, String password) {
        if (isNullOrEmpty(password)) {
            showToast(context, "Please enter Password first.");
        } /*else if (password.length() < 6) {
            showToast(context, "Password length should not be less than 6 characters");
        } else if (password.length() > 30) {
            showToast(context, "Password length should not be greater than 30 characters");
        }*/ else {
            return true;
        }
        return false;
    }

    public static void setUserDetails(Context context,ModLogin loginModel) {
        if(loginModel!=null) {
            String userDetail = new Gson().toJson(loginModel);
            PrefUtil.getInstance(context).putData(AppConstant.PREF_USER_DATA, userDetail);
        }else {
            PrefUtil.getInstance(context).removeKeyData(AppConstant.PREF_USER_DATA);
        }
    }

    public static ModLogin getUserDetails(Context context){
        String userDetail=PrefUtil.getInstance(context).getPreferences().getString(AppConstant.PREF_USER_DATA,null);
        ModLogin loginModel=null;
        if(userDetail!=null)
            loginModel=new Gson().fromJson(userDetail,ModLogin.class);
        return loginModel;
    }
}
