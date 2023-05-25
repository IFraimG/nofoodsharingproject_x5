package com.buyhelp.nofoodsharingproject;

import android.app.Application;
import com.buyhelp.nofoodsharingproject.data.RetrofitService;
import com.buyhelp.nofoodsharingproject.utils.DefineUser;
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
        RetrofitService.changeBaseUrl(defineUser.getBaseForRetrofit());

        AndroidThreeTen.init(getApplicationContext());

        mSocket = IO.socket(URI.create(defineUser.getBaseForRetrofit()));
    }

    public Socket getSocket() {
        return mSocket;
    }
}
