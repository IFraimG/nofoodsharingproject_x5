package com.buyhelp.nofoodsharingproject.presentation.factories.needy;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.buyhelp.nofoodsharingproject.data.api.needy.NeedyRepository;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.needy.NeedyProfileViewModel;

public class NeedyProfileFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final NeedyRepository needyRepository;

    public NeedyProfileFactory(Application application, NeedyRepository needyRepository) {
            this.application = application;
            this.needyRepository = needyRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new NeedyProfileViewModel(application, needyRepository);
    }
}