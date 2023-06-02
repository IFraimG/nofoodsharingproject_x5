package com.buyhelp.nofoodsharingproject.presentation.factories.setters;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.buyhelp.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.buyhelp.nofoodsharingproject.data.api.setter.SetterRepository;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.setter.SetterProfileViewModel;

public class SetterProfileFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final SetterRepository setterRepository;
    private final AdvertsRepository advertsRepository;

    public SetterProfileFactory(Application application, SetterRepository setterRepository, AdvertsRepository advertsRepository) {
        this.application = application;
        this.setterRepository = setterRepository;
        this.advertsRepository = advertsRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SetterProfileViewModel(application, setterRepository, advertsRepository);
    }
}