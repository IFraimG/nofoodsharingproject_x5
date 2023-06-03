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
import com.buyhelp.nofoodsharingproject.domain.helpers.ValidateUser;
import com.buyhelp.nofoodsharingproject.presentation.ApplicationCore;
import com.buyhelp.nofoodsharingproject.presentation.activities.MainActivity;
import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.databinding.FragmentSetterAuthBinding;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.getter.GetterAuthViewModel;
import com.buyhelp.nofoodsharingproject.presentation.factories.getters.GetterAuthFactory;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.setter.SetterAuthViewModel;
import com.buyhelp.nofoodsharingproject.presentation.factories.setters.SetterAuthFactory;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

public class SetterAuthFragment extends Fragment {
    private FragmentSetterAuthBinding binding;
    private WeakReference<FragmentSetterAuthBinding> mBinding;
    private SetterAuthViewModel viewModel;
    private GetterAuthViewModel viewModelGetter;
    private AuthRepository authRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationCore app = (ApplicationCore) requireActivity().getApplication();
        authRepository = app.getAppComponent().getAuthRepository();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetterAuthBinding.inflate(inflater);
        mBinding = new WeakReference<>(binding);

        viewModel = new ViewModelProvider(requireActivity(),
                new SetterAuthFactory(requireActivity().getApplication(), authRepository))
                .get(SetterAuthViewModel.class);
        viewModelGetter = new ViewModelProvider(requireActivity(),
                new GetterAuthFactory(requireActivity().getApplication(), authRepository))
                .get(GetterAuthViewModel.class);


        binding.setterAuthBtnReg.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_setterAuthF_to_setterLoginAuthF));
        binding.authSetterSignupBack.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_setterAuthF_to_mainAuthF));

        binding.setterAuthBtnLogin.setOnClickListener(View -> {
            viewModelGetter.sendToNotifyAccount().observe(requireActivity(), tokenFCM -> {
                String dtoPhone = binding.setterAuthPhone.getText().toString().replaceAll("\\s", "");
                String dtoLogin = binding.setterAuthLogin.getText().toString().replaceAll("\\s", "");
                String dtoPassword = binding.setterAuthPassword.getText().toString().replaceAll("\\s", "");

                if (ValidateUser.isValidate(requireContext(), dtoPhone, dtoLogin, dtoPassword)) {
                    binding.setterAuthBtnLogin.setEnabled(false);
                    viewModel.signup(tokenFCM, dtoPhone, dtoLogin, dtoPassword).observe(requireActivity(), setterSignUpResponseI -> {
                        binding.setterAuthBtnLogin.setEnabled(true);

                        int code = viewModel.getStatusCode();
                        if (code == 400) Snackbar.make(requireContext(), requireView(), getString(R.string.account_created), Snackbar.LENGTH_SHORT).show();
                        else if (code == 403) Snackbar.make(requireContext(), requireView(), getString(R.string.data_repeat), Snackbar.LENGTH_SHORT).show();
                        else if (code <= 299) {
                            Intent intent = new Intent(requireContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            });
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mBinding.clear();
    }
}