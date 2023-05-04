package com.example.nofoodsharingproject.fragments.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nofoodsharingproject.MainActivity;
import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.data.api.auth.interfaces.SignUpResponseI;
import com.example.nofoodsharingproject.data.repository.AuthRepository;
import com.example.nofoodsharingproject.models.Getter;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Getter_Auth_Fragment extends Fragment {
    EditText phone = null;
    EditText login = null;
    EditText password = null;
    Button btnSignup = null;
    Button btnLogin = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_getter_auth, container, false);
        btnLogin = (Button) view.findViewById(R.id.auth_getter_btn_login);

        phone = view.findViewById(R.id.auth_getter_signup_phone);
        password = view.findViewById(R.id.auth_getter_signup_password);
        login = view.findViewById(R.id.auth_getter_signup_login);
        btnSignup = view.findViewById(R.id.auth_getter_create);

        btnSignup.setOnClickListener(View -> signup());
        btnLogin.setOnClickListener(View -> login());

        return view;
    }

    public boolean validate() {
        String regexPhone = "^\\+?[0-9\\-\\s]*$";
        if (!phone.getText().toString().matches(regexPhone)) {
            Toast.makeText(getContext(), R.string.uncorrect_number_phone, Toast.LENGTH_LONG).show();
            return false;
        }
        if (login.getText().toString().length() < 4) {
            Toast.makeText(getContext(), R.string.uncorrect_name, Toast.LENGTH_LONG).show();
            return false;
        }
        if (password.getText().toString().length() < 8) {
            Toast.makeText(getContext(), R.string.uncorrect_password, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    public void pushData(SignUpResponseI<Getter> result) {
        try {
            MasterKey masterKey = new MasterKey.Builder(getActivity().getApplicationContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(getActivity().getApplicationContext(), "user", masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isGetter", true);
            editor.putString("login", result.user.getLogin());
            editor.putString("phone", result.user.getPhone());
            editor.putString("X5_id", result.user.getX5_Id());
            editor.putString("token", result.token);
            editor.apply();

            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();

        } catch (IOException | GeneralSecurityException err) {
            Log.e("auth error", err.toString());
            err.printStackTrace();
        }
    }

    public void login() {
        if (validate()) {
            btnLogin.setEnabled(false);
            AuthRepository.getterLogin(phone.getText().toString(), login.getText().toString(), password.getText().toString()).enqueue(new Callback<SignUpResponseI<Getter>>() {
                @Override
                public void onResponse(@NotNull Call<SignUpResponseI<Getter>> call, @NotNull Response<SignUpResponseI<Getter>> response) {
                    SignUpResponseI<Getter> result = response.body();
                    try {
                        if (response.code() == 400) {
                            btnLogin.setEnabled(true);
                            Toast.makeText(getContext(), R.string.not_right_password, Toast.LENGTH_SHORT).show();
                        } else if (result.token.length() == 0 || response.code() == 404) {
                            Toast.makeText(getContext(), R.string.account_not_exist, Toast.LENGTH_SHORT).show();
                            btnLogin.setEnabled(true);
                            btnSignup.setVisibility(View.VISIBLE);
                        } else pushData(result);
                    } catch (NullPointerException err) {
                        Toast.makeText(getContext(), R.string.account_not_exist, Toast.LENGTH_SHORT).show();
                        btnLogin.setEnabled(true);
                        btnSignup.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onFailure(Call<SignUpResponseI<Getter>> call, Throwable t) {
                    btnLogin.setEnabled(true);
                    t.printStackTrace();
                }
            });
        }
    }

    public void signup() {
        btnSignup.setEnabled(false);
        AuthRepository.getterRegistration(phone.getText().toString(), login.getText().toString(), password.getText().toString()).enqueue(new Callback<SignUpResponseI<Getter>>() {
            @Override
            public void onResponse(@NotNull Call<SignUpResponseI<Getter>> call, @NotNull Response<SignUpResponseI<Getter>> response) {
                SignUpResponseI<Getter> result = response.body();
                if (result.token.length() == 0 || response.code() == 400) {
                    Toast.makeText(getContext(), R.string.account_created, Toast.LENGTH_SHORT).show();
                    btnSignup.setEnabled(true);
                } else pushData(result);
            }

            @Override
            public void onFailure(Call<SignUpResponseI<Getter>> call, Throwable t) {
                btnSignup.setEnabled(true);
                t.printStackTrace();
            }
        });
    }
}