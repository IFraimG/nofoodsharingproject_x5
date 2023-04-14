package com.example.nofoodsharingproject.data.api.adverts;

import com.example.nofoodsharingproject.models.Advertisement;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AdvertsAPI {
    @GET("/advertisements/get_active")
    Call<List<Advertisement>> getListAdvertisements();
}
