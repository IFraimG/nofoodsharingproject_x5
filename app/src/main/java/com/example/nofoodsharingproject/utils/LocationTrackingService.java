package com.example.nofoodsharingproject.utils;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.nofoodsharingproject.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
public class LocationTrackingService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
//public class LocationTrackingService extends Service {
//    private FusedLocationProviderClient fusedLocationClient;
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        startForeground(1, createNotification());
//        requestLocationUpdates();
//        return START_STICKY;
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    private void requestLocationUpdates() {
//        LocationRequest locationRequest = LocationRequest.create();
//        locationRequest.setInterval(10000);
//        locationRequest.setFastestInterval(5000);
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//
//        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
//    }
//
//    private LocationCallback locationCallback = new LocationCallback() {
//        @Override
//        public void onLocationResult(LocationResult locationResult) {
//            if (locationResult == null) {
//                return;
//            }
//            for (Location location : locationResult.getLocations()) {
//                // Обработка полученных данных о местоположении
//            }
//        }
//    };
//
//    private Notification createNotification() {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel")
//                .setSmallIcon(R.drawable.notifications_active)
//                .setContentTitle("Нуждающемуся нужна помощь")
//                .setContentText("Смотрите подробную информацию")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//
//        return null;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//    }
//
//    @Override
//    public void onDestroy() {
//        fusedLocationClient.removeLocationUpdates(locationCallback);
//        super.onDestroy();
//    }
//}
