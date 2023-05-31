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
import com.buyhelp.nofoodsharingproject.databinding.FragmentSetterLoginAuthBinding;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.setter.SetterAuthViewModel;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

public class SetterLoginAuthFragment extends Fragment {
    private FragmentSetterLoginAuthBinding binding;
    private SetterAuthViewModel viewModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetterLoginAuthBinding.inflate(inflater);

        viewModel = new ViewModelProvider(requireActivity(),
                (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(SetterAuthViewModel.class);

        binding.authSetterLoginBack.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_setterLoginAuthF_to_setterAuthF));
        binding.loginAuthBtn.setOnClickListener(View -> {
            binding.loginAuthBtn.setEnabled(false);

            String dtoLogin = binding.authSetterLoginLogin.getText().toString().replaceAll("\\s", "");
            String dtoPassword = binding.authSetterLoginPassword.getText().toString().replaceAll("\\s", "");

            if (dtoLogin.length() == 0 || dtoPassword.length() == 0) Snackbar.make(requireContext(), requireView(), getString(R.string.not_full), Snackbar.LENGTH_LONG).show();
            else {
                viewModel.login(dtoLogin, dtoPassword).observe(requireActivity(), setterSignUpResponseI -> {
                    if (setterSignUpResponseI == null) binding.loginAuthBtn.setEnabled(true);
                    else {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                        requireActivity().finish();
                    }
                });
            }
        });

        return binding.getRoot();
    }
}