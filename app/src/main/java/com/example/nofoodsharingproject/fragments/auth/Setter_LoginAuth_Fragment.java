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

import com.example.nofoodsharingproject.activities.Main_Activity;
import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.data.api.auth.dto.SignUpResponseI;
import com.example.nofoodsharingproject.data.api.auth.AuthRepository;
import com.example.nofoodsharingproject.databinding.FragmentSetterLoginAuthBinding;
import com.example.nofoodsharingproject.models.Setter;
import com.example.nofoodsharingproject.utils.DefineUser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Setter_LoginAuth_Fragment extends Fragment {
    private FragmentSetterLoginAuthBinding binding;
    private Button btn;
    private EditText loginInput;
    private EditText passwordInput;
    private ImageView btnBack;
    private DefineUser<Setter> defineUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        defineUser = new DefineUser<Setter>(requireActivity());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetterLoginAuthBinding.inflate(inflater);

        loginInput = binding.authSetterLoginLogin;
        passwordInput = binding.authSetterLoginPassword;
        btn = binding.loginAuthBtn;
        btnBack = binding.authSetterLoginBack;

        btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_setterLoginAuthF_to_setterAuthF));

        btn.setOnClickListener(View -> login());

        return binding.getRoot();
    }

    private void login() {
        if (loginInput.getText().toString().length() == 0 || passwordInput.getText().toString().length() == 0) {
            Toast.makeText(getContext(), R.string.not_full, Toast.LENGTH_LONG).show();
        } else {
            btn.setEnabled(false);
            AuthRepository.setterLogin(loginInput.getText().toString(), passwordInput.getText().toString()).enqueue(new Callback<SignUpResponseI<Setter>>() {
                @Override
                public void onResponse(@NotNull Call<SignUpResponseI<Setter>> call, @NotNull Response<SignUpResponseI<Setter>> response) {
                    if (response.code() == 404 || response.code() == 400) {
                        Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                        btn.setEnabled(true);
                    } else {
                        if (response.body() != null && response.body().token != null) {
                            defineUser.saveUserData(false, response.body().user.getX5_Id(), response.body());
                            Intent intent = new Intent(getContext(), Main_Activity.class);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<SignUpResponseI<Setter>> call, @NotNull Throwable t) {
                    btn.setEnabled(true);
                    t.printStackTrace();
                }
            });
        }
    }
}