package com.buyhelp.nofoodsharingproject.data.api.auth;

import android.content.Context;

import com.buyhelp.nofoodsharingproject.data.api.RetrofitService;

public class AuthApiService {
    private static AuthAPI authAPI;

    public static AuthAPI create(Context ctx) {
        return RetrofitService.getInstance(ctx).create(AuthAPI.class);
    }

    public static AuthAPI getInstance(Context ctx) {
        if (authAPI == null) authAPI = create(ctx);
        return authAPI;
    }
}
