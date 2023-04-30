package com.example.nofoodsharingproject.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Setter extends User {
    @SerializedName("_id")
    @Expose
    String X5_Id;

    @SerializedName("successHistory")
    @Expose
    Advertisement[] successHistory;

    public Setter(String phone, String login, String password) {
        super(phone, login, password);
    }

    public String makeHelp(String adversID) {

        return "cлучайно сгенерированный айди до 6 символов из русских букв";
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

    public Advertisement[] getSuccessHistory() {
        return successHistory;
    }

    public void setSuccessHistory(Advertisement[] successHistory) {
        this.successHistory = successHistory;
    }
}
