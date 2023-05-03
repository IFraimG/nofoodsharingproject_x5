package com.example.nofoodsharingproject.activities;

import static com.example.nofoodsharingproject.R.layout.fragment_getter_advrs;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.example.nofoodsharingproject.MainActivity;
import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.data.repository.AdvertsRepository;
import com.example.nofoodsharingproject.fragments.getter.GetterAdvrsF;
import com.example.nofoodsharingproject.models.Advertisement;
import com.example.nofoodsharingproject.models.Getter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.yandex.mapkit.MapKitFactory;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                Getter result = getUserInfo();
                Advertisement advertisement = new Advertisement("test title", "test desc", result.getX5_Id(), result.getLogin());
                advertisement.setGettingProductID("testid");
                // жду recycler от тебя
                advertisement.setListProducts(null);
                button_ready.setEnabled(false);
                AdvertsRepository.createAdvert(advertisement).enqueue(new Callback<Advertisement>() {
                    @Override
                    public void onResponse(@NotNull Call<Advertisement> call, @NotNull Response<Advertisement> response) {
                        Advertisement result = response.body();
                        if (response.code() != 400) {
                            Toast.makeText(getApplicationContext(),
                                    "Объявление успешно создано", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            button_ready.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<Advertisement> call, Throwable t) {
                        button_ready.setEnabled(true);
                        t.printStackTrace();
                    }
                });
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

    public Getter getUserInfo() {
        try {
            MasterKey masterKey = new MasterKey.Builder(getApplicationContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(getApplicationContext(), "user", masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            String login = sharedPreferences.getString("login", "");
            String authorID = sharedPreferences.getString("X5_id", "");

            Getter userRequestData = new Getter();
            userRequestData.setLogin(login);
            userRequestData.setX5_Id(authorID);

            return userRequestData;
        } catch (IOException | GeneralSecurityException err) {
            Log.e("getting info error", err.toString());
            err.printStackTrace();
        }

        return null;
    }
}
