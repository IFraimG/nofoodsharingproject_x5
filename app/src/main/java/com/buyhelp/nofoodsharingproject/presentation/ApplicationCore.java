package com.buyhelp.nofoodsharingproject.presentation;

import android.app.Application;

import com.buyhelp.nofoodsharingproject.BuildConfig;
import com.buyhelp.nofoodsharingproject.data.api.RetrofitService;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.buyhelp.nofoodsharingproject.presentation.di.components.AppComponent;
import com.buyhelp.nofoodsharingproject.presentation.di.components.DaggerAppComponent;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.AppModule;
import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.yandex.mapkit.MapKitFactory;

import io.socket.client.Socket;
import java.net.URI;

import io.socket.client.IO;

public class ApplicationCore extends Application {
    private boolean isInitMap = false;
    private Socket mSocket;
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        new Instabug.Builder(this, BuildConfig.instabugKey)
                .setInvocationEvents(InstabugInvocationEvent.SHAKE, InstabugInvocationEvent.SCREENSHOT)
                .build();

        if (!isInitMap) {
            MapKitFactory.setApiKey(BuildConfig.apiKey);
            MapKitFactory.initialize(getApplicationContext());
            isInitMap = true;
        }

        DefineUser defineUser = new DefineUser(getSharedPreferences("prms", MODE_PRIVATE));

        appComponent = DaggerAppComponent.builder().application(this).appModule(new AppModule(getApplicationContext())).retrofitModule(new RetrofitService(getApplicationContext())).create();

        AndroidThreeTen.init(getApplicationContext());

        mSocket = IO.socket(URI.create(defineUser.getBaseForRetrofit()));
    }

    public Socket getSocket() {
        return mSocket;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}
