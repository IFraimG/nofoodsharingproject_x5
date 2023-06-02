package com.buyhelp.nofoodsharingproject.presentation.factories.getters;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.buyhelp.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.getter.GetterNewAdvertViewModel;

public class GetterNewAdvertFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final AdvertsRepository advertsRepository;

    public GetterNewAdvertFactory(Application application, AdvertsRepository advertsRepository) {
        this.application = application;
        this.advertsRepository = advertsRepository;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new GetterNewAdvertViewModel(application, advertsRepository);
    }
}