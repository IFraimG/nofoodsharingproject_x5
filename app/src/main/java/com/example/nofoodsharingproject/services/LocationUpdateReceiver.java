package com.example.nofoodsharingproject.services;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.nofoodsharingproject.R;

// не используется
public class LocationUpdateReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        String key = LocationManager.KEY_LOCATION_CHANGED;
        Location location = (Location) intent.getExtras().get(key);

        createNotificationChannel(context);

        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            long time = location.getTime();

            Location location2 = new Location("");
            location2.setLatitude(55.648748);
            location2.setLongitude(37.879440);

            double distance = location.distanceTo(location2);
            Log.d("location", Double.toString(distance));
            Log.d("location", Double.toString(distance));
            Log.d("location", Double.toString(distance));
            Log.d("location", Double.toString(distance));
            Log.d("location", Double.toString(distance));
            if (distance < 100) {
//
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "my_channel")
//                        .setSmallIcon(R.drawable.notifications_active)
//                        .setContentTitle("Нуждающемуся нужна помощь")
//                        .setContentText("Смотрите подробную информацию")
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
//                    notificationManager.notify(NOTIFICATION_ID, builder.build());
                }
            }

        }
    }

    private void createNotificationChannel(Context context) {
        CharSequence name = "My Channel";
        String description = "Описание канала";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("my_channel", name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
