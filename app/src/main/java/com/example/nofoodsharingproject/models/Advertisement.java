package com.example.nofoodsharingproject.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Advertisement {
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("fieldDescription")
    @Expose
    public String fieldDescription;

    @SerializedName("listProducts")
    @Expose
    public ArrayList<Product> listProducts;

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

    public Advertisement(String title, String authorID, String authorName) {
        this.title = title;
        this.authorID = authorID;
        this.authorName = authorName;
        // change
        this.dateOfExpires = Long.toString(new Date().getTime());
    }

    public static String generateID() {
        String alphabet = "абвгдежзиклмнопрстухцчшщэюя";
        int length = 4;
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
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

    public ArrayList<Product> getListProducts() {
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

    public void setListProducts(List<String> listProducts) {
        this.listProducts = new ArrayList<>();
        for (int i = 0 ; i < listProducts.size(); i++) {
            this.listProducts.add(new Product(listProducts.get(i)));
        }
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
