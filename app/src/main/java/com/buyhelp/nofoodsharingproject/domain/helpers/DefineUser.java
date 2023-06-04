package com.buyhelp.nofoodsharingproject.domain.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.buyhelp.nofoodsharingproject.data.api.auth.dto.SignUpResponseI;
import com.buyhelp.nofoodsharingproject.data.models.Giver;
import com.buyhelp.nofoodsharingproject.data.models.Needy;
import com.buyhelp.nofoodsharingproject.data.models.ShortDataUser;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.inject.Inject;
import javax.inject.Named;

public class DefineUser {
    private EncryptedSharedPreferences encryptedSharedPreferences;
    private SharedPreferences sharedPreferences;

    @Inject
    public DefineUser(@Named("application_context") Context ctx) {
        initEsp(ctx);
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

    public Giver defineGiver() {
        String login = encryptedSharedPreferences.getString("login", "");
        String phone = encryptedSharedPreferences.getString("phone", "");
        String userID = encryptedSharedPreferences.getString("X5_id", "");
        String token = encryptedSharedPreferences.getString("token", "");

        Giver user = new Giver();
        user.setLogin(login);
        user.setPhone(phone);
        user.setX5_Id(userID);
        user.setTokenJWT(token);

        return user;
    }

    public Needy defineNeedy() {
        String login = encryptedSharedPreferences.getString("login", "");
        String phone = encryptedSharedPreferences.getString("phone", "");
        String userID = encryptedSharedPreferences.getString("X5_id", "");
        String token = encryptedSharedPreferences.getString("token", "");

        Needy user = new Needy();
        user.setLogin(login);
        user.setPhone(phone);
        user.setX5_Id(userID);
        user.setTokenJWT(token);

        return user;
    }

    public Pair<String, Boolean> getTypeUser() {
        String userID = encryptedSharedPreferences.getString("X5_id", "");
        boolean isUser = encryptedSharedPreferences.getBoolean("isNeedy", false);

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

    public void saveUserDataNeedy(boolean isNeedy, String X5_id, SignUpResponseI<Needy> result) {
        saveData(isNeedy, X5_id, result.getUser().getLogin(), result.getUser().getPhone(), result.getToken(), result.user.getTokenFCM());
    }

    public void saveUserDataGiver(boolean isNeedy, String X5_id, SignUpResponseI<Giver> result) {
        saveData(isNeedy, X5_id, result.getUser().getLogin(), result.getUser().getPhone(), result.getToken(), result.user.getTokenFCM());
    }

    private void saveData(boolean isNeedy, String X5_id, String login, String phone, String token, String FCMtoken) {
        SharedPreferences.Editor editor = encryptedSharedPreferences.edit();
        editor.putBoolean("isNeedy", isNeedy);
        editor.putString("login", login);
        editor.putString("phone", phone);
        if (X5_id != null) editor.putString("X5_id", X5_id);
        else editor.putString("X5_id", "");
        editor.putString("token", token);
        editor.putString("FCMtoken", FCMtoken);
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

    public String isNeedy() {
        if (!encryptedSharedPreferences.contains("isNeedy")) return null;

        return encryptedSharedPreferences.getBoolean("isNeedy", false) ? "needy" : "giver";
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
