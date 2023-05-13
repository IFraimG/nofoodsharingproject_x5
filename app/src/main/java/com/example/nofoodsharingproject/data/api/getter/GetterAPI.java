package com.example.nofoodsharingproject.data.api.getter;

import com.example.nofoodsharingproject.data.api.getter.dto.RequestGetterEditProfile;
import com.example.nofoodsharingproject.data.api.notifications.dto.ResponseFCMToken;
import com.example.nofoodsharingproject.models.Getter;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GetterAPI {
    @PUT("/getters/edit_profile")
    Call<Getter> editProfile(@Body RequestGetterEditProfile requestGetterEditProfile);

    @GET("/getters/get_token/{authorID}")
    Call<ResponseFCMToken> getFCMtoken(@Path("authorID") String authorID);
}
