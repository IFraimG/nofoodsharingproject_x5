package com.buyhelp.nofoodsharingproject.data.api.auth;

import com.buyhelp.nofoodsharingproject.data.api.auth.dto.CheckAuthI;
import com.buyhelp.nofoodsharingproject.data.api.auth.dto.SignUpInformation;
import com.buyhelp.nofoodsharingproject.data.api.auth.dto.SignUpResponseI;
import com.buyhelp.nofoodsharingproject.data.models.Getter;
import com.buyhelp.nofoodsharingproject.data.models.Setter;

import javax.inject.Inject;

import retrofit2.Call;

public class AuthRepository {
    private final AuthApiService authApiService;


    @Inject
    public AuthRepository(AuthApiService apiService) {
        this.authApiService = apiService;
    }

    public Call<SignUpResponseI<Getter>> getterLogin(String phone, String login, String password) {
        return authApiService.getAuthAPI().getterLogin(new SignUpInformation(phone, login, password));
    }

    public Call<SignUpResponseI<Getter>> getterRegistration(String phone, String login, String password, String tokenFCM) {
        return authApiService.getAuthAPI().getterRegistration(new SignUpInformation(phone, login, password, tokenFCM));
    }

    public Call<CheckAuthI> checkAuthGetter(String token) {
        return authApiService.getAuthAPI().checkAuthGetter(token);
    }

    public Call<SignUpResponseI<Setter>> setterLogin(String login, String password) {
        return authApiService.getAuthAPI().setterLogin(new SignUpInformation(login, password));
    }

    public Call<SignUpResponseI<Setter>> setterRegistration(String phone, String login, String password, String tokenFCM) {
        return authApiService.getAuthAPI().setterRegistration(new SignUpInformation(phone, login, password, tokenFCM));
    }

    public Call<CheckAuthI> checkAuthSetter(String token) {
        return authApiService.getAuthAPI().checkAuthSetter(token);
    }
}
