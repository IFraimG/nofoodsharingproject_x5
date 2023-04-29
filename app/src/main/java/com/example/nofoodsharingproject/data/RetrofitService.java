package com.example.nofoodsharingproject.data;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RetrofitService {
    private static Retrofit retrofit;
//    private static final String BASE_URL = "https://buy-help-server.onrender.com";
    private static final String BASE_URL = "http://localhost:8000";

    public static Retrofit create() {
        return new Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

    public static Retrofit getInstance() {
        if (retrofit == null) retrofit = create();
        return retrofit;
    }
}
