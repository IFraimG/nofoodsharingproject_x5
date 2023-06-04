package com.buyhelp.nofoodsharingproject.data.api.auth;

import com.buyhelp.nofoodsharingproject.data.api.auth.dto.CheckAuthI;
import com.buyhelp.nofoodsharingproject.data.api.auth.dto.SignUpInformation;
import com.buyhelp.nofoodsharingproject.data.api.auth.dto.SignUpResponseI;
import com.buyhelp.nofoodsharingproject.data.models.Needy;
import com.buyhelp.nofoodsharingproject.data.models.Giver;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthAPI {
    @POST("/auth/needy/login")
    Call<SignUpResponseI<Needy>> needyLogin(@Body SignUpInformation signUpInformation);

    @POST("/auth/needy/signup")
    Call<SignUpResponseI<Needy>> needyRegistration(@Body SignUpInformation signUpInformation);

    @GET("/auth/needy/test")
    Call<CheckAuthI> checkAuthNeedy(@Header("Authorization") String credentials);

    @POST("/auth/giver/login")
    Call<SignUpResponseI<Giver>> giverLogin(@Body SignUpInformation signUpInformation);

    @POST("/auth/giver/signup")
    Call<SignUpResponseI<Giver>> giverRegistration(@Body SignUpInformation signUpInformation);

    @GET("/auth/giver/test")
    Call<CheckAuthI> checkAuthGiver(@Header("Authorization") String credentials);
}
