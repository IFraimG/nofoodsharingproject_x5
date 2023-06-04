package com.buyhelp.nofoodsharingproject.presentation.di.modules;


import androidx.appcompat.app.AppCompatActivity;

import com.buyhelp.nofoodsharingproject.domain.helpers.PermissionHandler;
import com.buyhelp.nofoodsharingproject.presentation.di.scopes.PermissionsScope;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

@Module(includes = { ActivityModule.class })
public class PermissionHandlerModule {
    private AppCompatActivity activity;

    public PermissionHandlerModule() {}

    @Inject
    public PermissionHandlerModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @PermissionsScope
    @Provides
    public PermissionHandler getPermissionHandler() {
        return new PermissionHandler(activity);
    }
}
