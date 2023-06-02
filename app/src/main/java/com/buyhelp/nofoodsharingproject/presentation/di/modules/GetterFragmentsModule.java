package com.buyhelp.nofoodsharingproject.presentation.di.modules;

import com.buyhelp.nofoodsharingproject.presentation.fragments.auth.GetterAuthFragment;
import com.buyhelp.nofoodsharingproject.presentation.fragments.getter.GetterAdvrsFragment;
import com.buyhelp.nofoodsharingproject.presentation.fragments.getter.GetterNewAdvertFragment;
import com.buyhelp.nofoodsharingproject.presentation.fragments.getter.GetterProfileFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class GetterFragmentsModule {
    @ContributesAndroidInjector
    abstract GetterAdvrsFragment contributeGetterAdvrsFragment();

    @ContributesAndroidInjector
    abstract GetterAuthFragment contributeGetterAuthFragment();

    @ContributesAndroidInjector
    abstract GetterNewAdvertFragment contributeGetterNewAdvertFragment();

    @ContributesAndroidInjector
    abstract GetterProfileFragment contributeGetterProfileFragment();
}
