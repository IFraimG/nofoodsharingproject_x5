package com.example.nofoodsharingproject.models;

import java.util.Date;
import java.util.UUID;

public class Notification {
    public String title;
    public String description;
    public String link;
    public String createdAt;
    String notificationID;

    public Notification(String title, String description, String link) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.notificationID = UUID.randomUUID().toString();
    }

    public Notification(String title, String description, String link, Date createdAt) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.createdAt = createdAt.toString();
        this.notificationID = UUID.randomUUID().toString();
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt.toString();
    }

    public String getNotificationID() {
        return notificationID;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }
}
