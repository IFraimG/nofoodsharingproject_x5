package com.buyhelp.nofoodsharingproject.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Notification {
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    @SerializedName("notificationID")
    @Expose
    private String notificationID;

    @SerializedName("typeOfUser")
    @Expose
    private String typeOfUser;

    @SerializedName("userID")
    @Expose
    private String userID;

    @SerializedName("fromUserID")
    @Expose
    private String fromUserID;

    @SerializedName("isRead")
    @Expose
    private boolean isRead;

    @SerializedName("listItems")
    @Expose
    private String[] listItems;

    @SerializedName("buttonType")
    @Expose
    private String buttonType;

    @SerializedName("advertID")
    @Expose
    private String advertID;
    public Notification() {}

    public Notification(String title, String description, String userID) {
        this.title = title;
        this.description = description;
        this.userID = userID;
    }

    public Notification(String title, String description, String createdAt, String notificationID, String typeOfUser, String userID, String fromUserID, boolean isRead, String[] listItems, String buttonType) {
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.notificationID = notificationID;
        this.typeOfUser = typeOfUser;
        this.userID = userID;
        this.fromUserID = fromUserID;
        this.isRead = isRead;
        this.listItems = listItems;
        this.buttonType = buttonType;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeOfUser() {
        return typeOfUser;
    }

    public void setTypeOfUser(String typeOfUser) {
        this.typeOfUser = typeOfUser;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFromUserID() {
        return fromUserID;
    }

    public void setFromUserID(String fromUserID) {
        this.fromUserID = fromUserID;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String[] getListItems() {
        return listItems;
    }

    public void setListItems(String[] listItems) {
        this.listItems = listItems;
    }

    public String getAdvertID() {
        return advertID;
    }

    public void setAdvertID(String advertID) {
        this.advertID = advertID;
    }
}
