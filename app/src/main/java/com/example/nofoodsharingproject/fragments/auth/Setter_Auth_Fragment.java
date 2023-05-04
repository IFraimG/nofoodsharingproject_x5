package com.example.nofoodsharingproject.fragments.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Setter_Auth_Fragment extends Fragment {
    EditText phone = null;
    EditText login = null;
    EditText password = null;
    Button btnLogin = null;
    Button btnRegistration = null;
    ImageView btnBack = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setter_auth, container, false);

        btnRegistration = (Button) view.findViewById(R.id.setter_auth_btn_reg);
        btnBack = (ImageView) view.findViewById(R.id.auth_setter_signup_back);
        btnLogin = (Button) view.findViewById(R.id.setter_auth_btn_login);
        phone = (EditText) view.findViewById(R.id.setter_auth_phone);
        login = (EditText) view.findViewById(R.id.setter_auth_login);
        password = (EditText) view.findViewById(R.id.setter_auth_password);

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_setterAuthF_to_setterLoginAuthF);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_setterAuthF_to_mainAuthF);
            }
        });

        btnLogin.setOnClickListener(View -> signup());

        return view;
    }

    public void signup() {
        if (validate()) {
            btnLogin.setEnabled(false);
            AuthRepository.setterRegistration(phone.getText().toString(), login.getText().toString(), password.getText().toString()).enqueue(new Callback<SignUpResponseI<Setter>>() {
                @Override
                public void onResponse(@NotNull Call<SignUpResponseI<Setter>> call, @NotNull Response<SignUpResponseI<Setter>> response) {
                    SignUpResponseI<Setter> result = response.body();
                    if (response.code() == 400 || result.token.length() == 0) {
                        Toast.makeText(getContext(), "Пользователь с этим номером телефона или логином уже существует", Toast.LENGTH_LONG).show();
                        btnLogin.setEnabled(true);
                    } else pushData(result);
                }

                @Override
                public void onFailure(Call<SignUpResponseI<Setter>> call, Throwable t) {
                    btnLogin.setEnabled(true);
                    t.printStackTrace();
                }
            });
        }
    }

    public boolean validate() {
        String regexPhone = "^\\+?[0-9\\-\\s]*$";
        if (!phone.getText().toString().matches(regexPhone)) {
            Toast.makeText(getContext(), "Номер телефона введен некорректно", Toast.LENGTH_LONG).show();
            return false;
        }
        if (login.getText().toString().length() < 4) {
            Toast.makeText(getContext(), "Ваш логин должен содержать хотя бы 4 символа", Toast.LENGTH_LONG).show();
            return false;
        }
        if (password.getText().toString().length() < 8) {
            Toast.makeText(getContext(), "Ваш пароль должен содержать хотя бы 8 символов", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void pushData(SignUpResponseI<Setter> result) {
        try {
            Intent intent = new Intent(getContext(), MainActivity.class);

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

            sendToNotifyAccount();
            startActivity(intent);
            getActivity().finish();

        } catch (IOException | GeneralSecurityException err) {
            Log.e("auth error", err.toString());
            err.printStackTrace();
        }
    }

    private void sendToNotifyAccount() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("err", "Fetching FCM registration token failed", task.getException());
                    return;
                }

                String token = task.getResult();
                Log.d("msg", token);
                Toast.makeText(getContext(), token, Toast.LENGTH_SHORT).show();
            }
        });
    }
}