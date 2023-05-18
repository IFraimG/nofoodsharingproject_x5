package com.example.nofoodsharingproject.data.api.auth;

import com.example.nofoodsharingproject.data.RetrofitService;
import com.example.nofoodsharingproject.data.api.adverts.AdvertsAPI;

public class AuthApiService {
    private static AuthAPI authAPI;

    public static AuthAPI create() {

        return RetrofitService.getInstance().create(AuthAPI.class);
    }

    public static AuthAPI getInstance() {
        if (authAPI == null) authAPI = create();
        return authAPI;
    }
}
