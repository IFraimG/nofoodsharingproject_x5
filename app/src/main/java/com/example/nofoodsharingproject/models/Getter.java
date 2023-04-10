package com.example.nofoodsharingproject.models;

public class Getter extends User {
    String X5_ID;
    boolean isCreatedAdvst;

    public Getter(String firstname, String lastname, String phone, String X5_Id) {
        super(firstname, lastname, phone, X5_Id);
    }

    public boolean removeAdvertisement() {

        return false;
    }
}
