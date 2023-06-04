package com.buyhelp.nofoodsharingproject.presentation.di.modules;

import com.buyhelp.nofoodsharingproject.presentation.fragments.auth.GiverAuthFragment;
import com.buyhelp.nofoodsharingproject.presentation.fragments.auth.GiverLoginAuthFragment;
import com.buyhelp.nofoodsharingproject.presentation.fragments.giver.GiverAdvertFragment;
import com.buyhelp.nofoodsharingproject.presentation.fragments.giver.GiverAdvrsFragment;
import com.buyhelp.nofoodsharingproject.presentation.fragments.giver.GiverProfileFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class GiverFragmentsModule {

    @ContributesAndroidInjector
    abstract GiverAdvertFragment contributeGiverAdvertFragment();

    @ContributesAndroidInjector
    abstract GiverAdvrsFragment contributeGiverAdvrsFragment();

    @ContributesAndroidInjector
    abstract GiverLoginAuthFragment contributeGiverLoginAuthFragment();

    @ContributesAndroidInjector
    abstract GiverAuthFragment contributeGiverAuthFragment();

    @ContributesAndroidInjector
    abstract GiverProfileFragment contributeGiverProfileFragment();
}