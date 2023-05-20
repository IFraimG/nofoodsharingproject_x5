package com.example.nofoodsharingproject.fragments.getter;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.activities.ChatsListActivity;
import com.example.nofoodsharingproject.activities.MainAuthActivity;
import com.example.nofoodsharingproject.data.api.getter.GetterRepository;
import com.example.nofoodsharingproject.databinding.FragmentGetterProfileBinding;
import com.example.nofoodsharingproject.models.ShortDataUser;
import com.example.nofoodsharingproject.utils.DefineUser;
import com.example.nofoodsharingproject.utils.ValidateUser;
import com.example.nofoodsharingproject.models.Getter;
import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetterProfileFragment extends Fragment {
    private FragmentGetterProfileBinding binding;
    private ShortDataUser user;
    private DefineUser<Getter> defineUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineUser = new DefineUser<>(requireActivity());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGetterProfileBinding.inflate(inflater);

        user = defineUser.getUser();

        binding.getterProfileLogin.setText(user.getLogin());
        binding.getterProfilePhone.setText(user.getPhone());

        binding.getterProfileLogout.setOnClickListener(View -> logout());
        binding.getterProfileSave.setOnClickListener(View -> editProfile());

        binding.getterProfileOpenChat.setOnClickListener(View -> openChat());

        return binding.getRoot();
    }


    private void editProfile() {
        String newLogin = binding.getterProfileEditLogin.getText().toString();
        String newPhone = binding.getterProfileEditPhone.getText().toString();
        String newPassword = binding.getterProfileEditPassword.getText().toString();
        String oldPasswordText = binding.getterProfileEditOldPassword.getText().toString();

        if (!ValidateUser.validatePhone(newPhone)) {
            Toast.makeText(getContext(), R.string.uncorrect_number_phone, Toast.LENGTH_LONG).show();
        } else if (!ValidateUser.validateLogin(newLogin)) {
            Toast.makeText(getContext(), R.string.uncorrect_name, Toast.LENGTH_LONG).show();
        } else if (!ValidateUser.validatePassword(newPassword)) {
            Toast.makeText(getContext(), R.string.uncorrect_password, Toast.LENGTH_LONG).show();
        } else {
            binding.getterProfileSave.setEnabled(false);
            GetterRepository.editProfile(user.getX5_Id(), newLogin, newPhone, newPassword, oldPasswordText).enqueue(new Callback<Getter>() {
                @Override
                public void onResponse(@NotNull Call<Getter> call, @NotNull Response<Getter> response) {
                    if (response.code() == 400) Toast.makeText(getContext(), R.string.your_password_uncorrect, Toast.LENGTH_SHORT).show();
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(getContext(), R.string.sucses, Toast.LENGTH_SHORT).show();
                        binding.getterProfileLogin.setText(response.body().getLogin());
                        binding.getterProfilePhone.setText(response.body().getPhone());

                        defineUser.editProfileInfo(response.body().getLogin(), response.body().getPhone());

                        binding.getterProfileEditLogin.setText("");
                        binding.getterProfileEditPassword.setText("");
                        binding.getterProfileEditPhone.setText("");
                        binding.getterProfileEditOldPassword.setText("");
                    } else Toast.makeText(requireContext(), getString(R.string.unvisinle_error), Toast.LENGTH_SHORT).show();
                    binding.getterProfileSave.setEnabled(true);
                }

                @Override
                public void onFailure(@NotNull Call<Getter> call, @NotNull Throwable t) {
                    t.printStackTrace();
                    binding.getterProfileSave.setEnabled(true);
                    Toast.makeText(getContext(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void logout() {
        defineUser.clearData();

        Intent intent = new Intent(requireActivity().getApplicationContext(), MainAuthActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    private void openChat() {
        Intent intent = new Intent(requireActivity(), ChatsListActivity.class);
        startActivity(intent);
    }
}