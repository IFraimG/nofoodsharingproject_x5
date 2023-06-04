package com.buyhelp.nofoodsharingproject.data.api.needy.dto;

import com.google.gson.annotations.SerializedName;

public class RequestChangeToken {
    @SerializedName("userID")
    private String userID;

    @SerializedName("fcmToken")
    private String fcmToken;

    public RequestChangeToken() {}

    public RequestChangeToken(String userID, String fcmToken) {
        this.userID = userID;
        this.fcmToken = fcmToken;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
