package com.buyhelp.nofoodsharingproject.presentation.fragments.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buyhelp.nofoodsharingproject.data.api.auth.AuthRepository;
import com.buyhelp.nofoodsharingproject.databinding.FragmentNeedyAuthBinding;
import com.buyhelp.nofoodsharingproject.domain.helpers.ValidateUser;
import com.buyhelp.nofoodsharingproject.presentation.ApplicationCore;
import com.buyhelp.nofoodsharingproject.presentation.activities.MainActivity;
import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.presentation.factories.needy.NeedyAuthFactory;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.needy.NeedyAuthViewModel;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

public class NeedyAuthFragment extends Fragment {
    private FragmentNeedyAuthBinding binding;
    private WeakReference<FragmentNeedyAuthBinding> mBinding;
    private NeedyAuthViewModel viewModel;
    private int code;
    public AuthRepository authRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationCore app = (ApplicationCore) requireActivity().getApplication();
        authRepository = app.getAppComponent().getAuthRepository();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNeedyAuthBinding.inflate(inflater);
        mBinding = new WeakReference<>(binding);

        viewModel = new ViewModelProvider(requireActivity(), new NeedyAuthFactory(requireActivity().getApplication(), authRepository)).get(NeedyAuthViewModel.class);

        binding.authNeedyCreate.setOnClickListener(v -> {
            viewModel.sendToNotifyAccount().observe(requireActivity(), tokenFCM -> {
                String dtoPhone = binding.authNeedySignupPhone.getText().toString().replaceAll("\\s", "");
                String dtoLogin = binding.authNeedySignupLogin.getText().toString().replaceAll("\\s", "");
                String dtoPassword = binding.authNeedySignupPassword.getText().toString().replaceAll("\\s", "");

                binding.authNeedyCreate.setEnabled(false);
                binding.authNeedyCreate.setEnabled(false);

                viewModel.signup(tokenFCM, dtoPhone, dtoLogin, dtoPassword).observe(requireActivity(), needySignUpResponseI -> {
                    binding.authNeedyCreate.setEnabled(true);
                    binding.authNeedyCreate.setEnabled(true);

                    int code = viewModel.getStatusCode();
                    if (code == 400) Snackbar.make(requireContext(), v, getString(R.string.account_created), Snackbar.LENGTH_SHORT).show();
                    else if (code == 403) {
                        Snackbar.make(requireContext(), v, getString(R.string.data_repeat), Snackbar.LENGTH_SHORT).show();
                    } else {
                        if (needySignUpResponseI != null) {
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);
                            requireActivity().finish();
                        }
                    }
                });
            });
        });

        binding.authNeedyBtnLogin.setOnClickListener(v -> {
            String dtoPhone = binding.authNeedySignupPhone.getText().toString().replaceAll("\\s", "");
            String dtoLogin = binding.authNeedySignupLogin.getText().toString().replaceAll("\\s", "");
            String dtoPassword = binding.authNeedySignupPassword.getText().toString().replaceAll("\\s", "");

            binding.authNeedyBtnLogin.setEnabled(false);
            binding.authNeedyCreate.setEnabled(false);
            if (ValidateUser.isValidate(requireContext(), dtoPhone, dtoLogin, dtoPassword)) {
                viewModel.login(dtoPhone, dtoLogin, dtoPassword).observe(requireActivity(), needySignUpResponseI -> {
                    binding.authNeedyBtnLogin.setEnabled(true);
                    binding.authNeedyCreate.setEnabled(true);

                    code = viewModel.getStatusCode();
                    if (code == 400) Snackbar.make(requireContext(), v, getString(R.string.not_right_password), Snackbar.LENGTH_SHORT).show();
                    else if (code == 403) {
                        Snackbar.make(requireContext(), v, getString(R.string.data_repeat), Snackbar.LENGTH_SHORT).show();
                        binding.authNeedyCreate.setVisibility(android.view.View.GONE);
                    }
                    else if (code == 404) Snackbar.make(requireContext(), v, getText(R.string.account_not_exist), Snackbar.LENGTH_SHORT).show();

                    if (code != 403) {
                        if (needySignUpResponseI == null) {
                            binding.authNeedyCreate.setVisibility(android.view.View.VISIBLE);
                        } else {
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            } else {
                binding.authNeedyBtnLogin.setEnabled(true);
                binding.authNeedyCreate.setEnabled(true);
            }
        });

        binding.authNeedySignupBack.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_needyAuthF_to_mainAuthF));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mBinding.clear();
    }
}