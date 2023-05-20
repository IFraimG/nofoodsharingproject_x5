package com.example.nofoodsharingproject.data.api.getter;

import com.example.nofoodsharingproject.data.api.getter.dto.RequestChangeToken;
import com.example.nofoodsharingproject.data.api.getter.dto.RequestGetterEditProfile;
import com.example.nofoodsharingproject.data.api.notifications.dto.ResponseFCMToken;
import com.example.nofoodsharingproject.models.Getter;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class GetterRepository {
    public static Call<Getter> editProfile(String userID, String login, String phone, String password, String oldPassword) {
        return GetterApiService.getInstance().editProfile(new RequestGetterEditProfile(userID, login, phone, password, oldPassword));
    }
    public static Call<ResponseFCMToken> getFCMtoken(String authorID) {
        return GetterApiService.getInstance().getFCMtoken(authorID);
    }
    public static Call<ResponseBody> changeToken(String userID, String fcmToken) {
        return GetterApiService.getInstance().changeToken(new RequestChangeToken(userID, fcmToken));
    }
}
