package com.example.nofoodsharingproject.data.api.auth.interfaces;

// для отправки запроса
public class SignUpInformation {
    String phone;
    String login;
    String password;

    public SignUpInformation(String phone, String login, String password) {
        this.password = password;
        this.phone = phone;
        this.login = login;
    }

    public SignUpInformation(String login, String password) {
        this.password = password;
        this.login = login;
    }
}
