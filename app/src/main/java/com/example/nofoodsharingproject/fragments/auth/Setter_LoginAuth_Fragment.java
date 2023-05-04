package com.example.nofoodsharingproject.fragments.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nofoodsharingproject.MainActivity;
import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.data.api.auth.interfaces.SignUpResponseI;
import com.example.nofoodsharingproject.data.repository.AuthRepository;
import com.example.nofoodsharingproject.models.Setter;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Setter_LoginAuth_Fragment extends Fragment {
    Button btn = null;
    EditText loginInput = null;
    EditText passwordInput = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setter_login_auth, container, false);

        loginInput = view.findViewById(R.id.auth_setter_login_login);
        passwordInput = view.findViewById(R.id.auth_setter_login_password);

        ImageView btnBack = (ImageView) view.findViewById(R.id.auth_setter_login_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_setterLoginAuthF_to_setterAuthF);
            }
        });

        btn = (Button) view.findViewById(R.id.login_auth_btn);
        btn.setOnClickListener(View -> login());

        return view;
    }

    public void login() {
        if (loginInput.getText().toString().length() == 0 || passwordInput.getText().toString().length() == 0) {
            Toast.makeText(getContext(), R.string.not_full, Toast.LENGTH_LONG).show();
        } else {
            btn.setEnabled(false);
            AuthRepository.setterLogin(loginInput.getText().toString(), passwordInput.getText().toString()).enqueue(new Callback<SignUpResponseI<Setter>>() {
                @Override
                public void onResponse(@NotNull Call<SignUpResponseI<Setter>> call, @NotNull Response<SignUpResponseI<Setter>> response) {
                    SignUpResponseI<Setter> result = response.body();
                    if (response.code() == 404 || response.code() == 400 || result.getToken().length() == 0) {
                        Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                        btn.setEnabled(true);
                    } else {
                        saveData(response.body());
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }

                @Override
                public void onFailure(Call<SignUpResponseI<Setter>> call, Throwable t) {
                    btn.setEnabled(true);
                    t.printStackTrace();
                }
            });
        }
    }

    public void saveData(SignUpResponseI<Setter> result) {
        try {
            MasterKey masterKey = new MasterKey.Builder(getActivity().getApplicationContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(getActivity().getApplicationContext(), "user", masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putBoolean("isGetter", false);
            editor.putString("login", result.user.getLogin());
            editor.putString("phone", result.user.getPhone());
            editor.putString("X5_id", result.user.getX5_Id());
            editor.putString("token", result.token);
            editor.apply();
        } catch (IOException | GeneralSecurityException err) {
            Log.e("auth error", err.toString());
            err.printStackTrace();
        }
    }
}