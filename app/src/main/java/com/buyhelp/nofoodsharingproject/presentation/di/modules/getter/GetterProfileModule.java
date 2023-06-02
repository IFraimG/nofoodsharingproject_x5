package com.buyhelp.nofoodsharingproject.presentation.di.modules.getter;

import com.buyhelp.nofoodsharingproject.presentation.fragments.getter.GetterProfileFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class GetterProfileModule {
    @ContributesAndroidInjector
    abstract GetterProfileFragment contributeGetterProfileFragment();
}
