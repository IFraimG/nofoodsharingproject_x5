package com.example.nofoodsharingproject.activities;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.example.nofoodsharingproject.BuildConfig;
import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.data.api.map.MarketTitleResponse;
import com.example.nofoodsharingproject.data.repository.MapRepository;
import com.example.nofoodsharingproject.databinding.ActivitySetterBinding;
import com.example.nofoodsharingproject.models.Setter;
import com.example.nofoodsharingproject.services.LocationTrackingService;
import com.example.nofoodsharingproject.utils.DateNowChecker;
import com.example.nofoodsharingproject.services.LocationUpdateReceiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.yandex.mapkit.MapKitFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Setter_Activity extends AppCompatActivity {
    NavController navController;
    private ActivitySetterBinding binding;
    private LocationManager locationManager;
    private PendingIntent pendingIntent;

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.setApiKey(BuildConfig.apiKey);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        startLocationObserver();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_setter_fragment);
        navController = navHostFragment.getNavController();

        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.setter_navigation);
        NavigationUI.setupWithNavController(navigationView, navController);

        DateNowChecker dateNowChecker = new DateNowChecker();
        if (dateNowChecker.getHour() >= 10 && dateNowChecker.getHour() < 21) {
            initLocation();
        } else removeLocation();
//        Log.d("info", Integer.toString(dateNowChecker.getHour()));

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavigationUI.onNavDestinationSelected(item, navController);
        return super.onOptionsItemSelected(item);
    }

    private void initLocation() {
        String svcName = Context.LOCATION_SERVICE;
        this.locationManager = (LocationManager) getSystemService(svcName);
        String provider = LocationManager.PASSIVE_PROVIDER;

        int t = 60000;
        int distance = 25;

        Intent intent = new Intent(this, LocationUpdateReceiver.class);
        this.pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_MUTABLE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
        locationManager.requestLocationUpdates(provider, t, distance, pendingIntent);
    }

    private void removeLocation() {
        if (locationManager != null) locationManager.removeUpdates(pendingIntent);
    }

    private void startLocationObserver() {
        MapRepository.getPinMarket("setter", getUserID()).enqueue(new Callback<MarketTitleResponse>() {
            @Override
            public void onResponse(Call<MarketTitleResponse> call, Response<MarketTitleResponse> response) {
                if (response.code() == 200) {
                    Intent serviceLocation = new Intent(Setter_Activity.this, LocationTrackingService.class);
                    startForegroundService(serviceLocation);
                }
            }

            @Override
            public void onFailure(Call<MarketTitleResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.unvisinle_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getUserID() {
        try {
            MasterKey masterKey = new MasterKey.Builder(getApplicationContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(getApplicationContext(), "user", masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

            return sharedPreferences.getString("X5_id", "");
        } catch (GeneralSecurityException | IOException err) {
            Toast.makeText(getApplicationContext(), R.string.unvisinle_error, Toast.LENGTH_SHORT).show();
            Log.e("esp_error", err.toString());
        }
        return null;
    }
}
