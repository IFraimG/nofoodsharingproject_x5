package com.buyhelp.nofoodsharingproject.data.api.getter;


import com.buyhelp.nofoodsharingproject.data.api.getter.dto.RequestChangeToken;
import com.buyhelp.nofoodsharingproject.data.api.getter.dto.RequestGetterEditProfile;
import com.buyhelp.nofoodsharingproject.data.api.notifications.dto.ResponseFCMToken;
import com.buyhelp.nofoodsharingproject.data.models.Getter;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class GetterRepository {
    private final GetterAPI getterAPI;

    @Inject
    public GetterRepository(GetterApiService getterApiService) {
        this.getterAPI = getterApiService.getGetterAPI();
    }

    public Call<Getter> editProfile(RequestGetterEditProfile requestGetterEditProfile) {
        return getterAPI.editProfile(requestGetterEditProfile);
    }

    public Call<ResponseFCMToken> getFCMtoken(String authorID) {
        return getterAPI.getFCMtoken(authorID);
    }

    public Call<ResponseBody> changeToken(String userID, String fcmToken) {
        return getterAPI.changeToken(new RequestChangeToken(userID, fcmToken));
    }
}
