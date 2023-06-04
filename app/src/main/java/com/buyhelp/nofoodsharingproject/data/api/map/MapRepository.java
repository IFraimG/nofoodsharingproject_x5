package com.buyhelp.nofoodsharingproject.data.api.map;

import com.buyhelp.nofoodsharingproject.data.api.map.dto.RequestMarketInfo;
import com.buyhelp.nofoodsharingproject.data.models.Giver;
import com.buyhelp.nofoodsharingproject.data.models.Needy;
import com.buyhelp.nofoodsharingproject.data.api.map.dto.MarketTitleResponse;

import javax.inject.Inject;

import retrofit2.Call;

public class MapRepository {
    private final MapAPI mapAPI;
    @Inject
    public MapRepository(MapApiService mapApiService) {
        mapAPI = mapApiService.getMapAPI();
    }
    public Call<Giver> setGiverMarket(String userID, String marketName) {
        return mapAPI.setGiverMarket(new RequestMarketInfo(userID, marketName));
    }
    public Call<Needy> setNeedyMarket(String userID, String marketName) {
        return mapAPI.setNeedyMarket(new RequestMarketInfo(userID, marketName));
    }

    public Call<MarketTitleResponse> getPinMarket(String typeUser, String userID) {
        return mapAPI.getPinMarket(typeUser, userID);
    }
}
