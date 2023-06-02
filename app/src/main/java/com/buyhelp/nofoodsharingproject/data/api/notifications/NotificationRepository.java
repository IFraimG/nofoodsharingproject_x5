package com.buyhelp.nofoodsharingproject.data.api.notifications;

import com.buyhelp.nofoodsharingproject.data.api.notifications.dto.FMCMessage;
import com.buyhelp.nofoodsharingproject.data.api.notifications.dto.FMCNotification;
import com.buyhelp.nofoodsharingproject.data.api.notifications.dto.RequestSetRead;
import com.buyhelp.nofoodsharingproject.data.api.notifications.dto.ResponseNotificationsList;
import com.buyhelp.nofoodsharingproject.data.models.Notification;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class NotificationRepository {
    private final InnerNotificationAPI innerNotificationAPI;

    @Inject
    public NotificationRepository(InnerNotificationService notificationApiService) {
        innerNotificationAPI = notificationApiService.getInnerNotificationAPI();
    }

    public Call<ResponseBody> requestNotifyDonateCall(String fmcToken, String title, String body) {
        return NotificationApiService.getInstance().notifyUserAboutDonate(new FMCMessage(fmcToken, new FMCNotification(title, body)));
    }

    public Call<ResponseNotificationsList> getNotifications(String userID, String typeOfUser) {
        return innerNotificationAPI.getNotifications(userID, typeOfUser);
    }

    public Call<Notification> getNotificationOne(String notificationID) {
        return innerNotificationAPI.getNotificationOne(notificationID);
    }

    public Call<Notification> createNotification(Notification notification) {
        return innerNotificationAPI.createNotification(notification);
    }

    public Call<Notification> setRead(String notificationID) {
        return innerNotificationAPI.setRead(new RequestSetRead(notificationID));
    }
}
