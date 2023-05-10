package com.example.nofoodsharingproject.fragments.getter;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.activities.MainAuth_Activity;
import com.example.nofoodsharingproject.data.repository.GetterRepository;
import com.example.nofoodsharingproject.databinding.FragmentGetterProfileBinding;
import com.example.nofoodsharingproject.utils.ValidateUser;
import com.example.nofoodsharingproject.models.Getter;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetterProfile_Fragment extends Fragment {
    private FragmentGetterProfileBinding binding;
    private EditText editLogin;
    private EditText editPhone;
    private EditText editPassword;
    private EditText oldPassword;
    private Button buttonLogout;
    private Button btnSave;
    private TextView login;
    private TextView phone;
    private Getter user;

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            MasterKey masterKey = new MasterKey.Builder(getActivity().getApplicationContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();
            sharedPreferences = EncryptedSharedPreferences.create(getActivity().getApplicationContext(), "user", masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        } catch (IOException | GeneralSecurityException err) {
            err.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGetterProfileBinding.inflate(inflater);

        buttonLogout = binding.getterProfileLogout;
        editLogin = binding.getterProfileEditLogin;
        editPhone = binding.getterProfileEditPhone;
        editPassword = binding.getterProfileEditPassword;
        btnSave = binding.getterProfileSave;
        login = binding.getterProfileLogin;
        phone = binding.getterProfilePhone;
        oldPassword = binding.getterProfileEditOldPassword;

        user = defineUser();

        login.setText(user.getLogin());
        phone.setText(user.getPhone());

        buttonLogout.setOnClickListener(View -> logout());
        btnSave.setOnClickListener(View -> editProfile());

        return binding.getRoot();
    }

    public Getter defineUser() {
        String login = sharedPreferences.getString("login", "");
        String phone = sharedPreferences.getString("phone", "");
        String userID = sharedPreferences.getString("X5_id", "");

        Getter user = new Getter();
        user.setLogin(login);
        user.setPhone(phone);
        user.setX5_Id(userID);

        return user;
    }

    private void editProfile() {
        if (!ValidateUser.validatePhone(editPhone.getText().toString())) {
            Toast.makeText(getContext(), R.string.uncorrect_number_phone, Toast.LENGTH_LONG).show();
        } else if (!ValidateUser.validateLogin(editLogin.getText().toString())) {
            Toast.makeText(getContext(), R.string.uncorrect_name, Toast.LENGTH_LONG).show();
        } else if (!ValidateUser.validatePassword(editPassword.getText().toString())) {
            Toast.makeText(getContext(), R.string.uncorrect_password, Toast.LENGTH_LONG).show();
        } else {
            btnSave.setEnabled(false);
            String newLogin = editLogin.getText().toString();
            String newPhone = editPhone.getText().toString();
            String newPassword = editPassword.getText().toString();
            String oldPasswordText = oldPassword.getText().toString();
            GetterRepository.editProfile(user.getX5_Id(), newLogin, newPhone, newPassword, oldPasswordText).enqueue(new Callback<Getter>() {
                @Override
                public void onResponse(@NotNull Call<Getter> call, @NotNull Response<Getter> response) {
                    if (response.code() == 400) Toast.makeText(getContext(), "Ваш старый пароль введен неверно", Toast.LENGTH_SHORT).show();
                    if (response.code() == 201) {
                        Toast.makeText(getContext(), "Успешно!", Toast.LENGTH_SHORT).show();
                        login.setText(response.body().getLogin());
                        phone.setText(response.body().getPhone());

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("login", response.body().getLogin());
                        editor.putString("phone", response.body().getPhone());

                        editor.apply();

                        editLogin.setText("");
                        editPassword.setText("");
                        editPhone.setText("");
                        oldPassword.setText("");
                    }
                    btnSave.setEnabled(true);
                }

                @Override
                public void onFailure(Call<Getter> call, Throwable t) {
                    t.printStackTrace();
                    btnSave.setEnabled(true);
                    Toast.makeText(getContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(getActivity().getApplicationContext(), MainAuth_Activity.class);
        startActivity(intent);
        getActivity().finish();
    }
}