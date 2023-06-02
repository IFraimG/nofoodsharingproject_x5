package com.buyhelp.nofoodsharingproject.data.api.map;

import android.content.Context;

import com.buyhelp.nofoodsharingproject.data.api.map.dto.RequestMarketInfo;
import com.buyhelp.nofoodsharingproject.data.models.Getter;
import com.buyhelp.nofoodsharingproject.data.models.Setter;
import com.buyhelp.nofoodsharingproject.data.api.map.dto.MarketTitleResponse;

import javax.inject.Inject;

import retrofit2.Call;

public class MapRepository {
    private final MapAPI mapAPI;
    @Inject
    public MapRepository(MapApiService mapApiService) {
        mapAPI = mapApiService.getMapAPI();
    }
    public Call<Setter> setSetterMarket(String userID, String marketName) {
        return mapAPI.setSetterMarket(new RequestMarketInfo(userID, marketName));
    }
    public Call<Getter> setGetterMarket(String userID, String marketName) {
        return mapAPI.setGetterMarket(new RequestMarketInfo(userID, marketName));
    }

    public Call<MarketTitleResponse> getPinMarket(String typeUser, String userID) {
        return mapAPI.getPinMarket(typeUser, userID);
    }
}
