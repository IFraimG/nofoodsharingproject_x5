package com.buyhelp.nofoodsharingproject.data.api.auth.dto;

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
