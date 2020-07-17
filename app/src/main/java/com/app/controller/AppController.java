package com.app.controller;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.app.activities.MainActivity;
import com.app.util.AppUtils;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import java.util.UUID;

public class AppController extends Application {

    public static String  uniqueID = UUID.randomUUID().toString();
    private static AppController controller;
    String app_id = "";
    String player_id = "";

    @Override
    public void onCreate() {
        super.onCreate();
        controller=this;

        OneSignal.startInit(this)
                .autoPromptLocation(false)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.None)
                .setNotificationOpenedHandler(new MyNotificationOpenedHandler())
                .setNotificationReceivedHandler(new MyNotificationReceivedHandler())
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        OneSignal.enableSound(true);

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                app_id = userId;
                player_id = registrationId;
                Log.w("TAG", "login app_id ***====>>>   " + app_id);
                Log.w("TAG", "login player_id =====>>>>  " + player_id);
            }
        });

    }

    public static AppController getInstance() {
        return controller;
    }

    public String getUniqueID(){
        return AppUtils.getUniqueId(this);
    }


    private class MyNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            //JSONObject object = result.notification.payload.additionalData;
            Intent intent=new Intent(AppController.this, MainActivity.class);
//            intent.putExtra("flag", "notify");
//            intent.putExtra("order", "approve");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }


    private class MyNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
        @Override
        public void notificationReceived(OSNotification notification) {
            Log.w("TAG","Recieved "+notification);
        }
    }


}
