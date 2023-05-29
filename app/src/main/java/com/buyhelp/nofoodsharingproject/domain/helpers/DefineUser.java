package com.buyhelp.nofoodsharingproject.domain.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.buyhelp.nofoodsharingproject.data.api.auth.dto.SignUpResponseI;
import com.buyhelp.nofoodsharingproject.data.models.Getter;
import com.buyhelp.nofoodsharingproject.data.models.Setter;
import com.buyhelp.nofoodsharingproject.data.models.ShortDataUser;
import com.buyhelp.nofoodsharingproject.data.models.User;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class DefineUser<T extends User> {
    private EncryptedSharedPreferences encryptedSharedPreferences;
    private SharedPreferences sharedPreferences;

    public DefineUser(Activity activity) {
        initEsp(activity);
    }
    public DefineUser(Context ctx) {
        initEsp(ctx);
    }

    public DefineUser(EncryptedSharedPreferences esp) {
        encryptedSharedPreferences = esp;
    }
    public DefineUser(SharedPreferences sp) {
        sharedPreferences = sp;
    }

    public void initBaseRetrofitPath(String path) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String result = "http://" + path + ":8080";
        if (path.length() > 0) editor.putString("server_url", result).apply();
        else editor.putString("server_url", "https://buy-help-server.onrender.com").apply();
    }

    public String getBaseForRetrofit() {
        return sharedPreferences.getString("server_url", "https://buy-help-server.onrender.com");
    }

    public void setDefaultBasePathForRetrofit() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("server_url", "https://buy-help-server.onrender.com").apply();
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

    public void initEsp(Context ctx) {
        try {
            MasterKey masterKey = new MasterKey.Builder(ctx, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            encryptedSharedPreferences = (EncryptedSharedPreferences) EncryptedSharedPreferences.create(ctx, "user", masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        } catch (IOException | GeneralSecurityException err) {
            Log.e("auth error", err.toString());
            err.printStackTrace();
        }
        sharedPreferences = ctx.getSharedPreferences("prms", Context.MODE_PRIVATE);
    }

    public Setter defineSetter() {
        String login = encryptedSharedPreferences.getString("login", "");
        String phone = encryptedSharedPreferences.getString("phone", "");
        String userID = encryptedSharedPreferences.getString("X5_id", "");
        String token = encryptedSharedPreferences.getString("token", "");

        Setter user = new Setter();
        user.setLogin(login);
        user.setPhone(phone);
        user.setX5_Id(userID);
        user.setTokenJWT(token);

        return user;
    }

    public Getter defineGetter() {
        String login = encryptedSharedPreferences.getString("login", "");
        String phone = encryptedSharedPreferences.getString("phone", "");
        String userID = encryptedSharedPreferences.getString("X5_id", "");
        String token = encryptedSharedPreferences.getString("token", "");

        Getter user = new Getter();
        user.setLogin(login);
        user.setPhone(phone);
        user.setX5_Id(userID);
        user.setTokenJWT(token);

        return user;
    }

    public Pair<String, Boolean> getTypeUser() {
        String userID = encryptedSharedPreferences.getString("X5_id", "");
        boolean isUser = encryptedSharedPreferences.getBoolean("isGetter", false);

        return new Pair<>(userID, isUser);
    }

    public void clearData() {
        if (encryptedSharedPreferences != null) {
            SharedPreferences.Editor editor = encryptedSharedPreferences.edit();
            editor.clear();
            editor.apply();
        }

        if (sharedPreferences != null) sharedPreferences.edit().clear().apply();
    }

    public void saveUserData(boolean isGetter, String X5_id, SignUpResponseI<T> result) {
        SharedPreferences.Editor editor = encryptedSharedPreferences.edit();
        editor.putBoolean("isGetter", isGetter);
        editor.putString("login", result.user.getLogin());
        editor.putString("phone", result.user.getPhone());
        if (X5_id != null) editor.putString("X5_id", X5_id);
        else editor.putString("X5_id", "");
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

    public String getFCMToken() {
        return encryptedSharedPreferences.getString("FCMtoken", "");
    }

    public void changeFCMtoken(String fcmToken) {
        encryptedSharedPreferences.edit().putString("FCMtoken", fcmToken).apply();
    }

    public boolean getIsLocation() {
        return sharedPreferences.getBoolean("locaiton", true);
    }
}
