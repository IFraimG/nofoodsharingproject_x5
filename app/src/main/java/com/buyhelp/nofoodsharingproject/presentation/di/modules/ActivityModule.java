package com.buyhelp.nofoodsharingproject.presentation.di.modules;

import com.buyhelp.nofoodsharingproject.presentation.activities.GiverActivity;
import com.buyhelp.nofoodsharingproject.presentation.activities.NeedyActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector(modules = { NeedyFragmentsModule.class })
    abstract NeedyActivity contributeNeedyActivity();

    @ContributesAndroidInjector(modules = { GiverFragmentsModule.class })
    abstract GiverActivity contributeGiverActivity();
}
