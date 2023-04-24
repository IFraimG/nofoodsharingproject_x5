package com.example.nofoodsharingproject.activities;

import static com.example.nofoodsharingproject.R.layout.fragment_getter_advrs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.fragments.getter.GetterAdvrsF;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.yandex.mapkit.MapKitFactory;

public class GetterNewAdvert extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getter_create_new_advertisment);
        Button button_ready = findViewById(R.id.ready_to_create);
        Button button_back = findViewById(R.id.button_back);
        Button button_re = findViewById(R.id.re_advertisment);

        button_ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ОТПРАВИТЬ ДАНЫЕ НА СЕРВЕР
                Toast.makeText(getApplicationContext(),
                        "Объявление успешно создано", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        button_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ОТПРАВИТЬ ДАНЫЕ НА СЕРВЕР
                Toast.makeText(getApplicationContext(),
                        "Объявление успешно создано", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
