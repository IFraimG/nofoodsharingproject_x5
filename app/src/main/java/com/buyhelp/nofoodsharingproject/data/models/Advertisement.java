package com.buyhelp.nofoodsharingproject.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Advertisement {
    @SerializedName("title")
    @Expose
    private String title;

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
    private String authorID;

    @SerializedName("dateOfCreated")
    @Expose
    private String dateOfCreated;

    @SerializedName("dateOfExpires")
    @Expose
    private String dateOfExpires;

    @SerializedName("isSuccessDone")
    @Expose
    private boolean isDone = false;

    @SerializedName("userDoneID")
    @Expose
    private String userDoneID;

    @SerializedName("dateDone")
    @Expose
    private String dateDone;

    private List<String> listArrayProducts;

    public Advertisement() {}

    public Advertisement(String title, String authorID, String authorName) {
        this.title = title;
        this.authorID = authorID;
        this.authorName = authorName;
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

    public boolean isDone() {
        return isDone;
    }

    public void setListProductsCustom(List<String> listProducts) {
        this.listProducts = new String[listProducts.size()];
        for (int i = 0; i < listProducts.size(); i++) {
            this.listProducts[i] = listProducts.get(i);
        }
    }

    public String getDateOfCreated() {
        return this.dateOfCreated;
    }

    public String[] getListProducts() {
        return listProducts;
    }

    public List<String> getListTitleProducts() {
        listArrayProducts = new ArrayList<>();
        listArrayProducts.addAll(Arrays.asList(listProducts));
        return listArrayProducts;
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
        if (authorName == null) return "";
        return authorName;
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

    public String getUserDoneID() {
        return userDoneID;
    }

    public String getDateDone() {
        if (dateDone == null) return "";
        return dateDone;
    }
}
