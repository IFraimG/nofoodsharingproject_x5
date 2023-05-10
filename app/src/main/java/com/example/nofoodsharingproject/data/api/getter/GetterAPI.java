package com.example.nofoodsharingproject.data.api.getter;

import com.example.nofoodsharingproject.models.Getter;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface GetterAPI {
    @PUT("/getters/edit_profile")
    Call<Getter> editProfile(@Body RequestGetterEditProfile requestGetterEditProfile);
}
