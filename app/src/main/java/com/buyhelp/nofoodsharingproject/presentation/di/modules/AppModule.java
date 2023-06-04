package com.buyhelp.nofoodsharingproject.presentation.di.modules;

import android.content.Context;

import com.buyhelp.nofoodsharingproject.presentation.di.qualifiers.ApplicationContext;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final Context ctx;

    public AppModule(@ApplicationContext Context context) {
        this.ctx = context;
    }

    @ApplicationContext
    @Provides
    public Context provideContext() {
        return ctx;
    }
}
