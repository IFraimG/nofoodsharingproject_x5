package com.buyhelp.nofoodsharingproject.data.api;

import android.content.Context;

import com.buyhelp.nofoodsharingproject.data.api.adverts.AdvertsApiService;
import com.buyhelp.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.buyhelp.nofoodsharingproject.data.api.auth.AuthApiService;
import com.buyhelp.nofoodsharingproject.data.api.auth.AuthRepository;
import com.buyhelp.nofoodsharingproject.data.api.getter.GetterApiService;
import com.buyhelp.nofoodsharingproject.data.api.getter.GetterRepository;
import com.buyhelp.nofoodsharingproject.data.api.map.MapApiService;
import com.buyhelp.nofoodsharingproject.data.api.map.MapRepository;
import com.buyhelp.nofoodsharingproject.data.api.notifications.InnerNotificationService;
import com.buyhelp.nofoodsharingproject.data.api.notifications.NotificationRepository;
import com.buyhelp.nofoodsharingproject.data.api.setter.SetterApiService;
import com.buyhelp.nofoodsharingproject.data.api.setter.SetterRepository;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.AppModule;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = { AppModule.class })
public class RetrofitService {
    private Retrofit retrofit;
    private Context ctx;
//    public String BASE_URL = "https://buy-help-server.onrender.com";
    public String BASE_URL = "http://192.168.0.100:8080";

    public RetrofitService() {}

    @Inject
    public RetrofitService(Context ctx) {
        this.ctx = ctx;
    }

    @Provides
    public HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return loggingInterceptor;
    }

    @Provides
    public Interceptor getAuthInterceptor() {
        return chain -> {
            Request originalRequest = chain.request();

            DefineUser defineUser = new DefineUser(ctx);

            if (defineUser.getToken() == null) {
                return chain.proceed(originalRequest);
            }

            Request requestWithToken = originalRequest.newBuilder()
                    .header("Authorization", defineUser.getToken())
                    .build();

            return chain.proceed(requestWithToken);
        };
    }

    @Provides
    @Singleton
    public Retrofit create() {
        OkHttpClient.Builder client = new OkHttpClient.Builder().addInterceptor(getHttpLoggingInterceptor()).addInterceptor(getAuthInterceptor());

        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(client.build())
                .build();
    }

    @Provides
    public Retrofit getInstance() {
        if (retrofit == null) retrofit = create();
        return retrofit;
    }

    @Provides
    public GetterApiService provideGetterRetrofitService(RetrofitService retrofitService) {
        return new GetterApiService(retrofitService);
    }

    @Provides
    public GetterRepository provideGetterRepository(GetterApiService getterApiService) {
        return new GetterRepository(getterApiService);
    }

    @Provides
    public AuthApiService provideAuthRetrofitService(RetrofitService retrofitService) {
        return new AuthApiService(retrofitService);
    }
    @Provides
    public AuthRepository provideAuthRepository(AuthApiService authApiService) {
        return new AuthRepository(authApiService);
    }

    @Provides
    public SetterApiService provideSetterRetrofitService(RetrofitService retrofitService) {
        return new SetterApiService(retrofitService);
    }
    @Provides
    public SetterRepository provideSetterRepository(SetterApiService setterApiService) {
        return new SetterRepository(setterApiService);
    }

    @Provides
    public AdvertsApiService provideAdvertsRetrofitService(RetrofitService retrofitService) {
        return new AdvertsApiService(retrofitService);
    }

    @Provides
    public AdvertsRepository provideAdvertsRepository(AdvertsApiService advertsApiService) {
        return new AdvertsRepository(advertsApiService);
    }

    @Provides
    public MapApiService provideMapRetrofitService(RetrofitService retrofitService) {
        return new MapApiService(retrofitService);
    }
    @Provides
    public MapRepository provideMapRepository(MapApiService mapApiService) {
        return new MapRepository(mapApiService);
    }

    @Provides
    public InnerNotificationService provideInnerNotificationRetrofitService(RetrofitService retrofitService) {
        return new InnerNotificationService(retrofitService);
    }
    @Provides
    public NotificationRepository provideInnerNotificationRepository(InnerNotificationService innerNotificationService) {
        return new NotificationRepository(innerNotificationService);
    }
}
