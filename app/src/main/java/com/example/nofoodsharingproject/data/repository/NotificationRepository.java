package com.example.nofoodsharingproject.data.repository;

import com.example.nofoodsharingproject.data.api.notifications.NotificationApiService;
import com.example.nofoodsharingproject.data.api.notifications.dto.FMCMessage;
import com.example.nofoodsharingproject.data.api.notifications.dto.FMCNotification;
import com.example.nofoodsharingproject.data.api.notifications.InnerNotificationService;
import com.example.nofoodsharingproject.data.api.notifications.dto.RequestSetRead;
import com.example.nofoodsharingproject.data.api.notifications.dto.ResponseNotificationsList;
import com.example.nofoodsharingproject.models.Notification;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class NotificationRepository {
    public static Call<ResponseBody> requestNotifyDonateCall(String fmcToken, String title, String body) {
        return NotificationApiService.getInstance().notifyUserAboutDonate(new FMCMessage(fmcToken, new FMCNotification(title, body)));
    }

    public static Call<ResponseNotificationsList> getNotifications(String userID, String typeOfUser) {
        return InnerNotificationService.getInstance().getNotifications(userID, typeOfUser);
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
