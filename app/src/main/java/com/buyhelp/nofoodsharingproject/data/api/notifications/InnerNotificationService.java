package com.buyhelp.nofoodsharingproject.data.api.notifications;

import android.content.Context;

import com.buyhelp.nofoodsharingproject.data.RetrofitService;

public class InnerNotificationService {
    private static InnerNotificationAPI innerNotificationAPI;

    public static InnerNotificationAPI create(Context ctx) {
        return RetrofitService.getInstance(ctx).create(InnerNotificationAPI.class);
    }

    public static InnerNotificationAPI getInstance(Context ctx) {
        if (innerNotificationAPI == null) innerNotificationAPI = create(ctx);
        return innerNotificationAPI;
    }
}
