package com.example.nofoodsharingproject.data.api.map.dto;

public class RequestMarketInfo {
    String userID;
    String market;

    public RequestMarketInfo() {

    }

    public RequestMarketInfo(String userID, String market) {
        this.userID = userID;
        this.market = market;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }
}
