package com.example.nofoodsharingproject.data.api.adverts.dto;

import com.google.gson.annotations.SerializedName;

public class RequestDoneAdvert {
//    String advertID;
    @SerializedName("userDoneID")
    String userDoneID;

    @SerializedName("authorID")
    String authorID;
    @SerializedName("gettingProductID")
    String gettingProductID;

    public RequestDoneAdvert() {}

    public RequestDoneAdvert(String authorID, String userDoneID, String gettingProductID) {
//        this.advertID = advertID;
        this.authorID = authorID;
        this.userDoneID = userDoneID;
        this.gettingProductID = gettingProductID;
    }

//    public String getAdvertID() {
//        return advertID;
//    }
//
//    public void setAdvertID(String advertID) {
//        this.advertID = advertID;
//    }


    public String getAuthorID() {
        return authorID;
    }

    public String getGettingProductID() {
        return gettingProductID;
    }

    public String getUserDoneID() {
        return userDoneID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public void setUserDoneID(String userDoneID) {
        this.userDoneID = userDoneID;
    }

    public void setGettingProductID(String gettingProductID) {
        this.gettingProductID = gettingProductID;
    }

    public String getUserID() {
        return userDoneID;
    }

    public void setUserID(String userDoneID) {
        this.userDoneID = userDoneID;
    }
}
