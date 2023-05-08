package com.example.nofoodsharingproject.data.api.map;

import com.example.nofoodsharingproject.models.Getter;
import com.example.nofoodsharingproject.models.Setter;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MapAPI {
    @PUT("/setter/set_market")
    Call<Setter> setSetterMarket(@Body RequestMarketInfo requestMarketInfo);

    @PUT("/getter/set_market")
    Call<Getter> setGetterMarket(@Body RequestMarketInfo requestMarketInfo);

    @GET("/get_pin_market")
    Call<String> getPinMarket(@Query("typeUser") String typeUser, @Query("userID") String userId);
}
