package com.buyhelp.nofoodsharingproject.presentation.factories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.buyhelp.nofoodsharingproject.data.api.notifications.NotificationRepository;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.NotificationsViewModel;

public class NotificationFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final NotificationRepository notificationRepository;

    public NotificationFactory(Application application, NotificationRepository notificationRepository) {
        this.application = application;
        this.notificationRepository = notificationRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new NotificationsViewModel(application, notificationRepository);
    }
}