package com.buyhelp.nofoodsharingproject.data.api.setter;

import android.content.Context;

import com.buyhelp.nofoodsharingproject.data.api.getter.dto.RequestChangeToken;
import com.buyhelp.nofoodsharingproject.data.api.getter.dto.RequestGetterEditProfile;
import com.buyhelp.nofoodsharingproject.data.api.notifications.dto.ResponseFCMToken;
import com.buyhelp.nofoodsharingproject.data.models.Setter;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class SetterRepository {
    public Call<Setter> editProfile(Context ctx, RequestGetterEditProfile requestGetterEditProfile) {
        return SetterApiService.getInstance(ctx).editProfile(requestGetterEditProfile);
    }
    public Call<ResponseFCMToken> getFCMtoken(Context ctx, String authorID) {
        return SetterApiService.getInstance(ctx).getFCMtoken(authorID);
    }
    public Call<ResponseBody> changeToken(Context ctx, String userID, String fcmToken) {
        return SetterApiService.getInstance(ctx).changeToken(new RequestChangeToken(userID, fcmToken));
    }
}
