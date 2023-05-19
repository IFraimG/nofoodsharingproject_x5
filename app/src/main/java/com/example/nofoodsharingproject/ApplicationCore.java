package com.example.nofoodsharingproject;

import android.app.Application;
import android.content.SharedPreferences;

import com.example.nofoodsharingproject.data.RetrofitService;
import com.example.nofoodsharingproject.utils.DefineUser;
import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.yandex.mapkit.MapKitFactory;

public class ApplicationCore extends Application {
    private boolean isInitMap = false;

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
    }
}
