package com.example.nofoodsharingproject.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Advertisement implements ShortDataWithDate {
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("fieldDescription")
    @Expose
    public String fieldDescription;

    @SerializedName("listProducts")
    @Expose
    public Product[] listProducts;

    @SerializedName("advertsID")
    @Expose
    public String advertsID;

    @SerializedName("authorName")
    @Expose
    public String authorName;

    @SerializedName("gettingProductID")
    @Expose
    public String gettingProductID;

    @SerializedName("authorID")
    @Expose
    public final String authorID;

    @SerializedName("dateOfCreated")
    @Expose
    public String dateOfCreated;

    @SerializedName("dateOfExpires")
    @Expose
    public String dateOfExpires;

    public Advertisement(String title, String fieldDescription, String authorID, String authorName) {
        this.title = title;
        this.fieldDescription = fieldDescription;
        this.authorID = authorID;
        this.authorName = authorName;
        // change
        this.dateOfExpires = Long.toString(new Date().getTime());
    }

//    public Advertisement(String date, String authorID, String advertsID, String gettingProductID) {
//        this.dateOfCreated = date;
//        this.authorID = authorID;
//        this.adversID = adversID;
//        this.gettingProductID = gettingProductID;
//    }

    public String getDateOfCreated() {
        return this.dateOfCreated;
    }

    public Product[] getListProducts() {
        return listProducts;
    }

    public void setAdvertsID(String advertsID) {
        this.advertsID = advertsID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFieldDescription() {
        return fieldDescription;
    }

    public void setFieldDescription(String fieldDescription) {
        this.fieldDescription = fieldDescription;
    }

    public void setListProducts(Product[] listProducts) {
        this.listProducts = listProducts;
    }

    public String getAdvertsID() {
        return advertsID;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getGettingProductID() {
        return gettingProductID;
    }

    public void setGettingProductID(String gettingProductID) {
        this.gettingProductID = gettingProductID;
    }

    public String getAuthorID() {
        return authorID;
    }

    public void setDateOfCreated(String dateOfCreated) {
        this.dateOfCreated = dateOfCreated;
    }

    public String getDateOfExpires() {
        return dateOfExpires;
    }

    public void setDateOfExpires(String dateOfExpires) {
        this.dateOfExpires = dateOfExpires;
    }
}
