package com.buyhelp.nofoodsharingproject.data.api.adverts;

import android.content.Context;

import com.buyhelp.nofoodsharingproject.data.RetrofitService;

public class AdvertsApiService {
    private static AdvertsAPI advertsAPI;

    public static AdvertsAPI create(Context ctx) {
        return RetrofitService.getInstance(ctx).create(AdvertsAPI.class);
    }

    public static AdvertsAPI getInstance(Context ctx) {
        if (advertsAPI == null) advertsAPI = create(ctx);
        return advertsAPI;
    }

}
