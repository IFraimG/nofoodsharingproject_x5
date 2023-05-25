package com.buyhelp.nofoodsharingproject.data.api.adverts.dto;

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
