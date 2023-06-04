package com.buyhelp.nofoodsharingproject.presentation.factories.giver;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.buyhelp.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.buyhelp.nofoodsharingproject.data.api.giver.GiverRepository;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.giver.GiverProfileViewModel;

public class GiverProfileFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final GiverRepository giverRepository;
    private final AdvertsRepository advertsRepository;

    public GiverProfileFactory(Application application, GiverRepository giverRepository, AdvertsRepository advertsRepository) {
        this.application = application;
        this.giverRepository = giverRepository;
        this.advertsRepository = advertsRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new GiverProfileViewModel(application, giverRepository, advertsRepository);
    }
}