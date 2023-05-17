package com.example.nofoodsharingproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.databinding.ActivitySetterBinding;
import com.example.nofoodsharingproject.services.LocationTrackingService;
import com.example.nofoodsharingproject.utils.DateNowChecker;
import com.example.nofoodsharingproject.utils.PermissionHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Setter_Activity extends AppCompatActivity {
    private NavController navController;
    private ActivitySetterBinding binding;
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

    private void initLocation() {
        if (!PermissionHandler.checkPermissions(getApplicationContext())) PermissionHandler.requestPermissions(this);
        else {
            Intent serviceIntent = new Intent(this, LocationTrackingService.class);
            startForegroundService(serviceIntent);
        }
    }
}
