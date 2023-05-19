package com.example.nofoodsharingproject.fragments.auth;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nofoodsharingproject.activities.MainActivity;
import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.data.api.auth.dto.SignUpResponseI;
import com.example.nofoodsharingproject.data.api.auth.AuthRepository;
import com.example.nofoodsharingproject.databinding.FragmentSetterLoginAuthBinding;
import com.example.nofoodsharingproject.models.Setter;
import com.example.nofoodsharingproject.utils.DefineUser;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetterLoginAuthFragment extends Fragment {
    private FragmentSetterLoginAuthBinding binding;
    private DefineUser<Setter> defineUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        defineUser = new DefineUser<>(requireActivity());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetterLoginAuthBinding.inflate(inflater);


        binding.authSetterLoginBack.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_setterLoginAuthF_to_setterAuthF));
        binding.loginAuthBtn.setOnClickListener(View -> login());

        return binding.getRoot();
    }

    private void login() {
        if (binding.authSetterLoginLogin.getText().toString().length() == 0 || binding.authSetterLoginPassword.getText().toString().length() == 0) {
            Toast.makeText(getContext(), R.string.not_full, Toast.LENGTH_LONG).show();
        } else {
            binding.loginAuthBtn.setEnabled(false);
            AuthRepository.setterLogin(binding.authSetterLoginLogin.getText().toString(), binding.authSetterLoginPassword.getText().toString()).enqueue(new Callback<SignUpResponseI<Setter>>() {
                @Override
                public void onResponse(@NotNull Call<SignUpResponseI<Setter>> call, @NotNull Response<SignUpResponseI<Setter>> response) {
                    if (response.code() == 404 || response.code() == 400) {
                        Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                        binding.loginAuthBtn.setEnabled(true);
                    } else if (response.isSuccessful()) {
                        if (response.body() != null && response.body().token != null) {
                            defineUser.saveUserData(false, response.body().user.getX5_Id(), response.body());
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        binding.loginAuthBtn.setEnabled(true);
                        Toast.makeText(getContext(), R.string.unvisinle_error, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<SignUpResponseI<Setter>> call, @NotNull Throwable t) {
                    binding.loginAuthBtn.setEnabled(true);
                    t.printStackTrace();
                }
            });
        }
    }
}