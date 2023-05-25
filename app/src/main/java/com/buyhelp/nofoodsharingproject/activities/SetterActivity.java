package com.buyhelp.nofoodsharingproject.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.databinding.ActivitySetterBinding;
import com.buyhelp.nofoodsharingproject.models.Setter;
import com.buyhelp.nofoodsharingproject.services.LocationTrackingService;
import com.buyhelp.nofoodsharingproject.utils.DateNowChecker;
import com.buyhelp.nofoodsharingproject.utils.DateNowCheckerOld;
import com.buyhelp.nofoodsharingproject.utils.DefineUser;
import com.buyhelp.nofoodsharingproject.utils.PermissionHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SetterActivity extends AppCompatActivity {
    private NavController navController;
    private ActivitySetterBinding binding;
    private DefineUser<Setter> defineUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetterBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        defineUser = new DefineUser<>(this);

        PermissionHandler.requestCalendarPermissions(this);
        if (defineUser.getIsLocation()) PermissionHandler.requestPermissions(this);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_setter_fragment);
        navController = navHostFragment.getNavController();

        BottomNavigationView navigationView = binding.setterNavigation;
        NavigationUI.setupWithNavController(navigationView, navController);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateNowChecker dateNowChecker = new DateNowChecker();
            if (dateNowChecker.getHour() >= 10 && dateNowChecker.getHour() < 23) initLocation();
        } else {
            DateNowCheckerOld dateNowCheckerOld = new DateNowCheckerOld();
            if (dateNowCheckerOld.getHour() >= 10 && dateNowCheckerOld.getHour() < 23) initLocation();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavigationUI.onNavDestinationSelected(item, navController);
        return super.onOptionsItemSelected(item);
    }

    private void initLocation() {
        if (!PermissionHandler.checkPermissions(getApplicationContext())) PermissionHandler.requestPermissions(this);
        else {
            if (defineUser.getIsLocation()) {
                Intent serviceIntent = new Intent(this, LocationTrackingService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent);
                } else startService(serviceIntent);
            }
        }
    }
}
