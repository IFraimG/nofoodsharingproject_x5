package com.example.nofoodsharingproject.data.api.notifications;

import android.content.Context;

import com.example.nofoodsharingproject.data.api.notifications.dto.FMCMessage;
import com.example.nofoodsharingproject.data.api.notifications.dto.FMCNotification;
import com.example.nofoodsharingproject.data.api.notifications.dto.RequestSetRead;
import com.example.nofoodsharingproject.data.api.notifications.dto.ResponseNotificationsList;
import com.example.nofoodsharingproject.models.Notification;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class NotificationRepository {
    public static Call<ResponseBody> requestNotifyDonateCall(String fmcToken, String title, String body) {
        return NotificationApiService.getInstance().notifyUserAboutDonate(new FMCMessage(fmcToken, new FMCNotification(title, body)));
    }

    public static Call<ResponseNotificationsList> getNotifications(Context ctx, String userID, String typeOfUser) {
        return InnerNotificationService.getInstance(ctx).getNotifications(userID, typeOfUser);
    }

    public static Call<Notification> getNotificationOne(Context ctx, String notificationID) {
        return InnerNotificationService.getInstance(ctx).getNotificationOne(notificationID);
    }

    public static Call<Notification> createNotification(Context ctx, Notification notification) {
        return InnerNotificationService.getInstance(ctx).createNotification(notification);
    }

    public static Call<Notification> setRead(Context ctx, String notificationID) {
        return InnerNotificationService.getInstance(ctx).setRead(new RequestSetRead(notificationID));
    }
}
