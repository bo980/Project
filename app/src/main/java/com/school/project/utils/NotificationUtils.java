package com.school.project.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class NotificationUtils extends ContextWrapper {

    private NotificationManager manager;
    public static final String id = "channel_ch";
    public static final String name = "channel_name_ch";

    public NotificationUtils(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setSound(null,null);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationCompat.Builder getChannelNotification() {
        createNotificationChannel();
        return new NotificationCompat.Builder(getApplicationContext(), id);
    }

    public NotificationCompat.Builder getNotification() {
        return new NotificationCompat.Builder(getApplicationContext());
    }


    public NotificationCompat.Builder getNotificationBuilder() {
        //判断是否是8.0Android.O
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return getChannelNotification();
        } else {
            return getNotification();
        }
    }

}