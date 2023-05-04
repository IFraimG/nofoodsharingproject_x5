package com.example.nofoodsharingproject.models;

import java.util.Date;

public class ShortDataWithDate {
    String dateOfCreated = new Date().toString();
    String authorID = "";
    String advertsID = "";
    String gettingProductID = "";

    public ShortDataWithDate() {

    }

    public ShortDataWithDate(String dateOfCreated, String authorID, String advertsID, String gettingProductID) {
        this.dateOfCreated = dateOfCreated;
        this.advertsID = advertsID;
        this.authorID = authorID;
        this.gettingProductID = gettingProductID;
    }

    public String getDateOfCreated() {
        return dateOfCreated;
    }

    public void setDateOfCreated(String dateOfCreated) {
        this.dateOfCreated = dateOfCreated;
    }

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public String getAdvertsID() {
        return advertsID;
    }

    public void setAdvertsID(String advertsID) {
        this.advertsID = advertsID;
    }

    public String getGettingProductID() {
        return gettingProductID;
    }

    public void setGettingProductID(String gettingProductID) {
        this.gettingProductID = gettingProductID;
    }
}