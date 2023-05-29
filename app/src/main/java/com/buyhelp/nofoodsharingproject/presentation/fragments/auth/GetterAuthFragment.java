package com.buyhelp.nofoodsharingproject.presentation.fragments.auth;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buyhelp.nofoodsharingproject.presentation.activities.MainActivity;
import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.databinding.FragmentGetterAuthBinding;
import com.buyhelp.nofoodsharingproject.presentation.view_models.GetterAuthViewModel;

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

        binding.authGetterCreate.setOnClickListener(View -> {
            viewModel.sendToNotifyAccount().observe(requireActivity(), tokenFCM -> {
                String dtoPhone = binding.authGetterSignupPhone.getText().toString().replaceAll("\\s", "");
                String dtoLogin = binding.authGetterSignupLogin.getText().toString().replaceAll("\\s", "");
                String dtoPassword = binding.authGetterSignupPassword.getText().toString().replaceAll("\\s", "");

                binding.authGetterCreate.setEnabled(false);
                binding.authGetterCreate.setEnabled(false);

                viewModel.signup(tokenFCM, dtoPhone, dtoLogin, dtoPassword).observe(requireActivity(), getterSignUpResponseI -> {
                    binding.authGetterCreate.setEnabled(true);
                    binding.authGetterCreate.setEnabled(true);

                    if (getterSignUpResponseI != null) {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                        requireActivity().finish();
                    }
                });
            });
        });

        binding.authGetterBtnLogin.setOnClickListener(View -> {
            String dtoPhone = binding.authGetterSignupPhone.getText().toString().replaceAll("\\s", "");
            String dtoLogin = binding.authGetterSignupLogin.getText().toString().replaceAll("\\s", "");
            String dtoPassword = binding.authGetterSignupPassword.getText().toString().replaceAll("\\s", "");

            binding.authGetterBtnLogin.setEnabled(false);
            binding.authGetterCreate.setEnabled(false);
            if (viewModel.validate(dtoPhone, dtoLogin, dtoPassword)) {
                viewModel.login(dtoPhone, dtoLogin, dtoPassword).observe(requireActivity(), getterSignUpResponseI -> {
                    binding.authGetterBtnLogin.setEnabled(true);
                    binding.authGetterCreate.setEnabled(true);
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