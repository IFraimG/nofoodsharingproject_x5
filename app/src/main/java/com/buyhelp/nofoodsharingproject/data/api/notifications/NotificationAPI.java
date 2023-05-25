package com.buyhelp.nofoodsharingproject.data.api.notifications;

import com.buyhelp.nofoodsharingproject.BuildConfig;
import com.buyhelp.nofoodsharingproject.data.api.notifications.dto.FMCMessage;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationAPI {

    @Headers({
            "Content-Type: application/json",
            "Authorization: key=" + BuildConfig.fmcKey
    })
    @POST("fcm/send")
    Call<ResponseBody> notifyUserAboutDonate(@Body FMCMessage message);

}
