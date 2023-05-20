package com.example.nofoodsharingproject.data.api.setter;

import android.content.Context;

import com.example.nofoodsharingproject.data.RetrofitService;

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
