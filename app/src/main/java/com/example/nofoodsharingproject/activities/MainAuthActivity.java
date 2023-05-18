package com.example.nofoodsharingproject.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.example.nofoodsharingproject.databinding.ActivityMainAuthBinding;

public class MainAuthActivity extends AppCompatActivity {
    private ActivityMainAuthBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

}