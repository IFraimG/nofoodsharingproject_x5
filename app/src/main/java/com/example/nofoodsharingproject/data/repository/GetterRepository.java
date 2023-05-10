package com.example.nofoodsharingproject.data.repository;

import com.example.nofoodsharingproject.data.api.getter.GetterApiService;
import com.example.nofoodsharingproject.data.api.getter.RequestGetterEditProfile;
import com.example.nofoodsharingproject.data.api.notifications.ResponseFCMToken;
import com.example.nofoodsharingproject.models.Getter;
import retrofit2.Call;

public class GetterRepository {
    public static Call<Getter> editProfile(String userID, String login, String phone, String password, String oldPassword) {
        return GetterApiService.getInstance().editProfile(new RequestGetterEditProfile(userID, login, phone, password, oldPassword));
    }

    public static Call<ResponseFCMToken> getFCMtoken(String authorID) {
        return GetterApiService.getInstance().getFCMtoken(authorID);
    }
}
