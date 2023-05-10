package com.example.nofoodsharingproject.data.repository;

import com.example.nofoodsharingproject.data.api.getter.NotificationApiService;
import com.example.nofoodsharingproject.data.api.notifications.FMCMessage;
import com.example.nofoodsharingproject.data.api.notifications.FMCNotification;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class NotificationRepository {
    public static Call<ResponseBody> requestNotifyDonateCall(String fmcToken, String title, String body) {
        return NotificationApiService.getInstance().notifyUserAboutDonate(new FMCMessage(fmcToken, new FMCNotification(title, body)));
    }
}
