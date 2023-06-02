package com.buyhelp.nofoodsharingproject.presentation.factories.getters;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.buyhelp.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.buyhelp.nofoodsharingproject.data.api.map.MapRepository;
import com.buyhelp.nofoodsharingproject.data.api.notifications.NotificationRepository;
import com.buyhelp.nofoodsharingproject.data.api.setter.SetterRepository;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.advertisements.AdvertisementOneViewModel;

public class GetterAdvrsFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final NotificationRepository notificationRepository;
    private final SetterRepository setterRepository;
    private final AdvertsRepository advertsRepository;
    private final MapRepository mapRepository;

    public GetterAdvrsFactory(Application application, NotificationRepository notificationRepository,
                              SetterRepository setterRepository, AdvertsRepository advertsRepository, MapRepository mapRepository) {
        this.application = application;
        this.notificationRepository = notificationRepository;
        this.setterRepository = setterRepository;
        this.advertsRepository = advertsRepository;
        this.mapRepository = mapRepository;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AdvertisementOneViewModel(application, notificationRepository, setterRepository, advertsRepository, mapRepository);
    }
}