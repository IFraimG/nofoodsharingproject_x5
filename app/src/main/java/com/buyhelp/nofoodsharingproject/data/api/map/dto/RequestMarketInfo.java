package com.buyhelp.nofoodsharingproject.data.api.map.dto;

import com.google.gson.annotations.SerializedName;

public class RequestMarketInfo {
    @SerializedName("userID")
    private String userID;

    @SerializedName("market")
    private String market;

    public RequestMarketInfo() {}

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
