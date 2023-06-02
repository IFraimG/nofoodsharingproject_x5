package com.buyhelp.nofoodsharingproject.presentation.di.modules;

import com.buyhelp.nofoodsharingproject.presentation.fragments.NotifyFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class NotifyModule {
    @ContributesAndroidInjector
    abstract NotifyFragment contributeNotifyFragment();
}

