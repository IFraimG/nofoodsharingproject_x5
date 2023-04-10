package com.example.nofoodsharingproject.models;

import java.util.Date;

public class Advertisement implements ShortDataWithDate {
    public String title;
    public String fieldDescription;
    public Product[] listProducts;
    public String adversID;
    public String authorName;
    public String gettingProductID;
    public final String authorID;
    public Date dateOfCreated;
    public Date dateOfExpires;

    public Advertisement(String title, String fieldDescription, String authorID, String authorName) {
        this.title = title;
        this.fieldDescription = fieldDescription;
        this.authorID = authorID;
        this.authorName = authorName;
    }

    public Advertisement(Date date, String authorID, String adversID, String gettingProductID) {
        this.dateOfCreated = date;
        this.authorID = authorID;
        this.adversID = adversID;
        this.gettingProductID = gettingProductID;
    }

    public Date getDateOfCreated() {
        return this.dateOfCreated;
    }
}
