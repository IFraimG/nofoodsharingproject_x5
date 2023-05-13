package com.example.nofoodsharingproject.data.api.adverts.dto;

import com.example.nofoodsharingproject.models.Advertisement;
import com.google.gson.annotations.SerializedName;

public class ResponseHistoryAdverts {
    @SerializedName("advertisements")
    Advertisement[] advertisements;

    public ResponseHistoryAdverts() {}

    public ResponseHistoryAdverts(Advertisement[] advertisements) {
        this.advertisements = advertisements;
    }

    public Advertisement[] getAdvertisements() {
        return advertisements;
    }

    public void setAdvertisements(Advertisement[] advertisements) {
        this.advertisements = advertisements;
    }
}
