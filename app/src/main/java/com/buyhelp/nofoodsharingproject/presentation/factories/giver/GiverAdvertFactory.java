package com.buyhelp.nofoodsharingproject.presentation.factories.giver;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.buyhelp.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.buyhelp.nofoodsharingproject.data.api.needy.NeedyRepository;
import com.buyhelp.nofoodsharingproject.data.api.notifications.NotificationRepository;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.giver.GiverAdvertViewModel;

public class GiverAdvertFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final AdvertsRepository advertsRepository;
    private final NotificationRepository notificationRepository;
    private final NeedyRepository needyRepository;

    public GiverAdvertFactory(Application application, AdvertsRepository advertsRepository,
                              NotificationRepository notificationRepository, NeedyRepository needyRepository) {
        this.application = application;
        this.advertsRepository = advertsRepository;
        this.notificationRepository = notificationRepository;
        this.needyRepository = needyRepository;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new GiverAdvertViewModel(application, advertsRepository, notificationRepository, needyRepository);
    }
}