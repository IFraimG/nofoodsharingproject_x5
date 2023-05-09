package com.example.nofoodsharingproject.data.api.adverts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseDeleteAdvert {
    @SerializedName("isDelete")
    @Expose
    public boolean isDelete;

    public ResponseDeleteAdvert() {}
    public ResponseDeleteAdvert(boolean isDelete) {
        this.isDelete = isDelete;
    }


    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }
}
