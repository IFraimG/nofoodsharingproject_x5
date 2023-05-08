package com.example.nofoodsharingproject.data.repository;

import com.example.nofoodsharingproject.data.api.auth.AuthApiService;
import com.example.nofoodsharingproject.data.api.auth.interfaces.SignUpInformation;
import com.example.nofoodsharingproject.data.api.auth.interfaces.SignUpResponseI;
import com.example.nofoodsharingproject.data.api.map.MapApiService;
import com.example.nofoodsharingproject.data.api.map.RequestMarketInfo;
import com.example.nofoodsharingproject.models.Getter;
import com.example.nofoodsharingproject.models.Setter;

import retrofit2.Call;
import retrofit2.http.Body;

public class MapRepository {
    public static Call<Setter> setSetterMarket(String userID, String marketName) {
        return MapApiService.getInstance().setSetterMarket(new RequestMarketInfo(userID, marketName));
    }
    public static Call<Getter> setGetterMarket(String userID, String marketName) {
        return MapApiService.getInstance().setGetterMarket(new RequestMarketInfo(userID, marketName));
    }

    public static Call<String> getPinMarket(String typeUser, String userID) {
        return MapApiService.getInstance().getPinMarket(typeUser, userID);
    }
}
