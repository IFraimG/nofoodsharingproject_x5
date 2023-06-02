package com.buyhelp.nofoodsharingproject.presentation.di.modules.getter;

import com.buyhelp.nofoodsharingproject.presentation.fragments.auth.GetterAuthFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class GetterAuthModule {
    @ContributesAndroidInjector
    abstract GetterAuthFragment contributeGetterAuthFragment();
}
