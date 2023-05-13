package com.example.nofoodsharingproject.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.Toast;


import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

// не используется
public class MakeNotificationTimerObserver extends BroadcastReceiver {
        final public static String ONE_TIME = "onetime";
        @Override
        public void onReceive(Context context, Intent intent) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "msg:mytag");
            //Acquire the lock
            wl.acquire();

            Bundle extras = intent.getExtras();
            StringBuilder msgStr = new StringBuilder();

            if (extras != null && extras.getBoolean(ONE_TIME, Boolean.FALSE)){
                msgStr.append("One time Timer : ");
            }
            Format formatter = new SimpleDateFormat("hh:mm:ss");
            msgStr.append(formatter.format(new Date()));

            Toast.makeText(context, msgStr, Toast.LENGTH_LONG).show();

            //Release the lock
            wl.release();
        }

        public void SetAlarm(Context context) {
            AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
            intent.putExtra(ONE_TIME, Boolean.FALSE);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 20 , pi);
        }

        public void CancelAlarm(Context context) {
            Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
            PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(sender);
        }

        public void setOnetimeTimer(Context context){
            AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
            intent.putExtra(ONE_TIME, Boolean.TRUE);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
        }
}
