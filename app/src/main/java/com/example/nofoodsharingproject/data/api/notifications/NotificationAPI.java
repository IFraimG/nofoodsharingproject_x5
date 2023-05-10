package com.example.nofoodsharingproject.data.api.notifications;

import com.example.nofoodsharingproject.BuildConfig;
import com.example.nofoodsharingproject.models.Notification;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationAPI {

    @Headers({
            "Content-Type: application/json",
            "Authorization: key=" + BuildConfig.fmcKey
    })
    @POST("fmc/send")
    Call<ResponseBody> notifyUserAboutDonate(@Body FMCMessage message);

}
