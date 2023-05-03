package com.example.nofoodsharingproject.data.api.adverts;

import com.example.nofoodsharingproject.data.RetrofitService;

public class AdvertsApiService {
    private static AdvertsAPI advertsAPI;

    public static AdvertsAPI create() {
        return RetrofitService.getInstance().create(AdvertsAPI.class);
    }

    public static AdvertsAPI getInstance() {
        if (advertsAPI == null) advertsAPI = create();
        return advertsAPI;
    }

}
