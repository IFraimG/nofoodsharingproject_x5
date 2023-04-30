package com.example.nofoodsharingproject.data.api.auth.interfaces;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckAuthI {
    @SerializedName("isAuth")
    @Expose
    boolean isAuth = false;

    public void setAuth(boolean auth) {
        isAuth = auth;
    }

    public boolean getIsAuth() {
        return this.isAuth;
    }

}
