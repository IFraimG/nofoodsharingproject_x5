/**
 * Класс {@code GiverLoginAuthFragment} - фрагмент страницы входа отдающего пользователя
 * @author Кулагин Александр
 */

package com.buyhelp.nofoodsharingproject.presentation.fragments.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buyhelp.nofoodsharingproject.data.api.auth.AuthRepository;
import com.buyhelp.nofoodsharingproject.databinding.FragmentGiverLoginAuthBinding;
import com.buyhelp.nofoodsharingproject.presentation.ApplicationCore;
import com.buyhelp.nofoodsharingproject.presentation.activities.MainActivity;
import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.giver.GiverAuthViewModel;
import com.buyhelp.nofoodsharingproject.presentation.factories.giver.GiverAuthFactory;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

public class GiverLoginAuthFragment extends Fragment {
    private FragmentGiverLoginAuthBinding binding;
    private WeakReference<FragmentGiverLoginAuthBinding> mBinding;
    private GiverAuthViewModel viewModel;
    private AuthRepository authRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationCore app = (ApplicationCore) requireActivity().getApplication();
        authRepository = app.getAppComponent().getAuthRepository();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGiverLoginAuthBinding.inflate(inflater);
        mBinding = new WeakReference<>(binding);

        viewModel = new ViewModelProvider(requireActivity(),
                new GiverAuthFactory(requireActivity().getApplication(), authRepository))
                .get(GiverAuthViewModel.class);

        binding.authGiverLoginBack.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_giverLoginauthF_to_giverAuthF));
        binding.loginAuthBtn.setOnClickListener(View -> {
            binding.loginAuthBtn.setEnabled(false);

            String dtoLogin = binding.authGiverLoginLogin.getText().toString().replaceAll("\\s", "");
            String dtoPassword = binding.authGiverLoginPassword.getText().toString().replaceAll("\\s", "");

            if (dtoLogin.length() == 0 || dtoPassword.length() == 0) Snackbar.make(requireContext(), requireView(), getString(R.string.not_full), Snackbar.LENGTH_LONG).show();
            else {
                viewModel.login(dtoLogin, dtoPassword).observe(requireActivity(), code -> {
                    binding.loginAuthBtn.setEnabled(true);
                    if (code == 400) {
                        Snackbar.make(requireContext(), requireView(), getString(R.string.not_right_password), Snackbar.LENGTH_SHORT).show();
                        viewModel.clearStatusCode();
                    } else if (code == 404) {
                        Snackbar.make(requireActivity(), requireView(), getString(R.string.account_not_exist), Snackbar.LENGTH_SHORT).show();
                        viewModel.clearStatusCode();
                    } else if (code != 0 && code <= 299) {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                        requireActivity().finish();
                    }
                });
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mBinding.clear();
    }
}