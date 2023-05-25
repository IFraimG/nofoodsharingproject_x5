package com.buyhelp.nofoodsharingproject.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Getter extends User {
    @SerializedName("_id")
    @Expose
    private String X5_Id;

    public Getter() {

    }
    public Getter(String phone, String login, String password) {
        super(phone, login, password);
    }

    @Override
    public String getLogin() {
        return super.getLogin();
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getPhone() {
        return super.getPhone();
    }

    public String getX5_Id() {
        return X5_Id;
    }

    public void setX5_Id(String x5_Id) {
        this.X5_Id = x5_Id;
    }

    @Override
    public void setLogin(String login) {
        super.setLogin(login);
    }
}
