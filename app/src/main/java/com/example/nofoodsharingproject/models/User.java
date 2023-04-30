package com.example.nofoodsharingproject.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public abstract class User {
    @SerializedName("phone")
    @Expose
    String phone;

    @SerializedName("login")
    @Expose
    String login;

    @SerializedName("password")
    @Expose
    String password;

    Date dateOfCreated;

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
}
