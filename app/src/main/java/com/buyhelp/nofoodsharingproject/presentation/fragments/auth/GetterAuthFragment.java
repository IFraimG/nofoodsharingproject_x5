package com.buyhelp.nofoodsharingproject.presentation.fragments.auth;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buyhelp.nofoodsharingproject.domain.helpers.ValidateUser;
import com.buyhelp.nofoodsharingproject.presentation.activities.MainActivity;
import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.databinding.FragmentGetterAuthBinding;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.getter.GetterAuthViewModel;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

public class GetterAuthFragment extends Fragment {
    private FragmentGetterAuthBinding binding;
    private GetterAuthViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGetterAuthBinding.inflate(inflater);

        viewModel = new ViewModelProvider(requireActivity(),
                (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(GetterAuthViewModel.class);

        binding.authGetterCreate.setOnClickListener(v -> {
            viewModel.sendToNotifyAccount().observe(requireActivity(), tokenFCM -> {
                String dtoPhone = binding.authGetterSignupPhone.getText().toString().replaceAll("\\s", "");
                String dtoLogin = binding.authGetterSignupLogin.getText().toString().replaceAll("\\s", "");
                String dtoPassword = binding.authGetterSignupPassword.getText().toString().replaceAll("\\s", "");

                binding.authGetterCreate.setEnabled(false);
                binding.authGetterCreate.setEnabled(false);

                viewModel.signup(tokenFCM, dtoPhone, dtoLogin, dtoPassword).observe(requireActivity(), getterSignUpResponseI -> {
                    binding.authGetterCreate.setEnabled(true);
                    binding.authGetterCreate.setEnabled(true);

                    int code = viewModel.getStatusCode();
                    if (code == 400) Snackbar.make(requireContext(), v, getString(R.string.account_created), Snackbar.LENGTH_SHORT).show();
                    else {
                        if (getterSignUpResponseI != null) {
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);
                            requireActivity().finish();
                        }
                    }
                });
            });
        });

        binding.authGetterBtnLogin.setOnClickListener(v -> {
            String dtoPhone = binding.authGetterSignupPhone.getText().toString().replaceAll("\\s", "");
            String dtoLogin = binding.authGetterSignupLogin.getText().toString().replaceAll("\\s", "");
            String dtoPassword = binding.authGetterSignupPassword.getText().toString().replaceAll("\\s", "");

            binding.authGetterBtnLogin.setEnabled(false);
            binding.authGetterCreate.setEnabled(false);
            if (ValidateUser.isValidate(requireContext(), dtoPhone, dtoLogin, dtoPassword)) {
                viewModel.login(dtoPhone, dtoLogin, dtoPassword).observe(requireActivity(), getterSignUpResponseI -> {
                    binding.authGetterBtnLogin.setEnabled(true);
                    binding.authGetterCreate.setEnabled(true);

                    int code = viewModel.getStatusCode();
                    if (code == 400) Snackbar.make(requireContext(), v, getString(R.string.not_right_password), Snackbar.LENGTH_SHORT).show();
                    else if (code == 404) Snackbar.make(requireContext(), v, getText(R.string.account_not_exist), Snackbar.LENGTH_SHORT).show();

                    if (getterSignUpResponseI == null) {
                        binding.authGetterCreate.setVisibility(android.view.View.VISIBLE);
                    } else {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                    }

                });
            } else {
                binding.authGetterBtnLogin.setEnabled(true);
                binding.authGetterCreate.setEnabled(true);
            }
        });

        binding.authGetterSignupBack.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_getterAuthF_to_mainAuthF));

        return binding.getRoot();
    }
}