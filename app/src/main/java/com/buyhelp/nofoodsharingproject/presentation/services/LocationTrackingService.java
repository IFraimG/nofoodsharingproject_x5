package com.buyhelp.nofoodsharingproject.presentation.services;

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
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.buyhelp.nofoodsharingproject.data.api.map.dto.MarketTitleResponse;
import com.buyhelp.nofoodsharingproject.data.api.map.MapRepository;
import com.buyhelp.nofoodsharingproject.data.api.notifications.NotificationRepository;
import com.buyhelp.nofoodsharingproject.data.models.Advertisement;
import com.buyhelp.nofoodsharingproject.data.models.Market;
import com.buyhelp.nofoodsharingproject.data.models.Notification;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;

import org.jetbrains.annotations.NotNull;

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
    private NotificationRepository notificationRepository;
    private DefineUser defineUser;

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        sharedPreferences = getSharedPreferences("dateSettings", Context.MODE_PRIVATE);
        notificationRepository = new NotificationRepository();
        defineUser = new DefineUser(getApplicationContext());

        getMarket();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return START_NOT_STICKY;
        }

        defineUser = new DefineUser(getApplicationContext());
        if (defineUser.getTypeUser().second.equals(false)) startForeground(1, notifySetter());

        notificationRepository = new NotificationRepository();
        getMarket();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        sharedPreferences = getSharedPreferences("dateSettings", Context.MODE_PRIVATE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        return START_STICKY;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null && compareCoords != null && location.distanceTo(compareCoords) < 50 && checkTimer() && titleMarket.length() != 0) {
            getRandomGetterAdvert();
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
        MapRepository.getPinMarket(getApplicationContext(), defineUser.getTypeUser().second.equals(true) ? "getter" : "setter", defineUser.getTypeUser().first.toString()).enqueue(new Callback<MarketTitleResponse>() {
            @Override
            public void onResponse(@NotNull Call<MarketTitleResponse> call, @NotNull Response<MarketTitleResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    titleMarket = response.body().getMarket();
                    for (Market fullListMarket : fullListMarkets) {
                        if (fullListMarket.getTitle().equals(titleMarket)) {
                            compareCoords = new Location("");
                            compareCoords.setLatitude(fullListMarket.getLatitude());
                            compareCoords.setLongitude(fullListMarket.getLongitude());
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

    private boolean checkTimer() {
        String dateLocation = sharedPreferences.getString("locationDate", "");
        if (dateLocation.length() == 0) return true;

        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = dateFormat.parse(dateLocation);

            if (date1 == null) return false;

            long diffInMilliseconds = Math.abs(new Date().getTime() - date1.getTime());

            return diffInMilliseconds > 7200000;
        } catch (ParseException err) {
            return true;
        }
    }

    private void getRandomGetterAdvert() {
        AdvertsRepository advertsRepository = new AdvertsRepository();
        advertsRepository.getRandomAdvertByMarket(getApplicationContext(), titleMarket).enqueue(new Callback<Advertisement>() {
            @Override
            public void onResponse(@NotNull Call<Advertisement> call, @NotNull Response<Advertisement> response) {
                if (!response.isSuccessful() || response.body() == null) createNotification(new Advertisement());
                else createNotification(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<Advertisement> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void createNotification(Advertisement advert) {
        String title = getString(R.string.near_market);
        StringBuilder body = new StringBuilder();
        if (advert.getAuthorName() != null) {
            body.append(advert.getAuthorName()).append(" нуждается в продуктах");
            if (advert.getListProducts().length > 0) {
                StringBuilder listProducts = new StringBuilder();

                listProducts.append(": ");
                String[] result = advert.getListProducts();
                for (int i = 0; i < result.length - 1; i++)
                    listProducts.append(result[i]).append(", ");

                listProducts.append(result[result.length - 1]).append(".");
                body.append(listProducts);
            }
        } else body.append(getString(R.string.help_getter));

        Notification notification = new Notification(title, body.toString(), defineUser.getTypeUser().first.toString());
        notification.setTypeOfUser("setter");
        notification.setAdvertID(advert.getAdvertsID());
        showNotification(title, body.toString());

        sendNotification(notification);
    }

    private void sendNotification(Notification notification) {
        notificationRepository.createNotification(getApplicationContext(), notification).enqueue(new Callback<Notification>() {
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

    private void showNotification(String title, String body) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel("my_channel_id", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel_id")
                .setSmallIcon(R.drawable.notifications_active)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManagerCompat = NotificationManagerCompat.from(this);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        notificationManagerCompat.notify(1, builder.build());
    }


    private android.app.Notification notifySetter() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel("my_channel_id", "Location Observer", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel_id")
                .setContentTitle(getString(R.string.location_on_title))
                .setContentText(getString(R.string.location_on_body))
                .setSmallIcon(R.drawable.notifications_active)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        return builder.build();
    }
}