package com.buyhelp.nofoodsharingproject.presentation.di.modules;

import com.buyhelp.nofoodsharingproject.presentation.fragments.ChatsListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ChatListModule {
    @ContributesAndroidInjector
    abstract ChatsListFragment contributeChatListFragment();
}
