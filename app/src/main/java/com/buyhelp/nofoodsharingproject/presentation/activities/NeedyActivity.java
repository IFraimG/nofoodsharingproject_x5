package com.buyhelp.nofoodsharingproject.presentation.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.databinding.ActivityNeedyBinding;
import com.buyhelp.nofoodsharingproject.domain.helpers.PermissionHandler;
import com.buyhelp.nofoodsharingproject.presentation.di.components.DaggerPermissionComponent;
import com.buyhelp.nofoodsharingproject.presentation.di.components.PermissionComponent;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.ActivityModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.PermissionHandlerModule;
import com.buyhelp.nofoodsharingproject.presentation.services.NetworkChangeReceiver;


public class NeedyActivity extends AppCompatActivity {
    private NavController navController;
    private ActivityNeedyBinding binding;
    private PermissionHandler permissionHandler;
    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNeedyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        networkChangeReceiver = new NetworkChangeReceiver(new NetworkChangeReceiver.NetworkChangeListener() {
            @Override
            public void onNetworkConnected() {}

            @Override
            public void onNetworkDisconnected() {
                Intent intent = new Intent(NeedyActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        PermissionComponent permissionComponent = DaggerPermissionComponent.builder().defineActivity(new ActivityModule(this)).definePermissions(new PermissionHandlerModule()).build();
        permissionHandler = permissionComponent.getPermissionHandler();

        permissionHandler.requestCalendarPermissions(this);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_needy_fragment);
        navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(binding.needyNavigation, navController);
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

    public void setBottomNavigationVisibility(boolean isVisible) {
        if (isVisible) binding.needyNavigation.setVisibility(View.VISIBLE);
        else binding.needyNavigation.setVisibility(View.GONE);
    }
}
