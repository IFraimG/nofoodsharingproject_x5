package com.example.nofoodsharingproject.models;

public class Setter extends User {
    Advertisement[] successHistory;

    public Setter(String firstname, String lastname, String phone, String X5_Id) {
        super(firstname, lastname, phone, X5_Id);
    }

    public String makeHelp(String adversID) {

        return "cлучайно сгенерированный айди до 6 символов из русских букв";
    }
}
