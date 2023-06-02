package com.buyhelp.nofoodsharingproject.presentation.di.modules;

import com.buyhelp.nofoodsharingproject.presentation.fragments.ChatFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ChatModule {
    @ContributesAndroidInjector
    abstract ChatFragment contributeChatModuleFragment();
}
