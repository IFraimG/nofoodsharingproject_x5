package com.example.nofoodsharingproject.models;

import com.google.gson.annotations.SerializedName;

public class Chat {
    // имя пользователя, которому ты пишешь
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

    public String[] getUsers() {
        return users;
    }

    public void setUsers(String[] users) {
        this.users = users;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}