package com.example.nofoodsharingproject.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.databinding.ActivitySetterBinding;
import com.example.nofoodsharingproject.services.LocationTrackingService;
import com.example.nofoodsharingproject.utils.DateNowChecker;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Setter_Activity extends AppCompatActivity {
    private NavController navController;
    private ActivitySetterBinding binding;
    private int firstPermission;
    private int secondPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetterBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

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

    private void requestPermissions() {
        firstPermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        secondPermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);

        if (firstPermission != PackageManager.PERMISSION_GRANTED && secondPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        } else if (firstPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
        } else if (secondPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
    }

    private boolean checkLocationPermissions() {
        firstPermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        secondPermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);

        return firstPermission == PackageManager.PERMISSION_GRANTED && secondPermission == PackageManager.PERMISSION_GRANTED;
    }
    private void initLocation() {
        if (!checkLocationPermissions()) requestPermissions();
        else {
            Intent serviceIntent = new Intent(this, LocationTrackingService.class);
            startForegroundService(serviceIntent);
        }
    }
}
