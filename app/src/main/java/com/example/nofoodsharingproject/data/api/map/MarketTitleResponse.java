package com.example.nofoodsharingproject.data.api.map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MarketTitleResponse {
    @SerializedName("market")
    @Expose
    public String market;

    public MarketTitleResponse() {}

    public MarketTitleResponse(String market) {
        this.market = market;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }
}
