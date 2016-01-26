package com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hxuehh.com.R;


import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.develop.Su;


import java.util.Date;

/**
 * Created by suwg on 2015/5/25.
 */
public class NotifiationUtil {

    public static final String LinkInWifi="wifi_Link";

    public static void showNotification(Intent intent, String tickertext, String message, String keyID) {
        try {
            Notification notification = new Notification(R.mipmap.icon, tickertext, System.currentTimeMillis());
            notification.defaults = Notification.DEFAULT_ALL;
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            PendingIntent p = PendingIntent.getActivity(SuApplication.getInstance(), (int) new Date().getTime(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

//            notification.setLatestEventInfo(SuApplication.getInstance(), tickertext, message, p);
            NotificationManager mNotificationManager = ((NotificationManager) SuApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE));
            mNotificationManager.notify(keyID, 1024, notification);
        } catch (Exception e) {
            Su.log(e.getMessage());
        }
    }

    public static void cancel(String notiID) {
        NotificationManager mNotificationManager = ((NotificationManager) SuApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE));
        mNotificationManager.cancel(notiID, 1024);
    }

}
