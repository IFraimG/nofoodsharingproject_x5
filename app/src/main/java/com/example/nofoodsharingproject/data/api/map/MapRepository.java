package com.example.nofoodsharingproject.data.api.map;

import android.content.Context;

import com.example.nofoodsharingproject.data.api.map.MapApiService;
import com.example.nofoodsharingproject.data.api.map.dto.RequestMarketInfo;
import com.example.nofoodsharingproject.models.Getter;
import com.example.nofoodsharingproject.models.Setter;
import com.example.nofoodsharingproject.data.api.map.dto.MarketTitleResponse;

import retrofit2.Call;

public class MapRepository {
    public static Call<Setter> setSetterMarket(Context ctx, String userID, String marketName) {
        return MapApiService.getInstance(ctx).setSetterMarket(new RequestMarketInfo(userID, marketName));
    }
    public static Call<Getter> setGetterMarket(Context ctx, String userID, String marketName) {
        return MapApiService.getInstance(ctx).setGetterMarket(new RequestMarketInfo(userID, marketName));
    }

    public static Call<MarketTitleResponse> getPinMarket(Context ctx, String typeUser, String userID) {
        return MapApiService.getInstance(ctx).getPinMarket(typeUser, userID);
    }
}
