package com.buyhelp.nofoodsharingproject.presentation.di.modules;

import com.buyhelp.nofoodsharingproject.presentation.fragments.auth.SetterAuthFragment;
import com.buyhelp.nofoodsharingproject.presentation.fragments.auth.SetterLoginAuthFragment;
import com.buyhelp.nofoodsharingproject.presentation.fragments.setter.SetterAdvertFragment;
import com.buyhelp.nofoodsharingproject.presentation.fragments.setter.SetterAdvrsFragment;
import com.buyhelp.nofoodsharingproject.presentation.fragments.setter.SetterProfileFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SetterFragmentsModule {

    @ContributesAndroidInjector
    abstract SetterAdvertFragment contributeSetterAdvertFragment();

    @ContributesAndroidInjector
    abstract SetterAdvrsFragment contributeSetterAdvrsFragment();

    @ContributesAndroidInjector
    abstract SetterLoginAuthFragment contributeSetterLoginAuthFragment();

    @ContributesAndroidInjector
    abstract SetterAuthFragment contributeSetterAuthFragment();

    @ContributesAndroidInjector
    abstract SetterProfileFragment contributeSetterProfileFragment();
}