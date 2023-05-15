package com.example.nofoodsharingproject.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.nofoodsharingproject.BuildConfig;
import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.databinding.ActivityGetterBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yandex.mapkit.MapKitFactory;

public class Getter_Activity extends AppCompatActivity {
    private NavController navController;
    private ActivityGetterBinding binding;
    private boolean isInitMap = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!isInitMap) {
            MapKitFactory.setApiKey(BuildConfig.apiKey);
            MapKitFactory.initialize(getApplicationContext());
            isInitMap = true;
        }
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_getter_fragment);
        navController = navHostFragment.getNavController();

        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.getter_navigation);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavigationUI.onNavDestinationSelected(item, navController);
        return super.onOptionsItemSelected(item);
    }
}
