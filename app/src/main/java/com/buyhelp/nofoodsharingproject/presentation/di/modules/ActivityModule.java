package com.buyhelp.nofoodsharingproject.presentation.di.modules;

import com.buyhelp.nofoodsharingproject.presentation.activities.GetterActivity;
import com.buyhelp.nofoodsharingproject.presentation.activities.SetterActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector(modules = { GetterFragmentsModule.class })
    abstract GetterActivity contributeGetterActivity();

    @ContributesAndroidInjector(modules = { SetterFragmentsModule.class })
    abstract SetterActivity contributeSetterActivity();
}
