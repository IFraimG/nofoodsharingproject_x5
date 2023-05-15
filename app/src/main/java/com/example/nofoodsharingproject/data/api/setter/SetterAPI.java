package com.example.nofoodsharingproject.data.api.setter;

import com.example.nofoodsharingproject.data.api.getter.dto.RequestGetterEditProfile;
import com.example.nofoodsharingproject.models.Setter;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface SetterAPI {
    @PUT("/setters/edit_profile")
    Call<Setter> editProfile(@Body RequestGetterEditProfile requestGetterEditProfile);

}
