package com.example.nofoodsharingproject.activities;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.nofoodsharingproject.BuildConfig;
import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.databinding.ActivityMainBinding;
import com.example.nofoodsharingproject.databinding.ActivitySetterBinding;
import com.example.nofoodsharingproject.utils.DateNowChecker;
import com.example.nofoodsharingproject.utils.LocationUpdateReceiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.yandex.mapkit.MapKitFactory;

import java.util.Set;

public class Setter_Activity extends AppCompatActivity {
    NavController navController;
    private ActivitySetterBinding binding;
    private LocationManager locationManager;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MapKitFactory.setApiKey(BuildConfig.apiKey);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_setter_fragment);
        navController = navHostFragment.getNavController();

        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.setter_navigation);
        NavigationUI.setupWithNavController(navigationView, navController);

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("token", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        String token = task.getResult();
                        Log.d("token", token);
                        Log.d("token", token);
                        Log.d("token", token);
                        Log.d("token", token);
                        Log.d("token", token);
                        Toast.makeText(Setter_Activity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });

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
}
