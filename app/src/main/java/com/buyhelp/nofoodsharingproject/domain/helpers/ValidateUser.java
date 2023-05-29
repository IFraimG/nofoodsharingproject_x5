package com.buyhelp.nofoodsharingproject.domain.helpers;

import android.content.Context;
import android.widget.Toast;

import com.buyhelp.nofoodsharingproject.R;

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

    public static boolean isValidate(Context ctx, String phone, String login, String password) {
        if (!validatePhone(phone)) {
            Toast.makeText(ctx, R.string.uncorrect_number_phone, Toast.LENGTH_LONG).show();
            return false;
        }
        if (!validateLogin(login)) {
            Toast.makeText(ctx, R.string.uncorrect_name, Toast.LENGTH_LONG).show();
            return false;
        }
        if (!validatePassword(password)) {
            Toast.makeText(ctx, R.string.uncorrect_password, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
