package com.example.nofoodsharingproject.data.api.map;

import com.example.nofoodsharingproject.data.api.map.MapApiService;
import com.example.nofoodsharingproject.data.api.map.dto.RequestMarketInfo;
import com.example.nofoodsharingproject.models.Getter;
import com.example.nofoodsharingproject.models.Setter;
import com.example.nofoodsharingproject.data.api.map.dto.MarketTitleResponse;

import retrofit2.Call;

public class MapRepository {
    public static Call<Setter> setSetterMarket(String userID, String marketName) {
        return MapApiService.getInstance().setSetterMarket(new RequestMarketInfo(userID, marketName));
    }
    public static Call<Getter> setGetterMarket(String userID, String marketName) {
        return MapApiService.getInstance().setGetterMarket(new RequestMarketInfo(userID, marketName));
    }

    public static Call<MarketTitleResponse> getPinMarket(String typeUser, String userID) {
        return MapApiService.getInstance().getPinMarket(typeUser, userID);
    }
}
