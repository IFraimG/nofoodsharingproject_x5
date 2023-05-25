package com.buyhelp.nofoodsharingproject.data.api.adverts.dto;

import com.google.gson.annotations.SerializedName;

public class RequestTakingProduct {
    @SerializedName("authorID")
    String authorID;

    public RequestTakingProduct() {}

    public RequestTakingProduct(String authorID) {
        this.authorID = authorID;
    }

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }
}
