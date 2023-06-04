package com.buyhelp.nofoodsharingproject.data.api;

import android.content.Context;

import com.buyhelp.nofoodsharingproject.data.api.adverts.AdvertsApiService;
import com.buyhelp.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.buyhelp.nofoodsharingproject.data.api.auth.AuthApiService;
import com.buyhelp.nofoodsharingproject.data.api.auth.AuthRepository;
import com.buyhelp.nofoodsharingproject.data.api.giver.GiverApiService;
import com.buyhelp.nofoodsharingproject.data.api.needy.NeedyApiService;
import com.buyhelp.nofoodsharingproject.data.api.needy.NeedyRepository;
import com.buyhelp.nofoodsharingproject.data.api.map.MapApiService;
import com.buyhelp.nofoodsharingproject.data.api.map.MapRepository;
import com.buyhelp.nofoodsharingproject.data.api.notifications.InnerNotificationService;
import com.buyhelp.nofoodsharingproject.data.api.notifications.NotificationRepository;
import com.buyhelp.nofoodsharingproject.data.api.giver.GiverRepository;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.AppModule;
import com.buyhelp.nofoodsharingproject.presentation.di.scopes.AppScope;

import javax.inject.Inject;
import javax.inject.Named;

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
    public RetrofitService(@Named("application_context") Context ctx) {
        this.ctx = ctx;
    }

    @AppScope
    @Provides
    public HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return loggingInterceptor;
    }

    @AppScope
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

    @AppScope
    @Provides
    public Retrofit create() {
        OkHttpClient.Builder client = new OkHttpClient.Builder().addInterceptor(getHttpLoggingInterceptor()).addInterceptor(getAuthInterceptor());

        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(client.build())
                .build();
    }

    @AppScope
    @Provides
    public Retrofit getInstance() {
        if (retrofit == null) retrofit = create();
        return retrofit;
    }

    @AppScope
    @Provides
    public NeedyApiService provideNeedyRetrofitService(RetrofitService retrofitService) {
        return new NeedyApiService(retrofitService);
    }

    @AppScope
    @Provides
    public NeedyRepository provideNeedyRepository(NeedyApiService needyApiService) {
        return new NeedyRepository(needyApiService);
    }

    @AppScope
    @Provides
    public AuthApiService provideAuthRetrofitService(RetrofitService retrofitService) {
        return new AuthApiService(retrofitService);
    }

    @AppScope
    @Provides
    public AuthRepository provideAuthRepository(AuthApiService authApiService) {
        return new AuthRepository(authApiService);
    }


    @AppScope
    @Provides
    public GiverApiService provideGiverRetrofitService(RetrofitService retrofitService) {
        return new GiverApiService(retrofitService);
    }

    @AppScope
    @Provides
    public GiverRepository provideGiverRepository(GiverApiService giverApiService) {
        return new GiverRepository(giverApiService);
    }

    @AppScope
    @Provides
    public AdvertsApiService provideAdvertsRetrofitService(RetrofitService retrofitService) {
        return new AdvertsApiService(retrofitService);
    }

    @AppScope
    @Provides
    public AdvertsRepository provideAdvertsRepository(AdvertsApiService advertsApiService) {
        return new AdvertsRepository(advertsApiService);
    }

    @AppScope
    @Provides
    public MapApiService provideMapRetrofitService(RetrofitService retrofitService) {
        return new MapApiService(retrofitService);
    }

    @AppScope
    @Provides
    public MapRepository provideMapRepository(MapApiService mapApiService) {
        return new MapRepository(mapApiService);
    }

    @AppScope
    @Provides
    public InnerNotificationService provideInnerNotificationRetrofitService(RetrofitService retrofitService) {
        return new InnerNotificationService(retrofitService);
    }

    @AppScope
    @Provides
    public NotificationRepository provideInnerNotificationRepository(InnerNotificationService innerNotificationService) {
        return new NotificationRepository(innerNotificationService);
    }
}
