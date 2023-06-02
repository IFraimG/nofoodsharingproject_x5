package com.buyhelp.nofoodsharingproject.data.api.setter;

import com.buyhelp.nofoodsharingproject.data.api.RetrofitService;

import javax.inject.Inject;

public class SetterApiService {
    private final SetterAPI setterAPI;

    @Inject
    public SetterApiService(RetrofitService retrofitService) {
        setterAPI = retrofitService.getInstance().create(SetterAPI.class);
    }

    public SetterAPI getSetterAPI() {
        return setterAPI;
    }
}
