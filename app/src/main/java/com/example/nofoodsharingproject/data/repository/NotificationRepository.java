package com.example.nofoodsharingproject.data.repository;

import com.example.nofoodsharingproject.data.RetrofitService;
import com.example.nofoodsharingproject.data.api.getter.NotificationApiService;
import com.example.nofoodsharingproject.data.api.notifications.FMCMessage;
import com.example.nofoodsharingproject.data.api.notifications.FMCNotification;
import com.example.nofoodsharingproject.data.api.notifications.InnerNotificationService;
import com.example.nofoodsharingproject.data.api.notifications.RequestSetRead;
import com.example.nofoodsharingproject.data.api.notifications.ResponseNotificationsList;
import com.example.nofoodsharingproject.models.Notification;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class NotificationRepository {
    public static Call<ResponseBody> requestNotifyDonateCall(String fmcToken, String title, String body) {
        return NotificationApiService.getInstance().notifyUserAboutDonate(new FMCMessage(fmcToken, new FMCNotification(title, body)));
    }

    public static Call<ResponseNotificationsList> getNotifications(String userID) {
        return InnerNotificationService.getInstance().getNotifications(userID);
    }

    public static Call<Notification> getNotificationOne(String notificationID) {
        return InnerNotificationService.getInstance().getNotificationOne(notificationID);
    }

    public static Call<Notification> createNotification(Notification notification) {
        return InnerNotificationService.getInstance().createNotification(notification);
    }

    public static Call<Notification> setRead(String notificationID) {
        return InnerNotificationService.getInstance().setRead(new RequestSetRead(notificationID));
    }
}
