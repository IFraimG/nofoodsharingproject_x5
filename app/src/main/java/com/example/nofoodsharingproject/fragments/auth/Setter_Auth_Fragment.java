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
import android.widget.TextView;
import android.widget.Toast;

import com.example.nofoodsharingproject.MainActivity;
import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.data.api.auth.dto.SignUpResponseI;
import com.example.nofoodsharingproject.data.api.auth.AuthRepository;
import com.example.nofoodsharingproject.databinding.FragmentSetterAuthBinding;
import com.example.nofoodsharingproject.models.Setter;
import com.example.nofoodsharingproject.utils.ValidateUser;
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
    private FragmentSetterAuthBinding binding;
    private EditText phone = null;
    private EditText login = null;
    private EditText password = null;
    private Button btnLogin = null;
    private TextView btnRegistration = null;
    private ImageView btnBack = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetterAuthBinding.inflate(inflater);

        btnRegistration = binding.setterAuthBtnReg;
        btnBack = binding.authSetterSignupBack;
        btnLogin = binding.setterAuthBtnLogin;
        phone = binding.setterAuthPhone;
        login = binding.setterAuthLogin;
        password = binding.setterAuthPassword;

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

        btnLogin.setOnClickListener(View -> sendToNotifyAccount());

        return binding.getRoot();
    }

    private void signup(String tokenFCM) {
        if (validate()) {
            btnLogin.setEnabled(false);
            AuthRepository.setterRegistration(phone.getText().toString(), login.getText().toString(), password.getText().toString(), tokenFCM).enqueue(new Callback<SignUpResponseI<Setter>>() {
                @Override
                public void onResponse(@NotNull Call<SignUpResponseI<Setter>> call, @NotNull Response<SignUpResponseI<Setter>> response) {
                    SignUpResponseI<Setter> result = response.body();
                    if (response.code() == 401 || result.token == null) {
                        Toast.makeText(getContext(), R.string.prodlem_on_autorization, Toast.LENGTH_LONG).show();
                        btnLogin.setEnabled(true);
                    } else if (response.code() == 400 || result.token.length() == 0) {
                        Toast.makeText(getContext(), R.string.account_created, Toast.LENGTH_LONG).show();
                        btnLogin.setEnabled(true);
                    } else pushData(result);
                }

                @Override
                public void onFailure(@NotNull Call<SignUpResponseI<Setter>> call, @NotNull Throwable t) {
                    btnLogin.setEnabled(true);
                    t.printStackTrace();
                }
            });
        }
    }

    private boolean validate() {
        if (!ValidateUser.validatePhone(phone.getText().toString())) {
            Toast.makeText(getContext(), R.string.uncorrect_number_phone, Toast.LENGTH_LONG).show();
            return false;
        }
        if (!ValidateUser.validateLogin(login.getText().toString())) {
            Toast.makeText(getContext(), R.string.uncorrect_name, Toast.LENGTH_LONG).show();
            return false;
        }
        if (!ValidateUser.validatePassword(password.getText().toString())) {
            Toast.makeText(getContext(), R.string.uncorrect_password, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void pushData(SignUpResponseI<Setter> result) {
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
            editor.putString("auth_id", result.user.getAuthID());
            editor.putString("token", result.token);
            editor.putString("FCMtoken", result.user.getTokenFCM());
            editor.apply();

            startActivity(intent);
//            getActivity().finish();

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
                    signup("");
                    return;
                }
                signup(task.getResult());
            }
        });
    }
}