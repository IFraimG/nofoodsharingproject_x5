package com.example.nofoodsharingproject.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public abstract class User {
    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("login")
    @Expose
    private String login;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("market")
    @Expose
    private String marketName;

    @SerializedName("fcmToken")
    @Expose
    private String tokenFCM;

    private String tokenJWT;

    public User() {}

    public User(String phone, String login, String password) {
        this.login = login;
        this.phone = phone;
        this.password = password;
    }

    public User(String phone, String login) {
        this.phone = phone;
        this.login = login;
    }

    public String getPhone() {
        return phone;
    }

    public String getLogin() {
        return login;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setTokenFCM(String tokenFCM) {
        this.tokenFCM = tokenFCM;
    }

    public String getTokenFCM() {
        return tokenFCM;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTokenJWT() {
        return tokenJWT;
    }

    public void setTokenJWT(String tokenJWT) {
        this.tokenJWT = tokenJWT;
    }
}
