package com.buyhelp.nofoodsharingproject.data.api.notifications;

import com.buyhelp.nofoodsharingproject.data.api.RetrofitService;

import javax.inject.Inject;

public class InnerNotificationService {
    private final InnerNotificationAPI innerNotificationAPI;

    @Inject
    public InnerNotificationService(RetrofitService retrofitService) {
        innerNotificationAPI = retrofitService.getInstance().create(InnerNotificationAPI.class);
    }

    public InnerNotificationAPI getInnerNotificationAPI() {
        return innerNotificationAPI;
    }
}
