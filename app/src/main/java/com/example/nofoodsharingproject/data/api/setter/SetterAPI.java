package com.example.nofoodsharingproject.data.api.setter;

import com.example.nofoodsharingproject.data.api.getter.dto.RequestGetterEditProfile;
import com.example.nofoodsharingproject.data.api.notifications.dto.ResponseFCMToken;
import com.example.nofoodsharingproject.models.Setter;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SetterAPI {
    @PUT("/setters/edit_profile")
    Call<Setter> editProfile(@Body RequestGetterEditProfile requestGetterEditProfile);

    @GET("/setters/get_token/{authorID}")
    Call<ResponseFCMToken> getFCMtoken(@Path("authorID") String authorID);
}
