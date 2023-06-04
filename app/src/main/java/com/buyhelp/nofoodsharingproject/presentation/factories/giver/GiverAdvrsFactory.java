package com.buyhelp.nofoodsharingproject.presentation.factories.giver;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.buyhelp.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.buyhelp.nofoodsharingproject.data.api.map.MapRepository;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.advertisements.AdvertisementListViewModel;

public class GiverAdvrsFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final AdvertsRepository advertsRepository;
    private final MapRepository mapRepository;

    public GiverAdvrsFactory(Application application, AdvertsRepository advertsRepository, MapRepository mapRepository) {
        this.application = application;
        this.advertsRepository = advertsRepository;
        this.mapRepository = mapRepository;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AdvertisementListViewModel(application, advertsRepository, mapRepository);
    }
}