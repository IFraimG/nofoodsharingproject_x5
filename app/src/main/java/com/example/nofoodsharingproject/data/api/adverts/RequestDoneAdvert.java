package com.example.nofoodsharingproject.data.api.adverts;

public class RequestDoneAdvert {
    String advertID;
    String userID;

    public RequestDoneAdvert(String advertID, String userID) {
        this.advertID = advertID;
        this.userID = userID;
    }

    public String getAdvertID() {
        return advertID;
    }

    public void setAdvertID(String advertID) {
        this.advertID = advertID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
