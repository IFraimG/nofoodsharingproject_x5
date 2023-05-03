package com.example.nofoodsharingproject.data.api.adverts;

import com.example.nofoodsharingproject.models.Advertisement;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface AdvertsAPI {
    @GET("/advertisements/get_active")
    Call<List<Advertisement>> getListAdvertisements();

    @POST("/advertisements/create")
    Call<Advertisement> createAdvert(@Body Advertisement advert);

    @PUT("/setters/done")
    Call<RequestDoneAdvert> makeDoneAdvert (@Body RequestDoneAdvert req);
}
