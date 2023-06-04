package com.buyhelp.nofoodsharingproject.presentation.di.modules;

import com.buyhelp.nofoodsharingproject.presentation.fragments.auth.NeedyAuthFragment;
import com.buyhelp.nofoodsharingproject.presentation.fragments.needy.NeedyAdvrsFragment;
import com.buyhelp.nofoodsharingproject.presentation.fragments.needy.NeedyNewAdvertFragment;
import com.buyhelp.nofoodsharingproject.presentation.fragments.needy.NeedyProfileFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class NeedyFragmentsModule {
    @ContributesAndroidInjector
    abstract NeedyAdvrsFragment contributeNeedyAdvrsFragment();

    @ContributesAndroidInjector
    abstract NeedyAuthFragment contributeNeedyAuthFragment();

    @ContributesAndroidInjector
    abstract NeedyNewAdvertFragment contributeNeedyNewAdvertFragment();

    @ContributesAndroidInjector
    abstract NeedyProfileFragment contributeNeedyProfileFragment();
}
