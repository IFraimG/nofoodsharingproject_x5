package com.example.nofoodsharingproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.nofoodsharingproject.activities.GetterAC;
import com.example.nofoodsharingproject.activities.MainAuthAC;
import com.example.nofoodsharingproject.activities.SetterAC;
import com.example.nofoodsharingproject.view_models.AdvertisementListViewModel;
import com.yandex.mapkit.MapKitFactory;

public class MainActivity extends AppCompatActivity {
    //    private ActivityMainBinding binding;
    NavController navController = null;
    boolean isAuth = false;
    // костыль
    // ЕСЛИ ХОЧЕШЬ РЕДАКТИРОВАТЬ НУЖДАЮЩЕГОСЯ, ОБЯЗАТЕЛЬНО ПРОПИШИ ЗДЕСЬ true
    boolean isGetter = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // костыль !
        isAuth = getIntent().getBooleanExtra("isAuth", false);

        // server request ...
//        if (!isAuth) { потом заменить
        if (isAuth) {
            Intent intentAuth = new Intent(getApplicationContext(), MainAuthAC.class);
            startActivity(intentAuth);
            finish();
        }
        else {
            if (isGetter) {
                Intent intentGetter = new Intent(getApplicationContext(), GetterAC.class);
                startActivity(intentGetter);
                finish();
            } else {
                Intent intentSetter = new Intent(getApplicationContext(), SetterAC.class);
                startActivity(intentSetter);
            }
            finish();
        }
    }

}