package com.buyhelp.nofoodsharingproject.presentation.di.modules;

import com.buyhelp.nofoodsharingproject.presentation.fragments.getter.GetterProfileFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MapModule {
    @ContributesAndroidInjector
    abstract GetterProfileFragment contributeMapFragment();
}
