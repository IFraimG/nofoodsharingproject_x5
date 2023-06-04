package com.buyhelp.nofoodsharingproject.presentation.di.modules;

import android.content.Context;

import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.buyhelp.nofoodsharingproject.presentation.di.qualifiers.ApplicationContext;
import com.buyhelp.nofoodsharingproject.presentation.di.scopes.HelpersScope;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;


@Module(includes = { AppModule.class })
public class DefineUserModule {
    private final Context ctx;

    @Inject
    public DefineUserModule(@ApplicationContext Context ctx) {
        this.ctx = ctx;
    }

    @HelpersScope
    @Provides
    public DefineUser provideDefineUser() {
        return new DefineUser(ctx);
    }
}
