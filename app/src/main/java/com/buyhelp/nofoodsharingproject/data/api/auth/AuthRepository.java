package com.buyhelp.nofoodsharingproject.data.api.auth;

import com.buyhelp.nofoodsharingproject.data.api.auth.dto.CheckAuthI;
import com.buyhelp.nofoodsharingproject.data.api.auth.dto.SignUpInformation;
import com.buyhelp.nofoodsharingproject.data.api.auth.dto.SignUpResponseI;
import com.buyhelp.nofoodsharingproject.data.models.Needy;
import com.buyhelp.nofoodsharingproject.data.models.Giver;

import javax.inject.Inject;

import retrofit2.Call;

public class AuthRepository {
    private final AuthApiService authApiService;


    @Inject
    public AuthRepository(AuthApiService apiService) {
        this.authApiService = apiService;
    }

    public Call<SignUpResponseI<Needy>> needyLogin(String phone, String login, String password) {
        return authApiService.getAuthAPI().needyLogin(new SignUpInformation(phone, login, password));
    }

    public Call<SignUpResponseI<Needy>> needyRegistration(String phone, String login, String password, String tokenFCM) {
        return authApiService.getAuthAPI().needyRegistration(new SignUpInformation(phone, login, password, tokenFCM));
    }

    public Call<CheckAuthI> checkAuthNeedy(String token) {
        return authApiService.getAuthAPI().checkAuthNeedy(token);
    }

    public Call<SignUpResponseI<Giver>> giverLogin(String login, String password) {
        return authApiService.getAuthAPI().giverLogin(new SignUpInformation(login, password));
    }

    public Call<SignUpResponseI<Giver>> giverRegistration(String phone, String login, String password, String tokenFCM) {
        return authApiService.getAuthAPI().giverRegistration(new SignUpInformation(phone, login, password, tokenFCM));
    }

    public Call<CheckAuthI> checkAuthGiver(String token) {
        return authApiService.getAuthAPI().checkAuthGiver(token);
    }
}
