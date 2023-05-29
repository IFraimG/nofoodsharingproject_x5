package com.buyhelp.nofoodsharingproject.data.api;

import android.content.Context;

import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;


import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private static Retrofit retrofit;
    public static String BASE_URL = "https://buy-help-server.onrender.com";
    private static OkHttpClient.Builder client;
    private static Retrofit builder;

    public static Retrofit create(Context ctx) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Interceptor authInterceptor = chain -> {
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

        OkHttpClient.Builder client = new OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor(authInterceptor);

        if (builder == null) return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(client.build())
                .build();

        return builder;
    }

    public static void changeBaseUrl(String newApiBaseUrl) {
        BASE_URL = newApiBaseUrl;
    }

    public static Retrofit getInstance(Context ctx) {
        if (retrofit == null) retrofit = create(ctx);
        return retrofit;
    }
}
