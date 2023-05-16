package com.example.nofoodsharingproject.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.data.api.adverts.dto.ResponseDeleteAdvert;
import com.example.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.example.nofoodsharingproject.models.Advertisement;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdvertisementExpires extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        getAdvert(context);
    }

    private String getUserID(Context ctx) {
        try {
            MasterKey masterKey = new MasterKey.Builder(ctx, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(ctx, "user", masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            String authorID = sharedPreferences.getString("X5_id", "");

            return authorID;
        } catch (IOException | GeneralSecurityException err) {
            Log.e("getting info error", err.toString());
            err.printStackTrace();
        }

        return "";
    }

    private void getAdvert(Context ctx) {
        AdvertsRepository.getOwnAdvert(getUserID(ctx)).enqueue(new Callback<Advertisement>() {
            @Override
            public void onResponse(@NotNull Call<Advertisement> call, @NotNull Response<Advertisement> response) {
                if (response.body() != null && response.code() == 200) {
                    deleteAdvert(ctx, response.body());
                }
            }

            @Override
            public void onFailure(@NotNull Call<Advertisement> call, @NotNull Throwable t) {
                Log.e("err", ctx.getString(R.string.unvisinle_error));
            }
        });
    }

    private void deleteAdvert(Context context, Advertisement advert) {
        if (advert != null) AdvertsRepository.deleteAdvert(advert.getAdvertsID()).enqueue(new Callback<ResponseDeleteAdvert>() {
            @Override
            public void onResponse(@NotNull Call<ResponseDeleteAdvert> call, @NotNull Response<ResponseDeleteAdvert> response) {
                if (response.code() == 400) Log.e("err", context.getString(R.string.unvisinle_error));
            }

            @Override
            public void onFailure(@NotNull Call<ResponseDeleteAdvert> call, @NotNull Throwable t) {
                Log.e("err", context.getString(R.string.unvisinle_error));
            }
        });
    }
}
