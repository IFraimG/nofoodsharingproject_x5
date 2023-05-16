package com.example.nofoodsharingproject.utils;

import android.app.Activity;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.example.nofoodsharingproject.models.Getter;
import com.example.nofoodsharingproject.models.Setter;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class DefineUser {
    private EncryptedSharedPreferences encryptedSharedPreferences;

    public DefineUser(Activity activity) {
        initEsp(activity);
    }

    public DefineUser(EncryptedSharedPreferences esp) {
        encryptedSharedPreferences = esp;
    }

    public void initEsp(Activity activity) {
        try {
            MasterKey masterKey = new MasterKey.Builder(activity.getApplicationContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            encryptedSharedPreferences = (EncryptedSharedPreferences) EncryptedSharedPreferences.create(activity.getApplicationContext(), "user", masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        } catch (IOException | GeneralSecurityException err) {
            Log.e("auth error", err.toString());
            err.printStackTrace();
        }
    }

    public Setter defineSetter() {
        String login = encryptedSharedPreferences.getString("login", "");
        String phone = encryptedSharedPreferences.getString("phone", "");
        String userID = encryptedSharedPreferences.getString("X5_id", "");

        Setter user = new Setter();
        user.setLogin(login);
        user.setPhone(phone);
        user.setX5_Id(userID);

        return user;
    }

    public Getter defineGetter() {
        String login = encryptedSharedPreferences.getString("login", "");
        String phone = encryptedSharedPreferences.getString("phone", "");
        String userID = encryptedSharedPreferences.getString("X5_id", "");

        Getter user = new Getter();
        user.setLogin(login);
        user.setPhone(phone);
        user.setX5_Id(userID);

        return user;
    }


}
