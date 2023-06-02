package com.buyhelp.nofoodsharingproject.presentation.di.modules.setter;

import com.buyhelp.nofoodsharingproject.presentation.fragments.auth.SetterLoginAuthFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SetterAuthLoginModule {
    @ContributesAndroidInjector
    abstract SetterLoginAuthFragment contributeSetterLoginAuthFragment();
}
