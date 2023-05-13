package com.example.nofoodsharingproject.data.api.notifications.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestSetRead {
    @SerializedName("notificationID")
    @Expose
    String notificationID;

    public RequestSetRead() {}

    public RequestSetRead(String notificationID) {
        this.notificationID = notificationID;
    }

    public String getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(String notificationID) {
        this.notificationID = notificationID;
    }
}
