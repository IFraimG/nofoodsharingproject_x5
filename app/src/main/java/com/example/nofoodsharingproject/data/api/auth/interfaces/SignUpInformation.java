package com.example.nofoodsharingproject.data.api.auth.interfaces;

// для отправки запроса
public class SignUpInformation {
    String phone;
    String login;
    String password;
    String tokenFCM;

    public SignUpInformation(String phone, String login, String password) {
        this.password = password;
        this.phone = phone;
        this.login = login;
    }

    public SignUpInformation(String phone, String login, String password, String tokenFCM) {
        this.password = password;
        this.phone = phone;
        this.login = login;
        this.tokenFCM = tokenFCM;
    }

    public SignUpInformation(String login, String password) {
        this.password = password;
        this.login = login;
    }
}
