package com.buyhelp.nofoodsharingproject.presentation.di.modules.setter;

import com.buyhelp.nofoodsharingproject.presentation.fragments.auth.SetterAuthFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SetterAuthModule {
    @ContributesAndroidInjector
    abstract SetterAuthFragment contributeSetterAuthFragment();
}
