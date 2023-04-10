package com.example.nofoodsharingproject.models;

import java.util.Date;

public abstract class User {
    String phone;
    String firstName;
    String lastName;
    String X5_Id;
    Date dateOfCreated;

    public User(String firstname, String lastname, String phone, String X5_Id) {
        this.firstName = firstname;
        this.lastName = lastname;
        this.phone = phone;
        this.X5_Id = X5_Id;
    }
}
