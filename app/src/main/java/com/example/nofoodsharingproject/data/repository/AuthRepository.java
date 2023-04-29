package com.example.nofoodsharingproject.data.repository;

import com.example.nofoodsharingproject.data.api.adverts.AdvertsApiService;
import com.example.nofoodsharingproject.data.api.auth.AuthApiService;
import com.example.nofoodsharingproject.models.Advertisement;
import com.example.nofoodsharingproject.models.Getter;
import com.example.nofoodsharingproject.models.Setter;

import java.util.List;

import retrofit2.Call;

public class AuthRepository {
    public static Call<Getter> getterLogin(String login, String password) {
        return AuthApiService.getInstance().getterLogin(login, password);
    }

    public static Call<Getter> getterRegistration(String phone, String login, String password) {
        return AuthApiService.getInstance().getterRegistration(phone, login, password);
    }

    public static Call<Setter> setterLogin(String login, String password) {
        return AuthApiService.getInstance().setterLogin(login, password);
    }

    public static Call<Setter> setterRegistration(String phone, String login, String password) {
        return AuthApiService.getInstance().setterRegistration(phone, login, password);
    }

    public static Call<Getter> checkAuthGetter() {
        return AuthApiService.getInstance().checkAuthGetter("");
    }

    public static Call<Setter> checkAuthSetter() {
        return AuthApiService.getInstance().checkAuthSetter("");
    }
}
