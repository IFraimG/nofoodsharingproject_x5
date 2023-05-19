package com.example.nofoodsharingproject.models;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("body")
    private String body;

    @SerializedName("chatID")
    private String chatID;

    @SerializedName("authorID")
    private String authorID;

    @SerializedName("dateCreated")
    private String dateCreated;

    public Message() {}

    public Message(String body, String chatID, String authorID, String dateCreated) {
        this.body = body;
        this.chatID = chatID;
        this.authorID = authorID;
        this.dateCreated = dateCreated;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
