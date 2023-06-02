package com.buyhelp.nofoodsharingproject.presentation.factories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.buyhelp.nofoodsharingproject.data.api.map.MapRepository;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.MapViewModel;

public class MapFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final MapRepository mapRepository;

    public MapFactory(Application application, MapRepository mapRepository) {
        this.application = application;
        this.mapRepository = mapRepository;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new MapViewModel(application, mapRepository);
    }
}