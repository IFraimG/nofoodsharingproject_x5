package com.example.nofoodsharingproject.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.nofoodsharingproject.BuildConfig;
import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.databinding.ActivitySetterBinding;
import com.example.nofoodsharingproject.services.LocationTrackingService;
import com.example.nofoodsharingproject.utils.DateNowChecker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yandex.mapkit.MapKitFactory;

public class Setter_Activity extends AppCompatActivity {
    private NavController navController;
    private ActivitySetterBinding binding;
    private boolean isInitMap = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!isInitMap) {
            MapKitFactory.setApiKey(BuildConfig.apiKey);
            MapKitFactory.initialize(getApplicationContext());
            isInitMap = true;
        }

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_setter_fragment);
        navController = navHostFragment.getNavController();

        BottomNavigationView navigationView = binding.setterNavigation;
        NavigationUI.setupWithNavController(navigationView, navController);

        DateNowChecker dateNowChecker = new DateNowChecker();
        if (dateNowChecker.getHour() >= 10 && dateNowChecker.getHour() < 21) initLocation();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavigationUI.onNavDestinationSelected(item, navController);
        return super.onOptionsItemSelected(item);
    }

    private void initLocation() {
        Intent serviceIntent = new Intent(this, LocationTrackingService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(serviceIntent);
        else startService(serviceIntent);
    }

}
