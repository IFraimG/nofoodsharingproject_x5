package com.example.nofoodsharingproject.data.api.auth.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUpResponseI<T> {
    @SerializedName("token")
    @Expose
    public String token;

    @SerializedName("user")
    @Expose
    public T user;

    public SignUpResponseI(String token, T user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public T getUser() {
        return user;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUser(T user) {
        this.user = user;
    }
}
