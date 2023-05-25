package com.buyhelp.nofoodsharingproject.data.api.notifications.dto;

public class FMCMessage {
    private String to;
    private FMCNotification notification;

    public FMCMessage() {}

    public FMCMessage(String to, FMCNotification notification) {
        this.to = to;
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public FMCNotification getNotification() {
        return notification;
    }

    public void setNotification(FMCNotification notification) {
        this.notification = notification;
    }
}
