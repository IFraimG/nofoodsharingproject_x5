/**
 * Класс {@code GiverAuthFragment} - фрагмент страницы регистрации отдаюзего пользователя
 * @author Кулагин Александр
 */

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
import com.buyhelp.nofoodsharingproject.databinding.FragmentGiverAuthBinding;
import com.buyhelp.nofoodsharingproject.domain.helpers.ValidateUser;
import com.buyhelp.nofoodsharingproject.presentation.ApplicationCore;
import com.buyhelp.nofoodsharingproject.presentation.activities.MainActivity;
import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.needy.NeedyAuthViewModel;
import com.buyhelp.nofoodsharingproject.presentation.factories.needy.NeedyAuthFactory;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.giver.GiverAuthViewModel;
import com.buyhelp.nofoodsharingproject.presentation.factories.giver.GiverAuthFactory;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

public class GiverAuthFragment extends Fragment {
    private FragmentGiverAuthBinding binding;
    private WeakReference<FragmentGiverAuthBinding> mBinding;
    private GiverAuthViewModel viewModel;
    private NeedyAuthViewModel viewModelNeedy;
    private AuthRepository authRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationCore app = (ApplicationCore) requireActivity().getApplication();
        authRepository = app.getAppComponent().getAuthRepository();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGiverAuthBinding.inflate(inflater);
        mBinding = new WeakReference<>(binding);

        viewModel = new ViewModelProvider(requireActivity(),
                new GiverAuthFactory(requireActivity().getApplication(), authRepository))
                .get(GiverAuthViewModel.class);
        viewModelNeedy = new ViewModelProvider(requireActivity(),
                new NeedyAuthFactory(requireActivity().getApplication(), authRepository))
                .get(NeedyAuthViewModel.class);


        binding.giverAuthBtnReg.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_giverAuthF_to_giverLoginauthF));
        binding.authGiverSignupBack.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_giverAuthF_to_mainAuthF));

        binding.giverAuthBtnLogin.setOnClickListener(View -> viewModelNeedy.sendToNotifyAccount().observe(requireActivity(), tokenFCM -> {
            String dtoPhone = binding.giverAuthPhone.getText().toString().replaceAll("\\s", "");
            String dtoLogin = binding.giverAuthLogin.getText().toString().replaceAll("\\s", "");
            String dtoPassword = binding.giverAuthPassword.getText().toString().replaceAll("\\s", "");

            if (ValidateUser.isValidate(requireContext(), dtoPhone, dtoLogin, dtoPassword)) {
                binding.giverAuthBtnLogin.setEnabled(false);
                viewModel.signup(tokenFCM, dtoPhone, dtoLogin, dtoPassword).observe(requireActivity(), giverSignUpResponseI -> {
                    binding.giverAuthBtnLogin.setEnabled(true);

                    int code = viewModel.getStatusCode();
                    if (code == 400) Snackbar.make(requireContext(), requireView(), getString(R.string.account_created), Snackbar.LENGTH_SHORT).show();
                    else if (code == 403) Snackbar.make(requireContext(), requireView(), getString(R.string.data_repeat), Snackbar.LENGTH_SHORT).show();
                    else if (code <= 299) {
                        Intent intent = new Intent(requireContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mBinding.clear();
    }
}