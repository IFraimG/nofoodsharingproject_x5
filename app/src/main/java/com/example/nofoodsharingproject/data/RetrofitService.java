package com.example.nofoodsharingproject.data;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private static Retrofit retrofit;
    public static String BASE_URL = "https://buy-help-server.onrender.com";
//    public static final String BASE_URL = "http://192.168.0.100:8000";
    private static OkHttpClient.Builder client;
    private static Retrofit builder;

    public static Retrofit create() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder().addInterceptor(interceptor);

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

    public static Retrofit getInstance() {
        if (retrofit == null) retrofit = create();
        return retrofit;
    }
}
