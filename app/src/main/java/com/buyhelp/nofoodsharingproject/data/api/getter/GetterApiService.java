package com.buyhelp.nofoodsharingproject.data.api.getter;

import android.content.Context;

import com.buyhelp.nofoodsharingproject.data.api.RetrofitService;

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
