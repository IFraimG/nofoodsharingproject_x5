package com.buyhelp.nofoodsharingproject.data.api.auth;

import com.buyhelp.nofoodsharingproject.data.api.RetrofitService;

import javax.inject.Inject;

public class AuthApiService {
    private final AuthAPI authAPI;

    @Inject
    public AuthApiService(RetrofitService retrofitService) {
        authAPI = retrofitService.getInstance().create(AuthAPI.class);
    }

    public AuthAPI getAuthAPI() {
        return authAPI;
    }
}
