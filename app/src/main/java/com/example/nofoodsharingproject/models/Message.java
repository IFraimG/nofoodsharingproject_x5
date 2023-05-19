package com.example.nofoodsharingproject.models;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Message {
    @SerializedName("body")
    private String body;

    @SerializedName("chatID")
    private String chatID;

    @SerializedName("_id")
    private String messageID;

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

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getMessageID() {
        return messageID;
    }

    public void createDate() {
        Calendar calendar = Calendar.getInstance();

        TimeZone moscowTimeZone = TimeZone.getTimeZone("Europe/Moscow");
        calendar.setTimeZone(moscowTimeZone);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        dateFormat.setTimeZone(moscowTimeZone);

        this.dateCreated = dateFormat.format(calendar.getTime());
    }
}
