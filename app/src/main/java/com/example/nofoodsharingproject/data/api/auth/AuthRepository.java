package com.example.nofoodsharingproject.data.api.auth;

import com.example.nofoodsharingproject.data.api.auth.AuthApiService;
import com.example.nofoodsharingproject.data.api.auth.dto.CheckAuthI;
import com.example.nofoodsharingproject.data.api.auth.dto.SignUpInformation;
import com.example.nofoodsharingproject.data.api.auth.dto.SignUpResponseI;
import com.example.nofoodsharingproject.models.Getter;
import com.example.nofoodsharingproject.models.Setter;

import retrofit2.Call;

public class AuthRepository {
    public static Call<SignUpResponseI<Getter>> getterLogin(String phone, String login, String password) {
        return AuthApiService.getInstance().getterLogin(new SignUpInformation(phone, login, password));
    }

    public static Call<SignUpResponseI<Getter>> getterRegistration(String phone, String login, String password, String tokenFCM) {
        return AuthApiService.getInstance().getterRegistration(new SignUpInformation(phone, login, password, tokenFCM));
    }

    public static Call<CheckAuthI> checkAuthGetter(String token) {
        return AuthApiService.getInstance().checkAuthGetter(token);
    }

    public static Call<SignUpResponseI<Setter>> setterLogin(String login, String password) {
        return AuthApiService.getInstance().setterLogin(new SignUpInformation(login, password));
    }

    public static Call<SignUpResponseI<Setter>> setterRegistration(String phone, String login, String password, String tokenFCM) {
        return AuthApiService.getInstance().setterRegistration(new SignUpInformation(phone, login, password, tokenFCM));
    }

    public static Call<CheckAuthI> checkAuthSetter(String token) {
        return AuthApiService.getInstance().checkAuthSetter(token);
    }
}
