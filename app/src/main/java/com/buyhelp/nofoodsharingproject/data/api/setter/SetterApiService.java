package com.buyhelp.nofoodsharingproject.data.api.setter;

import android.content.Context;

import com.buyhelp.nofoodsharingproject.data.RetrofitService;

public class SetterApiService {
    private static SetterAPI setterAPI;

    public static SetterAPI create(Context ctx) {
        return RetrofitService.getInstance(ctx).create(SetterAPI.class);
    }

    public static SetterAPI getInstance(Context ctx) {
        if (setterAPI == null) setterAPI = create(ctx);
        return setterAPI;
    }
}
