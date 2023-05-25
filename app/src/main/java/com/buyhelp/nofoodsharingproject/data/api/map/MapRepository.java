package com.buyhelp.nofoodsharingproject.data.api.map;

import android.content.Context;

import com.buyhelp.nofoodsharingproject.data.api.map.MapApiService;
import com.buyhelp.nofoodsharingproject.data.api.map.dto.RequestMarketInfo;
import com.buyhelp.nofoodsharingproject.models.Getter;
import com.buyhelp.nofoodsharingproject.models.Setter;
import com.buyhelp.nofoodsharingproject.data.api.map.dto.MarketTitleResponse;

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
