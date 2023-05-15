package com.example.nofoodsharingproject.data.api.setter;

import com.example.nofoodsharingproject.data.RetrofitService;

public class SetterApiService {
    private static SetterAPI setterAPI;

    public static SetterAPI create() {
        return RetrofitService.getInstance().create(SetterAPI.class);
    }

    public static SetterAPI getInstance() {
        if (setterAPI == null) setterAPI = create();
        return setterAPI;
    }
}
