package com.buyhelp.nofoodsharingproject.presentation.factories.setters;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.buyhelp.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.buyhelp.nofoodsharingproject.data.api.getter.GetterRepository;
import com.buyhelp.nofoodsharingproject.data.api.notifications.NotificationRepository;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.setter.SetterAdvertViewModel;

public class SetterAdvertFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final AdvertsRepository advertsRepository;
    private final NotificationRepository notificationRepository;
    private final GetterRepository getterRepository;

    public SetterAdvertFactory(Application application, AdvertsRepository advertsRepository,
                               NotificationRepository notificationRepository, GetterRepository getterRepository) {
        this.application = application;
        this.advertsRepository = advertsRepository;
        this.notificationRepository = notificationRepository;
        this.getterRepository = getterRepository;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SetterAdvertViewModel(application, advertsRepository, notificationRepository,  getterRepository);
    }
}