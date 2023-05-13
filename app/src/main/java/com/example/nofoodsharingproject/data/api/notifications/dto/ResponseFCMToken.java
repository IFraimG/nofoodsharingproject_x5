package com.example.nofoodsharingproject.data.api.notifications.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseFCMToken {
    @SerializedName("fcmToken")
    @Expose
    String fcmToken;

    public ResponseFCMToken() {}

    public ResponseFCMToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

}
