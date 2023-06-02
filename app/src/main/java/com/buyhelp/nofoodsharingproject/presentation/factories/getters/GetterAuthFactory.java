package com.buyhelp.nofoodsharingproject.presentation.factories.getters;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.buyhelp.nofoodsharingproject.data.api.auth.AuthRepository;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.getter.GetterAuthViewModel;

public class GetterAuthFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final AuthRepository authRepository;

    public GetterAuthFactory(Application application, AuthRepository authRepository) {
        this.application = application;
        this.authRepository = authRepository;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new GetterAuthViewModel(application, authRepository);
    }
}
