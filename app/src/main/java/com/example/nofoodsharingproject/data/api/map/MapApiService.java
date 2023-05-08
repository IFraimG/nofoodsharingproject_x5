package com.example.nofoodsharingproject.data.api.map;

import com.example.nofoodsharingproject.data.RetrofitService;
import com.example.nofoodsharingproject.data.api.auth.AuthAPI;

public class MapApiService {
    private static MapAPI mapAPI;

    public static MapAPI create() {
        return RetrofitService.getInstance().create(MapAPI.class);
    }

    public static MapAPI getInstance() {
        if (mapAPI == null) mapAPI = create();
        return mapAPI;
    }
}
