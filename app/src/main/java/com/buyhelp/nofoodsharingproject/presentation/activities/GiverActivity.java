package com.buyhelp.nofoodsharingproject.presentation.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.databinding.ActivityGiverBinding;
import com.buyhelp.nofoodsharingproject.presentation.ApplicationCore;
import com.buyhelp.nofoodsharingproject.presentation.di.components.DaggerPermissionComponent;
import com.buyhelp.nofoodsharingproject.presentation.di.components.PermissionComponent;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.ActivityModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.PermissionHandlerModule;
import com.buyhelp.nofoodsharingproject.presentation.services.LocationTrackingService;
import com.buyhelp.nofoodsharingproject.domain.utils.DateNowChecker;
import com.buyhelp.nofoodsharingproject.domain.utils.DateNowCheckerOld;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.buyhelp.nofoodsharingproject.domain.helpers.PermissionHandler;
import com.buyhelp.nofoodsharingproject.presentation.services.NetworkChangeReceiver;

import javax.inject.Inject;

public class GiverActivity extends AppCompatActivity {
    private NavController navController;
    private ActivityGiverBinding binding;

    @Inject
    public DefineUser defineUser;
    private PermissionHandler permissionHandler;
    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGiverBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        ApplicationCore app = (ApplicationCore) getApplication();
        app.getAppComponent().inject(this);

        networkChangeReceiver = new NetworkChangeReceiver(new NetworkChangeReceiver.NetworkChangeListener() {
            @Override
            public void onNetworkConnected() {}

            @Override
            public void onNetworkDisconnected() {
                Intent intent = new Intent(GiverActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        PermissionComponent permissionComponent = DaggerPermissionComponent.builder().defineActivity(new ActivityModule(this)).definePermissions(new PermissionHandlerModule()).build();
        permissionHandler = permissionComponent.getPermissionHandler();

        permissionHandler.requestCalendarPermissions(this);

        if (defineUser.getIsLocation()) permissionHandler.requestPermissions(this);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_giver_fragment);
        if (navHostFragment != null) navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(binding.giverNavigation, navController);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateNowChecker dateNowChecker = new DateNowChecker();
            if (dateNowChecker.getHour() >= 10 && dateNowChecker.getHour() < 23) initLocation();
        } else {
            DateNowCheckerOld dateNowCheckerOld = new DateNowCheckerOld();
            if (dateNowCheckerOld.getHour() >= 10 && dateNowCheckerOld.getHour() < 23) initLocation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkChangeReceiver);
    }

    public void setBottomNavigationVisibility(boolean isVisible) {
        if (isVisible) binding.giverNavigation.setVisibility(View.VISIBLE);
        else binding.giverNavigation.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        binding = null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavigationUI.onNavDestinationSelected(item, navController);
        return super.onOptionsItemSelected(item);
    }

    private void initLocation() {
        if (!permissionHandler.checkPermissions(this)) permissionHandler.requestPermissions(this);
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
