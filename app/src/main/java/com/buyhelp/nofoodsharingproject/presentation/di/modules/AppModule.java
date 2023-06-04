package com.buyhelp.nofoodsharingproject.presentation.di.modules;

import android.content.Context;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final Context ctx;

    public AppModule(Context context) {
        this.ctx = context;
    }

    @Named("application_context")
    @Provides
    public Context provideContext() {
        return ctx;
    }
}
