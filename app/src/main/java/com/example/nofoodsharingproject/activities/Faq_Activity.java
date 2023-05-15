package com.example.nofoodsharingproject.activities;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nofoodsharingproject.databinding.ActivityFaqBinding;

public class Faq_Activity extends AppCompatActivity {
    private ActivityFaqBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        binding = ActivityFaqBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}
