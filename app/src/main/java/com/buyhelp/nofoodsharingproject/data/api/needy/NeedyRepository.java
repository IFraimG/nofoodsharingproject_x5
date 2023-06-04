package com.buyhelp.nofoodsharingproject.data.api.needy;


import com.buyhelp.nofoodsharingproject.data.api.needy.dto.RequestChangeToken;
import com.buyhelp.nofoodsharingproject.data.api.needy.dto.RequestNeedyEditProfile;
import com.buyhelp.nofoodsharingproject.data.api.notifications.dto.ResponseFCMToken;
import com.buyhelp.nofoodsharingproject.data.models.Needy;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class NeedyRepository {
    private final NeedyAPI needyAPI;

    @Inject
    public NeedyRepository(NeedyApiService needyApiService) {
        this.needyAPI = needyApiService.getNeedyAPI();
    }

    public Call<Needy> editProfile(RequestNeedyEditProfile requestNeedyEditProfile) {
        return needyAPI.editProfile(requestNeedyEditProfile);
    }

    public Call<ResponseFCMToken> getFCMtoken(String authorID) {
        return needyAPI.getFCMtoken(authorID);
    }

    public Call<ResponseBody> changeToken(String userID, String fcmToken) {
        return needyAPI.changeToken(new RequestChangeToken(userID, fcmToken));
    }
}
