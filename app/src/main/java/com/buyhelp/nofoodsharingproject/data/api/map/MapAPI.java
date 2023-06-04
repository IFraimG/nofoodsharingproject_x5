package com.buyhelp.nofoodsharingproject.data.api.map;

import com.buyhelp.nofoodsharingproject.data.api.map.dto.MarketTitleResponse;
import com.buyhelp.nofoodsharingproject.data.api.map.dto.RequestMarketInfo;
import com.buyhelp.nofoodsharingproject.data.models.Giver;
import com.buyhelp.nofoodsharingproject.data.models.Needy;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface MapAPI {
    @PUT("/giver/set_market")
    Call<Giver> setGiverMarket(@Body RequestMarketInfo requestMarketInfo);

    @PUT("/needy/set_market")
    Call<Needy> setNeedyMarket(@Body RequestMarketInfo requestMarketInfo);

    @GET("/get_pin_market")
    Call<MarketTitleResponse> getPinMarket(@Query("typeUser") String typeUser, @Query("userID") String userId);
}
