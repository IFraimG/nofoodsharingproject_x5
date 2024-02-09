package com.buyhelp.nofoodsharingproject.presentation;

import android.app.Application;

import com.buyhelp.nofoodsharingproject.BuildConfig;
import com.buyhelp.nofoodsharingproject.data.api.RetrofitService;
import com.buyhelp.nofoodsharingproject.presentation.di.components.AppComponent;
import com.buyhelp.nofoodsharingproject.presentation.di.components.DaggerAppComponent;
import com.buyhelp.nofoodsharingproject.presentation.di.components.HelpersComponent;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.AppModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.DefineUserModule;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.yandex.mapkit.MapKitFactory;

import io.socket.client.Socket;
import java.net.URI;

import io.socket.client.IO;

public class ApplicationCore extends Application {
    private boolean isInitMap = false;
    private Socket mSocket;
    private AppComponent appComponent;
    private HelpersComponent helpersComponent;

    @Override
    public void onCreate() {
        super.onCreate();

//        new Instabug.Builder(this, BuildConfig.instabugKey)
//                .setInvocationEvents(InstabugInvocationEvent.SHAKE, InstabugInvocationEvent.SCREENSHOT)
//                .build();

        if (!isInitMap) {
            MapKitFactory.setApiKey(BuildConfig.apiKey);
            MapKitFactory.initialize(getApplicationContext());
            isInitMap = true;
        }

        appComponent = DaggerAppComponent.builder().appModule(new AppModule(getApplicationContext())).retrofitModule(new RetrofitService()).create();
        helpersComponent = appComponent.helpersComponentBuilder().defineUserModule(new DefineUserModule(getApplicationContext())).create();

        AndroidThreeTen.init(getApplicationContext());

//        mSocket = IO.socket(URI.create("http://95.163.242.189:8080"));
        // новая версия
        mSocket = IO.socket(URI.create("http://95.163.242.247:8080"));
    }

    public Socket getSocket() {
        return mSocket;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    /**
     * Получение helper (DefineUser) для редактирования информации в ESP/SP
     */
    public HelpersComponent getHelpersComponent() {
        return helpersComponent;
    }

}
