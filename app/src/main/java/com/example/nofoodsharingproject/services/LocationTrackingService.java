package com.example.nofoodsharingproject.services;

import android.Manifest;
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
import com.example.nofoodsharingproject.data.api.map.MarketTitleResponse;
import com.example.nofoodsharingproject.data.repository.MapRepository;
import com.example.nofoodsharingproject.data.repository.NotificationRepository;
import com.example.nofoodsharingproject.models.Advertisement;
import com.example.nofoodsharingproject.models.Market;
import com.example.nofoodsharingproject.models.Notification;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// в разработке
public class LocationTrackingService extends Service implements LocationListener {

    private LocationManager locationManager;
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
            public void onFailure(Call<MarketTitleResponse> call, Throwable t) {
                Log.e("err", getString(R.string.unvisinle_error));
                t.printStackTrace();
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return START_NOT_STICKY;
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        return START_STICKY;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location.distanceTo(compareCoords) < 50 && checkTimer()) {
            Advertisement advert = getRandomGetterAdvert();
            createNotification(advert);
        }

        // сравнить с прикрепленным магазином и отправить уведомление пользователю, затем включить таймер, чтобы уведомления каждую секунду не отправлялись
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

        return null;
    }

    private boolean checkTimer() {
        return false;
    }

    private Advertisement getRandomGetterAdvert() {
        return new Advertisement();
    }

    private void createNotification(Advertisement advert) {
        Notification notification = new Notification("Вы находитесь рядом с магазином!", "Тестовое уведомление", defineUser().first);
        NotificationRepository.createNotification(notification).enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(@NotNull Call<Notification> call, @NotNull Response<Notification> response) {
                if (response.code() == 201) showNotification();
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
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
}