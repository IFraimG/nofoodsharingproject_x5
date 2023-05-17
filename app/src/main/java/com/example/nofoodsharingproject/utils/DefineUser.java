package com.example.nofoodsharingproject.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.example.nofoodsharingproject.data.api.auth.dto.SignUpResponseI;
import com.example.nofoodsharingproject.models.Getter;
import com.example.nofoodsharingproject.models.Setter;
import com.example.nofoodsharingproject.models.ShortDataUser;
import com.example.nofoodsharingproject.models.User;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class DefineUser<T extends User> {
    private EncryptedSharedPreferences encryptedSharedPreferences;
    private SharedPreferences sharedPreferences;

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
        sharedPreferences = activity.getSharedPreferences("prms", Context.MODE_PRIVATE);
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

    public Pair<String, Boolean> getTypeUser() {
        String userID = encryptedSharedPreferences.getString("X5_id", "");
        boolean isUser = encryptedSharedPreferences.getBoolean("isGetter", false);

        return new Pair<>(userID, isUser);
    }

    public void clearData() {
        SharedPreferences.Editor editor = encryptedSharedPreferences.edit();
        editor.clear();
        editor.apply();

        sharedPreferences.edit().clear().apply();
    }

    public void saveUserData(boolean isGetter, String X5_id, SignUpResponseI<T> result) {
        SharedPreferences.Editor editor = encryptedSharedPreferences.edit();
        editor.putBoolean("isGetter", isGetter);
        editor.putString("login", result.user.getLogin());
        editor.putString("phone", result.user.getPhone());
        editor.putString("X5_id", X5_id);
        editor.putString("token", result.getToken());
        editor.putString("FCMtoken", result.user.getTokenFCM());
        editor.apply();
    }

    public ShortDataUser getUser() {
        String login = encryptedSharedPreferences.getString("login", "");
        String phone = encryptedSharedPreferences.getString("phone", "");
        String userID = encryptedSharedPreferences.getString("X5_id", "");

        ShortDataUser user = new ShortDataUser();
        user.setLogin(login);
        user.setPhone(phone);
        user.setX5_Id(userID);

        return user;
    }

    public void editProfileInfo(String login, String phone) {
        SharedPreferences.Editor editor = encryptedSharedPreferences.edit();
        editor.putString("login", login);
        editor.putString("phone", phone);

        editor.apply();
    }

    public void setToPreferences(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getPreferences(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public String isGetter() {
        if (!encryptedSharedPreferences.contains("isGetter")) return null;

        return encryptedSharedPreferences.getBoolean("isGetter", false) ? "getter" : "setter";
    }

    public String getToken() {
        return encryptedSharedPreferences.getString("token", "");
    }
}
