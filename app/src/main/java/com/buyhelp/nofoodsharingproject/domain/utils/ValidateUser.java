package com.buyhelp.nofoodsharingproject.domain.utils;

public class ValidateUser {
    public static boolean validateLogin(String login) {
        return login.length() >= 4;
    }

    public static boolean validatePassword(String password) {
        return password.length() >= 8;
    }

    public static boolean validatePhone(String phone) {
        String regexPhone = "^\\+?[0-9\\-\\s]*$";
        return phone.matches(regexPhone);
    }
}
