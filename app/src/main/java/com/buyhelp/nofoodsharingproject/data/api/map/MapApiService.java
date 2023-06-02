package com.buyhelp.nofoodsharingproject.data.api.map;

import com.buyhelp.nofoodsharingproject.data.api.RetrofitService;

import javax.inject.Inject;

public class MapApiService {
    private final MapAPI mapAPI;

    @Inject
    public MapApiService(RetrofitService retrofitService) {
        mapAPI = retrofitService.getInstance().create(MapAPI.class);
    }

    public MapAPI getMapAPI() {
        return mapAPI;
    }
}
