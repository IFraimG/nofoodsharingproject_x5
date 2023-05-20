package com.example.nofoodsharingproject.data.api.getter;

import android.content.Context;

import com.example.nofoodsharingproject.data.RetrofitService;

public class GetterApiService {
    private static GetterAPI getterAPI;

    public static GetterAPI create(Context ctx) {
        return RetrofitService.getInstance(ctx).create(GetterAPI.class);
    }

    public static GetterAPI getInstance(Context ctx) {
        if (getterAPI == null) getterAPI = create(ctx);
        return getterAPI;
    }
}
