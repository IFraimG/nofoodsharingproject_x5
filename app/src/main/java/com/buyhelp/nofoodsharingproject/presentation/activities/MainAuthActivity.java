package com.buyhelp.nofoodsharingproject.presentation.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.buyhelp.nofoodsharingproject.databinding.ActivityMainAuthBinding;
import com.buyhelp.nofoodsharingproject.presentation.services.NetworkChangeReceiver;

public class MainAuthActivity extends AppCompatActivity {
    private ActivityMainAuthBinding binding;
    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        networkChangeReceiver = new NetworkChangeReceiver(new NetworkChangeReceiver.NetworkChangeListener() {
            @Override
            public void onNetworkConnected() {}

            @Override
            public void onNetworkDisconnected() {
                Intent intent = new Intent(MainAuthActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
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
}