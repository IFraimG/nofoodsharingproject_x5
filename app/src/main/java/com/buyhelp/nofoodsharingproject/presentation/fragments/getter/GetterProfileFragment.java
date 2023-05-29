package com.buyhelp.nofoodsharingproject.presentation.fragments.getter;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.presentation.activities.MainAuthActivity;
import com.buyhelp.nofoodsharingproject.data.api.getter.GetterRepository;
import com.buyhelp.nofoodsharingproject.databinding.FragmentGetterProfileBinding;
import com.buyhelp.nofoodsharingproject.data.models.ShortDataUser;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.buyhelp.nofoodsharingproject.domain.helpers.ValidateUser;
import com.buyhelp.nofoodsharingproject.data.models.Getter;
import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetterProfileFragment extends Fragment {
    private FragmentGetterProfileBinding binding;
    private ShortDataUser user;
    private DefineUser<Getter> defineUser;
    private GetterRepository getterRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineUser = new DefineUser<>(requireActivity());

        getterRepository = new GetterRepository();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGetterProfileBinding.inflate(inflater);

        user = defineUser.getUser();

        binding.getterProfileLogin.setText(user.getLogin());
        binding.getterProfilePhone.setText(user.getPhone());

        binding.getterProfileLogout.setOnClickListener(View -> logout());
        binding.getterProfileSave.setOnClickListener(View -> editProfile());

        binding.getterProfileOpenChat.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_getterProfileF_to_chatsListFragment));

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
            getterRepository.editProfile(requireContext(), user.getX5_Id(), newLogin, newPhone, newPassword, oldPasswordText).enqueue(new Callback<Getter>() {
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
}