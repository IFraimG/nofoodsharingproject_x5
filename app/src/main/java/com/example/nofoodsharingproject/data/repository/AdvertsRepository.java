package com.example.nofoodsharingproject.data.repository;


import com.example.nofoodsharingproject.data.api.adverts.AdvertsApiService;
import com.example.nofoodsharingproject.data.api.adverts.RequestDoneAdvert;
import com.example.nofoodsharingproject.models.Advertisement;

import java.util.List;

import retrofit2.Call;

public class AdvertsRepository {

    public static Call<List<Advertisement>> getListAdverts() {
        return AdvertsApiService.getInstance().getListAdvertisements();
    }

    public static Call<Advertisement> createAdvert(Advertisement advert) {
        return AdvertsApiService.getInstance().createAdvert(advert);
    }

    public static Call<RequestDoneAdvert> makeDoneAdvert(RequestDoneAdvert req) {
        return AdvertsApiService.getInstance().makeDoneAdvert(req);
    }
}
