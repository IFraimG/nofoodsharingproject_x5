package com.buyhelp.nofoodsharingproject.data.api.adverts.dto;

import com.buyhelp.nofoodsharingproject.data.models.Advertisement;
import com.google.gson.annotations.SerializedName;

public class ResponseActiveAdverts {
    @SerializedName("result")
    Advertisement[] advertisements;

    public ResponseActiveAdverts() {}

    public ResponseActiveAdverts(Advertisement[] advertisementList) {
        this.advertisements = advertisementList;
    }

    public Advertisement[] getAdvertisements() {
        return advertisements;
    }

    public void setAdvertisements(Advertisement[] advertisements) {
        this.advertisements = advertisements;
    }
}
