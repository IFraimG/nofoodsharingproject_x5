package com.example.nofoodsharingproject.fragments.auth;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nofoodsharingproject.activities.MainActivity;
import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.data.api.auth.dto.SignUpResponseI;
import com.example.nofoodsharingproject.data.api.auth.AuthRepository;
import com.example.nofoodsharingproject.databinding.FragmentGetterAuthBinding;
import com.example.nofoodsharingproject.models.Getter;
import com.example.nofoodsharingproject.utils.DefineUser;
import com.example.nofoodsharingproject.utils.ValidateUser;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetterAuthFragment extends Fragment {
    private FragmentGetterAuthBinding binding;
    private EditText phone = null;
    private EditText login = null;
    private EditText password = null;
    private Button btnSignup = null;
    private Button btnLogin = null;
    private ImageView btnBack = null;
    private DefineUser<Getter> defineUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineUser = new DefineUser<Getter>(requireActivity());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGetterAuthBinding.inflate(inflater);

        btnLogin = binding.authGetterBtnLogin;
        phone = binding.authGetterSignupPhone;
        password = binding.authGetterSignupPassword;
        login = binding.authGetterSignupLogin;
        btnSignup = binding.authGetterCreate;
        btnBack = binding.authGetterSignupBack;

        btnSignup.setOnClickListener(View -> sendToNotifyAccount());
        btnLogin.setOnClickListener(View -> login());
        btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_getterAuthF_to_mainAuthF));

        return binding.getRoot();
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


    private void pushData(SignUpResponseI<Getter> result) {
        defineUser.saveUserData(true, result.user.getX5_Id(), result);

        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }

    private void login() {
        if (validate()) {
            btnLogin.setEnabled(false);
            AuthRepository.getterLogin(phone.getText().toString(), login.getText().toString(), password.getText().toString()).enqueue(new Callback<SignUpResponseI<Getter>>() {
                @Override
                public void onResponse(@NotNull Call<SignUpResponseI<Getter>> call, @NotNull Response<SignUpResponseI<Getter>> response) {
                    if (response.code() == 400) {
                        btnLogin.setEnabled(true);
                        Toast.makeText(getContext(), R.string.not_right_password, Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 404) {
                        Toast.makeText(getContext(), R.string.account_not_exist, Toast.LENGTH_SHORT).show();
                        btnLogin.setEnabled(true);
                        btnSignup.setVisibility(View.VISIBLE);
                    } else {
                        if (response.body() != null && response.body().token != null) pushData(response.body());
                    }
                }

                @Override
                public void onFailure(@NotNull Call<SignUpResponseI<Getter>> call, @NotNull Throwable t) {
                    btnLogin.setEnabled(true);
                    t.printStackTrace();
                }
            });
        }
    }

    private void signup(String tokenFCM) {
        btnSignup.setEnabled(false);
        AuthRepository.getterRegistration(phone.getText().toString(), login.getText().toString(), password.getText().toString(), tokenFCM).enqueue(new Callback<SignUpResponseI<Getter>>() {
            @Override
            public void onResponse(@NotNull Call<SignUpResponseI<Getter>> call, @NotNull Response<SignUpResponseI<Getter>> response) {
                if (response.code() == 400) {
                    Toast.makeText(getContext(), R.string.account_created, Toast.LENGTH_SHORT).show();
                    btnSignup.setEnabled(true);
                } else {
                    if (response.body() != null && response.body().token != null) pushData(response.body());
                }
            }

            @Override
            public void onFailure(@NotNull Call<SignUpResponseI<Getter>> call, @NotNull Throwable t) {
                btnSignup.setEnabled(true);
                t.printStackTrace();
            }
        });
    }

    private void sendToNotifyAccount() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w("err", "Fetching FCM registration token failed", task.getException());
                signup("");
                return;
            }
            signup(task.getResult());
        });
    }
}