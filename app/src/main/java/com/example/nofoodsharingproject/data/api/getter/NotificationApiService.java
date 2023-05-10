package com.example.nofoodsharingproject.data.api.getter;

import com.example.nofoodsharingproject.data.RetrofitNotificationService;
import com.example.nofoodsharingproject.data.api.notifications.NotificationAPI;

public class NotificationApiService {
    private static NotificationAPI notificationAPI;

    public static NotificationAPI create() {
        return RetrofitNotificationService.getInstance().create(NotificationAPI.class);
    }

    public static NotificationAPI getInstance() {
        if (notificationAPI == null) notificationAPI = create();
        return notificationAPI;
    }
}
