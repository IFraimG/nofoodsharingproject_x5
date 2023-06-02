package com.buyhelp.nofoodsharingproject.presentation.di.modules.getter;

import com.buyhelp.nofoodsharingproject.presentation.fragments.getter.GetterAdvrsFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class GetterAdvrsModule {
    @ContributesAndroidInjector
    abstract GetterAdvrsFragment contributeGetterAdvrsFragment();
}
