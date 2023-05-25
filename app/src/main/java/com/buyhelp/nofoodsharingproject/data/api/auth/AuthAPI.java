package com.buyhelp.nofoodsharingproject.data.api.auth;

import com.buyhelp.nofoodsharingproject.data.api.auth.dto.CheckAuthI;
import com.buyhelp.nofoodsharingproject.data.api.auth.dto.SignUpInformation;
import com.buyhelp.nofoodsharingproject.data.api.auth.dto.SignUpResponseI;
import com.buyhelp.nofoodsharingproject.models.Getter;
import com.buyhelp.nofoodsharingproject.models.Setter;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthAPI {
    @POST("/auth/getter/login")
    Call<SignUpResponseI<Getter>> getterLogin(@Body SignUpInformation signUpInformation);

    @POST("/auth/getter/signup")
    Call<SignUpResponseI<Getter>> getterRegistration(@Body SignUpInformation signUpInformation);

    @GET("/auth/getter/test")
    Call<CheckAuthI> checkAuthGetter(@Header("Authorization") String credentials);

    @POST("/auth/setter/login")
    Call<SignUpResponseI<Setter>> setterLogin(@Body SignUpInformation signUpInformation);

    @POST("/auth/setter/signup")
    Call<SignUpResponseI<Setter>> setterRegistration(@Body SignUpInformation signUpInformation);

    @GET("/auth/setter/test")
    Call<CheckAuthI> checkAuthSetter(@Header("Authorization") String credentials);
}
