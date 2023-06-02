package com.buyhelp.nofoodsharingproject.presentation.di.modules.getter;

import com.buyhelp.nofoodsharingproject.presentation.fragments.getter.GetterNewAdvertFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class GetterNewAdvertModule {
    @ContributesAndroidInjector
    abstract GetterNewAdvertFragment contributeGetterNewAdvertFragment();
}
