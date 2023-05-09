package com.example.nofoodsharingproject.data.api.adverts;

import com.example.nofoodsharingproject.models.Advertisement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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
