package com.example.nofoodsharingproject.models;

import java.util.Date;

public abstract class User {
    String phone;
    String firstName;
    String lastName;
    Date dateOfCreated;

    public User(String firstname, String lastname, String phone) {
        this.firstName = firstname;
        this.lastName = lastname;
        this.phone = phone;
    }
}
