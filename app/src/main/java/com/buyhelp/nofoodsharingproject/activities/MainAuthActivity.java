package com.buyhelp.nofoodsharingproject.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.buyhelp.nofoodsharingproject.databinding.ActivityMainAuthBinding;

public class MainAuthActivity extends AppCompatActivity {
    private ActivityMainAuthBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        binding = null;
    }
}