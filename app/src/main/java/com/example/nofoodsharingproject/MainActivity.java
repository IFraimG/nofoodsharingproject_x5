package com.example.nofoodsharingproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.nofoodsharingproject.activities.Getter_Activity;
import com.example.nofoodsharingproject.activities.MainAuth_Activity;
import com.example.nofoodsharingproject.activities.Setter_Activity;
import com.example.nofoodsharingproject.data.api.auth.interfaces.CheckAuthI;
import com.example.nofoodsharingproject.data.repository.AuthRepository;
import com.example.nofoodsharingproject.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private SharedPreferences sharedPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try {
            MasterKey masterKey = new MasterKey.Builder(getApplicationContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();
            sharedPreferences = EncryptedSharedPreferences.create(getApplicationContext(), "user", masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            if (sharedPreferences.contains("isGetter")) {
                if (sharedPreferences.getBoolean("isGetter", false)) authGetter();
                else authSetter();
            } else redirectToAuth();
        } catch (IOException | GeneralSecurityException err) {
            Log.e("auth error", err.toString());
            err.printStackTrace();
        }
    }

    public void authSetter() {
        AuthRepository.checkAuthSetter(sharedPreferences.getString("token", "")).enqueue(new Callback<CheckAuthI>() {
            @Override
            public void onResponse(@NotNull Call<CheckAuthI> call, @NotNull Response<CheckAuthI> response) {
                if (!response.body().getIsAuth()) redirectToAuth();
                else {
                    Intent intentSetter = new Intent(getApplicationContext(), Setter_Activity.class);
                    startActivity(intentSetter);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<CheckAuthI> call, Throwable t) {
                redirectToAuth();
            }
        });
    }

    public void authGetter() {
        AuthRepository.checkAuthGetter(sharedPreferences.getString("token", "")).enqueue(new Callback<CheckAuthI>() {
            @Override
            public void onResponse(@NotNull Call<CheckAuthI> call, @NotNull Response<CheckAuthI> response) {
                if (response.body().getIsAuth()) {
                    Intent intentGetter = new Intent(getApplicationContext(), Getter_Activity.class);
                    startActivity(intentGetter);
                    finish();
                } else redirectToAuth();
            }

            @Override
            public void onFailure(Call<CheckAuthI> call, Throwable t) {
                redirectToAuth();
            }
        });
    }

    public void redirectToAuth() {
//        Toast.makeText(getApplicationContext(), "Вы не авторизованы", Toast.LENGTH_SHORT).show();
        Intent intentAuth = new Intent(getApplicationContext(), MainAuth_Activity.class);
        startActivity(intentAuth);
        finish();
    }
}