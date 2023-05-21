package com.example.nofoodsharingproject.fragments.auth;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private DefineUser<Getter> defineUser;
    private AuthRepository authRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineUser = new DefineUser<>(requireActivity());
        authRepository = new AuthRepository();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGetterAuthBinding.inflate(inflater);

        binding.authGetterCreate.setOnClickListener(View -> sendToNotifyAccount());
        binding.authGetterBtnLogin.setOnClickListener(View -> login());
        binding.authGetterSignupBack.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_getterAuthF_to_mainAuthF));

        return binding.getRoot();
    }

    private boolean validate() {
        if (!ValidateUser.validatePhone(binding.authGetterSignupPhone.getText().toString())) {
            Toast.makeText(getContext(), R.string.uncorrect_number_phone, Toast.LENGTH_LONG).show();
            return false;
        }
        if (!ValidateUser.validateLogin(binding.authGetterSignupLogin.getText().toString())) {
            Toast.makeText(getContext(), R.string.uncorrect_name, Toast.LENGTH_LONG).show();
            return false;
        }
        if (!ValidateUser.validatePassword(binding.authGetterSignupPassword.getText().toString())) {
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
            binding.authGetterBtnLogin.setEnabled(false);
            authRepository.getterLogin(requireContext(), binding.authGetterSignupPhone.getText().toString(), binding.authGetterSignupLogin.getText().toString(), binding.authGetterSignupPassword.getText().toString()).enqueue(new Callback<SignUpResponseI<Getter>>() {
                @Override
                public void onResponse(@NotNull Call<SignUpResponseI<Getter>> call, @NotNull Response<SignUpResponseI<Getter>> response) {
                    if (response.code() == 400) {
                        binding.authGetterBtnLogin.setEnabled(true);
                        Toast.makeText(getContext(), R.string.not_right_password, Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 404) {
                        Toast.makeText(getContext(), R.string.account_not_exist, Toast.LENGTH_SHORT).show();
                        binding.authGetterBtnLogin.setEnabled(true);
                        binding.authGetterCreate.setVisibility(View.VISIBLE);
                    } else if (response.isSuccessful()) {
                        if (response.body() != null && response.body().token != null) pushData(response.body());
                    } else {
                        binding.authGetterBtnLogin.setEnabled(true);
                        Toast.makeText(getContext(), R.string.unvisinle_error, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<SignUpResponseI<Getter>> call, @NotNull Throwable t) {
                    binding.authGetterBtnLogin.setEnabled(true);
                    t.printStackTrace();
                }
            });
        }
    }

    private void signup(String tokenFCM) {
        binding.authGetterCreate.setEnabled(false);
        authRepository.getterRegistration(requireContext(), binding.authGetterSignupPhone.getText().toString(), binding.authGetterSignupLogin.getText().toString(), binding.authGetterSignupPassword.getText().toString(), tokenFCM).enqueue(new Callback<SignUpResponseI<Getter>>() {
            @Override
            public void onResponse(@NotNull Call<SignUpResponseI<Getter>> call, @NotNull Response<SignUpResponseI<Getter>> response) {
                if (response.code() == 400) {
                    Toast.makeText(getContext(), R.string.account_created, Toast.LENGTH_SHORT).show();
                    binding.authGetterCreate.setEnabled(true);
                } else if (response.isSuccessful()) {
                    if (response.body() != null && response.body().token != null) pushData(response.body());
                } else {
                    Toast.makeText(getContext(), R.string.unvisinle_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<SignUpResponseI<Getter>> call, @NotNull Throwable t) {
                binding.authGetterCreate.setEnabled(true);
                t.printStackTrace();
            }
        });
    }

    private void sendToNotifyAccount() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w("err", getString(R.string.error_fmc), task.getException());
                signup("");
                return;
            }
            signup(task.getResult());
        });
    }
}