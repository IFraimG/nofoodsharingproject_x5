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
import com.example.nofoodsharingproject.databinding.FragmentSetterAuthBinding;
import com.example.nofoodsharingproject.models.Setter;
import com.example.nofoodsharingproject.utils.DefineUser;
import com.example.nofoodsharingproject.utils.ValidateUser;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetterAuthFragment extends Fragment {
    private FragmentSetterAuthBinding binding;
    private DefineUser<Setter> defineUser;
    private AuthRepository authRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineUser = new DefineUser<>(requireActivity());
        authRepository = new AuthRepository();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetterAuthBinding.inflate(inflater);

        binding.setterAuthBtnReg.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_setterAuthF_to_setterLoginAuthF));
        binding.authSetterSignupBack.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_setterAuthF_to_mainAuthF));

        binding.setterAuthBtnLogin.setOnClickListener(View -> sendToNotifyAccount());

        return binding.getRoot();
    }

    private void signup(String tokenFCM) {
        if (validate()) {
            binding.setterAuthBtnLogin.setEnabled(false);
            authRepository.setterRegistration(requireContext(), binding.setterAuthPhone.getText().toString().replaceAll("\\s", ""), binding.setterAuthLogin.getText().toString().replaceAll("\\s", ""), binding.setterAuthPassword.getText().toString().replaceAll("\\s", ""), tokenFCM).enqueue(new Callback<SignUpResponseI<Setter>>() {
                @Override
                public void onResponse(@NotNull Call<SignUpResponseI<Setter>> call, @NotNull Response<SignUpResponseI<Setter>> response) {
                    if (response.code() == 401) {
                        Toast.makeText(getContext(), R.string.prodlem_on_autorization, Toast.LENGTH_LONG).show();
                        binding.setterAuthBtnLogin.setEnabled(true);
                    } else if (response.code() == 400) {
                        Toast.makeText(getContext(), R.string.account_created, Toast.LENGTH_LONG).show();
                        binding.setterAuthBtnLogin.setEnabled(true);
                    } else if (response.isSuccessful()) {
                        if (response.body() != null && response.body().getToken() != null) pushData(response.body());
                    } else {
                        Toast.makeText(getContext(), R.string.unvisinle_error, Toast.LENGTH_LONG).show();
                        binding.setterAuthBtnLogin.setEnabled(true);
                    }
                }
                @Override
                public void onFailure(@NotNull Call<SignUpResponseI<Setter>> call, @NotNull Throwable t) {
                    binding.setterAuthBtnLogin.setEnabled(true);
                    t.printStackTrace();
                }
            });
        }
    }

    private boolean validate() {
        if (!ValidateUser.validatePhone(binding.setterAuthPhone.getText().toString().replaceAll("\\s", ""))) {
            Toast.makeText(getContext(), R.string.uncorrect_number_phone, Toast.LENGTH_LONG).show();
            return false;
        }
        if (!ValidateUser.validateLogin(binding.setterAuthLogin.getText().toString().replaceAll("\\s", ""))) {
            Toast.makeText(getContext(), R.string.uncorrect_name, Toast.LENGTH_LONG).show();
            return false;
        }
        if (!ValidateUser.validatePassword(binding.setterAuthPassword.getText().toString().replaceAll("\\s", ""))) {
            Toast.makeText(getContext(), R.string.uncorrect_password, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void pushData(SignUpResponseI<Setter> result) {
        defineUser.saveUserData(false, result.user.getX5_Id(), result);

        Intent intent = new Intent(getContext(), MainActivity.class);

        startActivity(intent);
        requireActivity().finish();
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