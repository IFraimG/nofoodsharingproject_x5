package com.buyhelp.nofoodsharingproject.data.models;

public class ShortDataUser {
    private String login;
    private String phone;
    private String X5_Id;


    public ShortDataUser() {}

    public ShortDataUser(String login, String phone, String X5_Id) {
        this.login = login;
        this.phone = phone;
        this.X5_Id = X5_Id;
    }


    public void setLogin(String login) {
        this.login = login;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getLogin() {
        return login;
    }

    public String getPhone() {
        return phone;
    }

    public void setX5_Id(String x5_Id) {
        X5_Id = x5_Id;
    }

    public String getX5_Id() {
        return X5_Id;
    }
}
