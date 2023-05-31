package com.buyhelp.nofoodsharingproject.presentation.fragments.getter;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.data.api.getter.dto.RequestGetterEditProfile;
import com.buyhelp.nofoodsharingproject.presentation.activities.MainAuthActivity;
import com.buyhelp.nofoodsharingproject.databinding.FragmentGetterProfileBinding;
import com.buyhelp.nofoodsharingproject.data.models.ShortDataUser;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.buyhelp.nofoodsharingproject.domain.helpers.ValidateUser;
import com.buyhelp.nofoodsharingproject.data.models.Getter;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.getter.GetterProfileViewModel;

import org.jetbrains.annotations.NotNull;

public class GetterProfileFragment extends Fragment {
    private FragmentGetterProfileBinding binding;
    private ShortDataUser user;
    private DefineUser<Getter> defineUser;
    private GetterProfileViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineUser = new DefineUser<>(requireActivity());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGetterProfileBinding.inflate(inflater);

        user = defineUser.getUser();

        viewModel = new ViewModelProvider(requireActivity(),
                (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(GetterProfileViewModel.class);

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
            viewModel.editProfile(defineUser, new RequestGetterEditProfile(user.getX5_Id(), newLogin, newPhone, newPassword, oldPasswordText)).observe(requireActivity(), new Observer<Getter>() {
                @Override
                public void onChanged(Getter getter) {
                    if (getter != null) {
                        binding.getterProfileLogin.setText(getter.getLogin());
                        binding.getterProfilePhone.setText(getter.getPhone());

                        defineUser.editProfileInfo(getter.getLogin(), getter.getPhone());

                        binding.getterProfileEditLogin.setText("");
                        binding.getterProfileEditPassword.setText("");
                        binding.getterProfileEditPhone.setText("");
                        binding.getterProfileEditOldPassword.setText("");
                    }

                    binding.getterProfileSave.setEnabled(true);
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