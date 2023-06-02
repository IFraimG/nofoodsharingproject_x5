package com.buyhelp.nofoodsharingproject.data.api.adverts;

import com.buyhelp.nofoodsharingproject.data.api.RetrofitService;

import javax.inject.Inject;

public class AdvertsApiService {
    private final AdvertsAPI advertsAPI;

    @Inject
    public AdvertsApiService(RetrofitService retrofitService) {
        advertsAPI = retrofitService.getInstance().create(AdvertsAPI.class);
    }

    public AdvertsAPI getAdvertsAPI() {
        return advertsAPI;
    }

}
