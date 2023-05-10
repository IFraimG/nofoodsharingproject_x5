package com.example.nofoodsharingproject.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Advertisement {
    @SerializedName("title")
    @Expose
    private String title;

    private ArrayList<Product> fullListProducts;

    @SerializedName("listProducts")
    @Expose
    private String[] listProducts;

    @SerializedName("advertsID")
    @Expose
    private String advertsID;

    @SerializedName("authorName")
    @Expose
    private String authorName;

    @SerializedName("gettingProductID")
    @Expose
    private String gettingProductID;

    @SerializedName("authorID")
    @Expose
    private final String authorID;

    @SerializedName("dateOfCreated")
    @Expose
    private String dateOfCreated;

    @SerializedName("dateOfExpires")
    @Expose
    private String dateOfExpires;

    @SerializedName("isSuccessDone")
    @Expose
    private boolean isDone = false;

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

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public void setListProducts(String[] listProducts) {
        this.listProducts = listProducts;
    }

    public void setListProductsCustom(List<String> listProducts) {
        this.listProducts = new String[listProducts.size()];
        for (int i = 0; i < listProducts.size(); i++) {
            this.listProducts[i] = listProducts.get(i);
        }
    }

    public ArrayList<Product> getFullListProducts() {
        return fullListProducts;
    }

    public void setFullListProducts(ArrayList<Product> fullListProducts) {
        this.fullListProducts = fullListProducts;
    }

    public String getDateOfCreated() {
        return this.dateOfCreated;
    }

    public String[] getListProducts() {
        return listProducts;
    }

    public String[] getListTitleProducts() {
//        String[] arr = new String[this.listProducts.size()];
//        for (int i = 0; i < listProducts.size(); i++) arr[i] = listProducts.get(i).getTitle();
        return getListProducts();
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
