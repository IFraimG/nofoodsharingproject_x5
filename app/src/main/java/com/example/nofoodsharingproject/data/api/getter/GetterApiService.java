package com.example.nofoodsharingproject.data.api.getter;

import com.example.nofoodsharingproject.data.RetrofitService;

public class GetterApiService {
    private static GetterAPI getterAPI;

    public static GetterAPI create() {
        return RetrofitService.getInstance().create(GetterAPI.class);
    }

    public static GetterAPI getInstance() {
        if (getterAPI == null) getterAPI = create();
        return getterAPI;
    }
}
