package com.buyhelp.nofoodsharingproject.presentation.di.modules.setter;

import com.buyhelp.nofoodsharingproject.presentation.fragments.setter.SetterAdvrsFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SetterAdvrsModule {
    @ContributesAndroidInjector
    abstract SetterAdvrsFragment contributeSetterAdvrsFragment();
}
