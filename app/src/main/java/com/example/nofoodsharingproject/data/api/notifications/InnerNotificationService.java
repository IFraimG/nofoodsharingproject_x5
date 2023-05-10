package com.example.nofoodsharingproject.data.api.notifications;

import com.example.nofoodsharingproject.data.RetrofitService;

public class InnerNotificationService {
    private static InnerNotificationAPI innerNotificationAPI;

    public static InnerNotificationAPI create() {
        return RetrofitService.getInstance().create(InnerNotificationAPI.class);
    }

    public static InnerNotificationAPI getInstance() {
        if (innerNotificationAPI == null) innerNotificationAPI = create();
        return innerNotificationAPI;
    }
}
