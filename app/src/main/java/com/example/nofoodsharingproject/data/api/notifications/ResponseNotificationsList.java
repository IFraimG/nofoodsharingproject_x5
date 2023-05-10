package com.example.nofoodsharingproject.data.api.notifications;

import com.example.nofoodsharingproject.models.Notification;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseNotificationsList {
    @SerializedName("result")
    @Expose
    Notification[] result;

    public ResponseNotificationsList() {}

    public ResponseNotificationsList(Notification[] notifications) {
        this.result = notifications;
    }

    public Notification[] getResult() {
        return result;
    }

    public void setResult(Notification[] result) {
        this.result = result;
    }
}
