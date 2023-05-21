package com.example.nofoodsharingproject.data.api.map;

import android.content.Context;

import com.example.nofoodsharingproject.data.RetrofitService;

public class MapApiService {
    private static MapAPI mapAPI;

    public static MapAPI create(Context ctx) {
        return RetrofitService.getInstance(ctx).create(MapAPI.class);
    }

    public static MapAPI getInstance(Context ctx) {
        if (mapAPI == null) mapAPI = create(ctx);
        return mapAPI;
    }
}
