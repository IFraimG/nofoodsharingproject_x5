package com.example.nofoodsharingproject.data.api.setter;

import com.example.nofoodsharingproject.data.api.getter.dto.RequestGetterEditProfile;
import com.example.nofoodsharingproject.data.api.notifications.dto.ResponseFCMToken;
import com.example.nofoodsharingproject.models.Setter;

import retrofit2.Call;

public class SetterRepository {
    public static Call<Setter> editProfile(String userID, String login, String phone, String password, String oldPassword) {
        return SetterApiService.getInstance().editProfile(new RequestGetterEditProfile(userID, login, phone, password, oldPassword));
    }
    public static Call<ResponseFCMToken> getFCMtoken(String authorID) {
        return SetterApiService.getInstance().getFCMtoken(authorID);
    }
}
