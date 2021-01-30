package com.app.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.app.R;
import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationDisplayedResult;
import com.onesignal.OSNotificationReceivedResult;

import java.math.BigInteger;

import androidx.core.app.NotificationCompat;

public class MyNotificationExtenderService extends NotificationExtenderService {

    @Override
    protected boolean onNotificationProcessing(final OSNotificationReceivedResult notification) {
        OverrideSettings overrideSettings = new OverrideSettings();
        final Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.logo);

        //Define sound URI
        final Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        overrideSettings.extender = new NotificationCompat.Extender() {
            @Override
            public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    return builder
                            .setColor(new BigInteger("e47e1c", 16).intValue())
                            .setLargeIcon(icon)
                            .setSound(soundUri)
                            ;
                    // notification.setColor(getResources().getColor(R.color.notification_color));
                } else {
                    return builder
                            .setColor(new BigInteger("e47e1c", 16).intValue())
                            .setSmallIcon(R.drawable.logo)
                            .setLargeIcon(icon)
                            .setSound(soundUri)
                            ;
                }
            }
        };
        notification.payload.sound=null;
        OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);
        Log.w("OneSignalExample", "Notification displayed with id: " + displayedResult.androidNotificationId);

        return true;
    }

}
