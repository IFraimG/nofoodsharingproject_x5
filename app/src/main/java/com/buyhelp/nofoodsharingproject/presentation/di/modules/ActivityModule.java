package com.buyhelp.nofoodsharingproject.presentation.di.modules;


import android.content.Context;

import com.buyhelp.nofoodsharingproject.presentation.di.qualifiers.ActivityContext;
import com.buyhelp.nofoodsharingproject.presentation.di.scopes.PermissionsScope;


import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final Context ctx;

    public ActivityModule(@ActivityContext Context activity) {
        this.ctx = activity;
    }

    @PermissionsScope
    @ActivityContext
    @Provides
    Context provideActivity() {
        return ctx;
    }
}
