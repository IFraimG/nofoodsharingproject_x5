package com.buyhelp.nofoodsharingproject.data.models;

import com.google.gson.annotations.SerializedName;

public class Chat {
    @SerializedName("title")
    private String title;

    @SerializedName("_id")
    private String chatID;

    @SerializedName("users")
    private String[] users;

    @SerializedName("dateCreated")
    private String dateCreated;

    public Chat() {}

    public Chat(String title, String chatID, String[] users, String dateCreated) {
        this.title = title;
        this.chatID = chatID;
        this.users = users;
        this.dateCreated = dateCreated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public void setUsers(String[] users) {
        this.users = users;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}