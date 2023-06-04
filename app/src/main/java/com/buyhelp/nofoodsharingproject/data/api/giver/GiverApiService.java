package com.buyhelp.nofoodsharingproject.data.api.giver;

import com.buyhelp.nofoodsharingproject.data.api.RetrofitService;

import javax.inject.Inject;

public class GiverApiService {
    private final GiverAPI giverAPI;

    @Inject
    public GiverApiService(RetrofitService retrofitService) {
        giverAPI = retrofitService.getInstance().create(GiverAPI.class);
    }

    public GiverAPI getGiverAPI() {
        return giverAPI;
    }
}
