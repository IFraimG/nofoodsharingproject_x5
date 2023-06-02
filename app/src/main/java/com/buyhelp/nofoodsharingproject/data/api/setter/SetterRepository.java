package com.buyhelp.nofoodsharingproject.data.api.setter;

import com.buyhelp.nofoodsharingproject.data.api.getter.dto.RequestChangeToken;
import com.buyhelp.nofoodsharingproject.data.api.getter.dto.RequestGetterEditProfile;
import com.buyhelp.nofoodsharingproject.data.api.notifications.dto.ResponseFCMToken;
import com.buyhelp.nofoodsharingproject.data.models.Setter;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class SetterRepository {
    private final SetterAPI setterAPI;

    @Inject
    public SetterRepository(SetterApiService setterApiService) {
        setterAPI = setterApiService.getSetterAPI();
    }

    public Call<Setter> editProfile(RequestGetterEditProfile requestGetterEditProfile) {
        return setterAPI.editProfile(requestGetterEditProfile);
    }
    public Call<ResponseFCMToken> getFCMtoken(String authorID) {
        return setterAPI.getFCMtoken(authorID);
    }
    public Call<ResponseBody> changeToken(String userID, String fcmToken) {
        return setterAPI.changeToken(new RequestChangeToken(userID, fcmToken));
    }
}
