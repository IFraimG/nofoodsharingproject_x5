package com.buyhelp.nofoodsharingproject.presentation.factories.getters;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.buyhelp.nofoodsharingproject.data.api.getter.GetterRepository;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.getter.GetterProfileViewModel;

public class GetterProfileFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final GetterRepository getterRepository;

    public GetterProfileFactory(Application application, GetterRepository getterRepository) {
            this.application = application;
            this.getterRepository = getterRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new GetterProfileViewModel(application, getterRepository);
    }
}