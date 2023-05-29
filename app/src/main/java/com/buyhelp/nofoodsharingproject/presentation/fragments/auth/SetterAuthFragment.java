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
import com.buyhelp.nofoodsharingproject.databinding.FragmentSetterAuthBinding;
import com.buyhelp.nofoodsharingproject.presentation.view_models.GetterAuthViewModel;
import com.buyhelp.nofoodsharingproject.presentation.view_models.SetterAuthViewModel;
import org.jetbrains.annotations.NotNull;

public class SetterAuthFragment extends Fragment {
    private FragmentSetterAuthBinding binding;
    private SetterAuthViewModel viewModel;
    private GetterAuthViewModel viewModelGetter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetterAuthBinding.inflate(inflater);

        viewModel = new ViewModelProvider(requireActivity(),
                (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(SetterAuthViewModel.class);
        viewModelGetter = new ViewModelProvider(requireActivity(),
                (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
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

                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                        requireActivity().finish();
                    });
                }
            });
        });

        return binding.getRoot();
    }
}