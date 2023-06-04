package com.buyhelp.nofoodsharingproject.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Giver extends User {
    @SerializedName("id")
    @Expose
    private String X5_Id;
    @SerializedName("successHistory")
    @Expose
    private Advertisement[] successHistory;

    @SerializedName("authID")
    private String authID;

    public Giver() {}

    public Giver(String phone, String login, String password) {
        super(phone, login, password);
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

    public void setAuthID(String authID) {
        this.authID = authID;
    }

    public String getAuthID() {
        return authID;
    }
}
