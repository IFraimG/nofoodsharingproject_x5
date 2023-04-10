package com.example.nofoodsharingproject.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nofoodsharingproject.R;

public class SetterAdvertAC extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setter_advert);

        String advertID = getIntent().getStringExtra("advertID");
    }
}
