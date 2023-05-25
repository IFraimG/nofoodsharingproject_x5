package com.buyhelp.nofoodsharingproject.data.api.auth;

import android.content.Context;

import com.buyhelp.nofoodsharingproject.data.api.auth.dto.CheckAuthI;
import com.buyhelp.nofoodsharingproject.data.api.auth.dto.SignUpInformation;
import com.buyhelp.nofoodsharingproject.data.api.auth.dto.SignUpResponseI;
import com.buyhelp.nofoodsharingproject.models.Getter;
import com.buyhelp.nofoodsharingproject.models.Setter;

import retrofit2.Call;

public class AuthRepository {
    public Call<SignUpResponseI<Getter>> getterLogin(Context ctx, String phone, String login, String password) {
        return AuthApiService.getInstance(ctx).getterLogin(new SignUpInformation(phone, login, password));
    }

    public Call<SignUpResponseI<Getter>> getterRegistration(Context ctx, String phone, String login, String password, String tokenFCM) {
        return AuthApiService.getInstance(ctx).getterRegistration(new SignUpInformation(phone, login, password, tokenFCM));
    }

    public Call<CheckAuthI> checkAuthGetter(Context ctx, String token) {
        return AuthApiService.getInstance(ctx).checkAuthGetter(token);
    }

    public Call<SignUpResponseI<Setter>> setterLogin(Context ctx, String login, String password) {
        return AuthApiService.getInstance(ctx).setterLogin(new SignUpInformation(login, password));
    }

    public Call<SignUpResponseI<Setter>> setterRegistration(Context ctx, String phone, String login, String password, String tokenFCM) {
        return AuthApiService.getInstance(ctx).setterRegistration(new SignUpInformation(phone, login, password, tokenFCM));
    }

    public Call<CheckAuthI> checkAuthSetter(Context ctx, String token) {
        return AuthApiService.getInstance(ctx).checkAuthSetter(token);
    }
}
