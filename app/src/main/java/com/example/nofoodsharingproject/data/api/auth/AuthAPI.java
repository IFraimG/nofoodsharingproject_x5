package com.example.nofoodsharingproject.data.api.auth;

import com.example.nofoodsharingproject.models.Getter;
import com.example.nofoodsharingproject.models.Setter;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthAPI {
    @POST("/auth/setter/login")
    Call<Setter> setterLogin(@Body String login, @Body String password);

    @POST("/auth/getter/login")
    Call<Getter> getterLogin(@Body String login, @Body String password);

    @POST("/auth/setter/registration")
    Call<Setter> setterRegistration(@Body String phone, @Body String login, @Body String hashedPassword);

    @POST("/auth/getter/registration")
    Call<Getter> getterRegistration(@Body String phone, @Body String login, @Body String hashedPassword);

    @GET("/auth/getter/test")
    Call<Getter> checkAuthGetter(@Header("Authorization") String credentials);

    @GET("/auth/setter/test")
    Call<Setter> checkAuthSetter(@Header("Authorization") String credentials);
}
