package com.example.nofoodsharingproject.data.api.auth;

import android.content.Context;

import com.example.nofoodsharingproject.data.api.auth.dto.CheckAuthI;
import com.example.nofoodsharingproject.data.api.auth.dto.SignUpInformation;
import com.example.nofoodsharingproject.data.api.auth.dto.SignUpResponseI;
import com.example.nofoodsharingproject.models.Getter;
import com.example.nofoodsharingproject.models.Setter;

import retrofit2.Call;

public class AuthRepository {
    public static Call<SignUpResponseI<Getter>> getterLogin(Context ctx, String phone, String login, String password) {
        return AuthApiService.getInstance(ctx).getterLogin(new SignUpInformation(phone, login, password));
    }

    public static Call<SignUpResponseI<Getter>> getterRegistration(Context ctx, String phone, String login, String password, String tokenFCM) {
        return AuthApiService.getInstance(ctx).getterRegistration(new SignUpInformation(phone, login, password, tokenFCM));
    }

    public static Call<CheckAuthI> checkAuthGetter(Context ctx, String token) {
        return AuthApiService.getInstance(ctx).checkAuthGetter(token);
    }

    public static Call<SignUpResponseI<Setter>> setterLogin(Context ctx, String login, String password) {
        return AuthApiService.getInstance(ctx).setterLogin(new SignUpInformation(login, password));
    }

    public static Call<SignUpResponseI<Setter>> setterRegistration(Context ctx, String phone, String login, String password, String tokenFCM) {
        return AuthApiService.getInstance(ctx).setterRegistration(new SignUpInformation(phone, login, password, tokenFCM));
    }

    public static Call<CheckAuthI> checkAuthSetter(Context ctx, String token) {
        return AuthApiService.getInstance(ctx).checkAuthSetter(token);
    }
}
