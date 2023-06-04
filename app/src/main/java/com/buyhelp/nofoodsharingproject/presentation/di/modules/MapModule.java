package com.buyhelp.nofoodsharingproject.presentation.di.modules;

import com.buyhelp.nofoodsharingproject.presentation.fragments.needy.NeedyProfileFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MapModule {
    @ContributesAndroidInjector
    abstract NeedyProfileFragment contributeMapFragment();
}
