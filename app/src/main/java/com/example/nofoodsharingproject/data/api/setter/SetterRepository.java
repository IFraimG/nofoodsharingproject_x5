package com.example.nofoodsharingproject.data.api.setter;

import android.content.Context;

import com.example.nofoodsharingproject.data.api.getter.dto.RequestChangeToken;
import com.example.nofoodsharingproject.data.api.getter.dto.RequestGetterEditProfile;
import com.example.nofoodsharingproject.data.api.notifications.dto.ResponseFCMToken;
import com.example.nofoodsharingproject.models.Setter;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class SetterRepository {
    public Call<Setter> editProfile(Context ctx, String userID, String login, String phone, String password, String oldPassword) {
        return SetterApiService.getInstance(ctx).editProfile(new RequestGetterEditProfile(userID, login, phone, password, oldPassword));
    }
    public Call<ResponseFCMToken> getFCMtoken(Context ctx, String authorID) {
        return SetterApiService.getInstance(ctx).getFCMtoken(authorID);
    }
    public Call<ResponseBody> changeToken(Context ctx, String userID, String fcmToken) {
        return SetterApiService.getInstance(ctx).changeToken(new RequestChangeToken(userID, fcmToken));
    }
}
