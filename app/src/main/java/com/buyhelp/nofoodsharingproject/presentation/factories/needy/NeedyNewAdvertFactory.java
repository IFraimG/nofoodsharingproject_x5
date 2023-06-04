package com.buyhelp.nofoodsharingproject.presentation.factories.needy;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.buyhelp.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.needy.NeedyNewAdvertViewModel;

public class NeedyNewAdvertFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final AdvertsRepository advertsRepository;

    public NeedyNewAdvertFactory(Application application, AdvertsRepository advertsRepository) {
        this.application = application;
        this.advertsRepository = advertsRepository;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new NeedyNewAdvertViewModel(application, advertsRepository);
    }
}