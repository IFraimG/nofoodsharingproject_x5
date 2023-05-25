package com.buyhelp.nofoodsharingproject.data.api.getter.dto;

import com.google.gson.annotations.SerializedName;

public class RequestGetterEditProfile {
    @SerializedName("userID")
    String userID;

    @SerializedName("login")
    String login;

    @SerializedName("phone")
    String phone;

    @SerializedName("password")
    String password;

    @SerializedName("old_password")
    String oldPassword;

    public RequestGetterEditProfile() {}

    public RequestGetterEditProfile(String userID, String login, String phone, String password, String oldPassword) {
        this.userID = userID;
        this.login = login;
        this.phone = phone;
        this.password = password;
        this.oldPassword = oldPassword;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
