package com.example.nofoodsharingproject.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.data.api.map.dto.MarketTitleResponse;
import com.example.nofoodsharingproject.data.api.map.MapRepository;
import com.example.nofoodsharingproject.data.api.notifications.NotificationRepository;
import com.example.nofoodsharingproject.models.Advertisement;
import com.example.nofoodsharingproject.models.Market;
import com.example.nofoodsharingproject.models.Notification;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationTrackingService extends Service implements LocationListener {

    private LocationManager locationManager;
    private SharedPreferences sharedPreferences;

    private NotificationManager notificationManager;
    private NotificationManagerCompat notificationManagerCompat;
    private NotificationChannel channel;
    private String titleMarket;
    private final Market[] fullListMarkets = new Market[]{
            new Market("Большая Андроньевская улица, 22", 55.740813, 37.670078),
            new Market("1, микрорайон Парковый, Котельники", 55.660216, 37.875793),
            new Market("Ковров пер., 8, стр. 1", 55.740582, 37.681854),
            new Market("Нижегородская улица, 34", 55.736351, 37.695708)
    };

    private Location compareCoords;

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        sharedPreferences = getSharedPreferences("dateSettings", Context.MODE_PRIVATE);

        getMarket();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return START_NOT_STICKY;
        }

        if (!defineUser().second) startForeground(1, notifySetter());

        getMarket();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        sharedPreferences = getSharedPreferences("dateSettings", Context.MODE_PRIVATE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        return START_STICKY;
    }
    @Override
    public void onLocationChanged(Location location) {
        if (location.distanceTo(compareCoords) < 100 && checkTimer() && titleMarket.length() != 0) {
            Advertisement advert = getRandomGetterAdvert();
            createNotification(advert);
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void getMarket() {
        Pair<String, Boolean> userData = defineUser();
        MapRepository.getPinMarket(userData.second ? "getter" : "setter", userData.first).enqueue(new Callback<MarketTitleResponse>() {
            @Override
            public void onResponse(@NotNull Call<MarketTitleResponse> call, @NotNull Response<MarketTitleResponse> response) {
                if (response.code() == 200) {
                    titleMarket = response.body().getMarket();
                    for (int i = 0; i < fullListMarkets.length; i++) {
                        if (fullListMarkets[i].getTitle().equals(titleMarket)) {
                            compareCoords = new Location("");
                            compareCoords.setLatitude(fullListMarkets[i].getLatitude());
                            compareCoords.setLongitude(fullListMarkets[i].getLongitude());
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<MarketTitleResponse> call, @NotNull Throwable t) {
                Log.e("err", getString(R.string.unvisinle_error));
                t.printStackTrace();
            }
        });
    }
    private Pair<String, Boolean> defineUser() {
        try {
            MasterKey masterKey = new MasterKey.Builder(getApplicationContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(getApplicationContext(), "user", masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            boolean isGetter = sharedPreferences.getBoolean("isGetter", false);
            String userID = sharedPreferences.getString("X5_id", "");

            return new Pair<String, Boolean>(userID, isGetter);
        } catch (IOException | GeneralSecurityException err) {
            Log.e("auth error", err.toString());
            err.printStackTrace();
        }

        return new Pair<>("", false);
    }

    private boolean checkTimer() {
        String dateLocation = sharedPreferences.getString("locationDate", "");
        if (dateLocation.length() == 0) return true;

        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = dateFormat.parse(dateLocation);

            long diffInMilliseconds = Math.abs(new Date().getTime() - date1.getTime());

            Log.d("msg", Long.toString(diffInMilliseconds));

            return diffInMilliseconds > 12000000;
        } catch (ParseException err) {
            return true;
        }
    }

    private Advertisement getRandomGetterAdvert() {
        return new Advertisement();
    }

    private void createNotification(Advertisement advert) {
        Notification notification = new Notification("Вы находитесь рядом с магазином!", "Тестовое уведомление", defineUser().first);
        notification.setTypeOfUser("setter");
        showNotification();
        NotificationRepository.createNotification(notification).enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(@NotNull Call<Notification> call, @NotNull Response<Notification> response) {
                if (response.code() == 201) {
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String result = dateFormat.format(new Date());

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("locationDate", result);
                    editor.apply();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Notification> call, @NotNull Throwable t) {
                Log.e("err", getString(R.string.unvisinle_error));
                t.printStackTrace();
            }
        });
    }

    private void showNotification() {
        channel = new NotificationChannel("my_channel_id", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel_id")
                .setSmallIcon(R.drawable.notifications_active)
                .setContentTitle("Нуждающемуся нужна помощь!")
                .setContentText("Рыба, мясо, колбаса")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManagerCompat = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {}

        notificationManagerCompat.notify(1, builder.build());
    }

    private android.app.Notification notifySetter() {
        NotificationChannel channel = new NotificationChannel("my_channel_id", "Location Observer", NotificationManager.IMPORTANCE_LOW);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel_id")
                .setContentTitle("Спасибо, что используете наше приложение с умом!")
                .setContentText("Когда вы будете рядом с вашим магазином, мы сообщим вам о нуждающемся")
                .setSmallIcon(R.drawable.notifications_active)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        return builder.build();
    }
}