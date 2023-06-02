package com.buyhelp.nofoodsharingproject.data.api.getter;

import com.buyhelp.nofoodsharingproject.data.api.RetrofitService;


public class GetterApiService {
    private final GetterAPI getterAPI;

    public GetterApiService(RetrofitService retrofitService) {
        getterAPI = retrofitService.getInstance().create(GetterAPI.class);
    }

    public GetterAPI getGetterAPI() {
        return getterAPI;
    }
}
