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

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.data.api.getter.GetterRepository;
import com.buyhelp.nofoodsharingproject.data.api.getter.dto.RequestGetterEditProfile;
import com.buyhelp.nofoodsharingproject.presentation.ApplicationCore;
import com.buyhelp.nofoodsharingproject.presentation.activities.MainAuthActivity;
import com.buyhelp.nofoodsharingproject.databinding.FragmentGetterProfileBinding;
import com.buyhelp.nofoodsharingproject.data.models.ShortDataUser;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.buyhelp.nofoodsharingproject.domain.helpers.ValidateUser;
import com.buyhelp.nofoodsharingproject.data.models.Getter;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.getter.GetterProfileViewModel;
import com.buyhelp.nofoodsharingproject.presentation.factories.getters.GetterProfileFactory;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

public class GetterProfileFragment extends Fragment {
    private FragmentGetterProfileBinding binding;
    private WeakReference<FragmentGetterProfileBinding> mBinding;
    private ShortDataUser user;
    private DefineUser defineUser;
    private GetterProfileViewModel viewModel;
    private GetterRepository getterRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationCore app = (ApplicationCore) requireActivity().getApplication();
        getterRepository = app.getAppComponent().getGetterRepository();
        defineUser = app.getHelpersComponent().getDefineUser();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGetterProfileBinding.inflate(inflater);
        mBinding = new WeakReference<>(binding);

        user = defineUser.getUser();

        viewModel = new ViewModelProvider(requireActivity(),
                new GetterProfileFactory(requireActivity().getApplication(), getterRepository))
                .get(GetterProfileViewModel.class);

        binding.getterProfileLogin.setText(user.getLogin());
        binding.getterProfilePhone.setText(user.getPhone());

        binding.getterProfileLogout.setOnClickListener(View -> logout());
        binding.getterProfileSave.setOnClickListener(View -> editProfile());

        binding.getterProfileOpenChat.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_getterProfileF_to_chatsListFragment));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding.clear();
    }

    private void editProfile() {
        String newLogin = binding.getterProfileEditLogin.getText().toString();
        String newPhone = binding.getterProfileEditPhone.getText().toString();
        String newPassword = binding.getterProfileEditPassword.getText().toString();
        String oldPasswordText = binding.getterProfileEditOldPassword.getText().toString();

        if (!ValidateUser.validatePhone(newPhone)) {
            Snackbar.make(requireContext(), requireView(), getString(R.string.uncorrect_number_phone), Snackbar.LENGTH_LONG).show();
        } else if (!ValidateUser.validateLogin(newLogin)) {
            Snackbar.make(requireContext(), requireView(), getString(R.string.uncorrect_name), Snackbar.LENGTH_LONG).show();
        } else if (!ValidateUser.validatePassword(newPassword)) {
            Snackbar.make(requireContext(), requireView(), getString(R.string.uncorrect_password), Snackbar.LENGTH_LONG).show();
        } else {
            binding.getterProfileSave.setEnabled(false);
            viewModel.editProfile(defineUser, new RequestGetterEditProfile(user.getX5_Id(), newLogin, newPhone, newPassword, oldPasswordText)).observe(requireActivity(), new Observer<Getter>() {
                @Override
                public void onChanged(Getter getter) {
                    int code = viewModel.getStatusCode();
                    if (code == 403) Snackbar.make(requireContext(), requireView(), getString(R.string.used_data), Snackbar.LENGTH_SHORT).show();
                    else if (code > 299) Snackbar.make(requireContext(), requireView(), getString(R.string.your_password_uncorrect), Snackbar.LENGTH_SHORT).show();

                    if (getter != null) {
                        binding.getterProfileLogin.setText(getter.getLogin());
                        binding.getterProfilePhone.setText(getter.getPhone());

                        defineUser.editProfileInfo(getter.getLogin(), getter.getPhone());

                        binding.getterProfileEditLogin.setText("");
                        binding.getterProfileEditPassword.setText("");
                        binding.getterProfileEditPhone.setText("");
                        binding.getterProfileEditOldPassword.setText("");

                        Snackbar.make(requireContext(), requireView(), getString(R.string.sucses), Snackbar.LENGTH_SHORT).show();
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