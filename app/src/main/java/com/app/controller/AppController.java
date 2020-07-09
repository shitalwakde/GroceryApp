package com.app.controller;

import android.app.Application;

import com.app.util.AppUtils;

import java.util.UUID;

public class AppController extends Application {

    public static String  uniqueID = UUID.randomUUID().toString();
    private static AppController controller;

    @Override
    public void onCreate() {
        super.onCreate();
        controller=this;
    }

    public static AppController getInstance() {
        return controller;
    }

    public String getUniqueID(){
        return AppUtils.getUniqueId(this);
    }
}
