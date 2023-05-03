package com.example.nofoodsharingproject.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.data.api.adverts.RequestDoneAdvert;
import com.example.nofoodsharingproject.data.repository.AdvertsRepository;
import com.example.nofoodsharingproject.models.Getter;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetterAdvertAC extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setter_advert);

        String advertID = getIntent().getStringExtra("advertID");
        ImageView backButton = findViewById(R.id.setter_advert_back);
        backButton.setOnClickListener(View -> finish());

        Button acceptBtn = (Button) findViewById(R.id.setter_advert_accept);
        acceptBtn.setOnClickListener(View -> {
            acceptBtn.setEnabled(false);
            AdvertsRepository.makeDoneAdvert(new RequestDoneAdvert(advertID, getSetterID())).enqueue(new Callback<RequestDoneAdvert>() {
                @Override
                public void onResponse(@NotNull Call<RequestDoneAdvert> call, @NotNull Response<RequestDoneAdvert> response) {
                    if (response.code() == 400) {
                        acceptBtn.setEnabled(true);
                        Toast.makeText(getApplicationContext(), "Что-то пошло не так. Попробуйте еще раз", Toast.LENGTH_SHORT).show();
                    } else finish();
                }

                @Override
                public void onFailure(Call<RequestDoneAdvert> call, Throwable t) {
                    acceptBtn.setEnabled(true);
                    t.printStackTrace();
                }
            });
        });
    }

    public String getSetterID() {
        try {
            MasterKey masterKey = new MasterKey.Builder(getApplicationContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(getApplicationContext(), "user", masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            String authorID = sharedPreferences.getString("X5_id", "");
            return authorID;
        } catch (IOException | GeneralSecurityException err) {
            Log.e("getting info error", err.toString());
            err.printStackTrace();
        }

        return null;
    }
}
