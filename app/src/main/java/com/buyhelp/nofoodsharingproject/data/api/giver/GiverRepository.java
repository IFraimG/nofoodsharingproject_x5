package com.buyhelp.nofoodsharingproject.data.api.giver;

import com.buyhelp.nofoodsharingproject.data.api.needy.dto.RequestChangeToken;
import com.buyhelp.nofoodsharingproject.data.api.needy.dto.RequestNeedyEditProfile;
import com.buyhelp.nofoodsharingproject.data.api.notifications.dto.ResponseFCMToken;
import com.buyhelp.nofoodsharingproject.data.models.Giver;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class GiverRepository {
    private final GiverAPI giverAPI;

    @Inject
    public GiverRepository(GiverApiService giverApiService) {
        giverAPI = giverApiService.getGiverAPI();
    }

    public Call<Giver> editProfile(RequestNeedyEditProfile requestNeedyEditProfile) {
        return giverAPI.editProfile(requestNeedyEditProfile);
    }
    public Call<ResponseFCMToken> getFCMtoken(String authorID) {
        return giverAPI.getFCMtoken(authorID);
    }
    public Call<ResponseBody> changeToken(String userID, String fcmToken) {
        return giverAPI.changeToken(new RequestChangeToken(userID, fcmToken));
    }
}
