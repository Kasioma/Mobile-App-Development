package com.example.hardapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.time.ZoneId;

public class AndroidAlarmScheduler implements AlarmScheduler{
    private Context context;
    AlarmManager alarmManager;
    public AndroidAlarmScheduler(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }


    @Override
    public void schedule(AlarmItem item) {
        Intent i = new Intent(context, AlarmReceiver.class);
        i.putExtra("title", item.getTitle());
        i.putExtra("time", item.getTime().toString());
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                item.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
                PendingIntent.getBroadcast(
                        context,
                        item.getId(),
                        i,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                )
                );
    }

    @Override
    public void cancel(AlarmItem item) {
        alarmManager.cancel(
                PendingIntent.getBroadcast(
                        context,
                        item.getId(),
                        new Intent(context, AlarmReceiver.class),
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                )
        );
    }

}
