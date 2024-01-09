package com.example.hardapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;


import androidx.core.app.NotificationCompat;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String channelId = "notifications";
    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context);

        String title = intent.getStringExtra("title");
        String time = intent.getStringExtra("time");

        Notification notification = new NotificationCompat.Builder(context, channelId)
                .setContentTitle("Reminder: " + title)
                .setContentText("Time: " + time.substring(11))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .build();

        int id = title.hashCode();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

            if (notificationManager.getNotificationChannel(channelId) == null) {
                CharSequence name = "notifications_channel";
                String description = "for_notifications";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;

                NotificationChannel channel = new NotificationChannel(channelId, name, importance);
                channel.setDescription(description);

                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
