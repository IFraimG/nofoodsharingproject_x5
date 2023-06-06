/**
 * Класс {@code NeedyProfileFragment} - фрагмент для редактирования профиля отдающего
 * @author Кулагин Александр
 */


package com.buyhelp.nofoodsharingproject.presentation.fragments.needy;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.data.api.needy.NeedyRepository;
import com.buyhelp.nofoodsharingproject.data.api.needy.dto.RequestNeedyEditProfile;
import com.buyhelp.nofoodsharingproject.databinding.FragmentNeedyProfileBinding;
import com.buyhelp.nofoodsharingproject.presentation.ApplicationCore;
import com.buyhelp.nofoodsharingproject.presentation.activities.MainAuthActivity;
import com.buyhelp.nofoodsharingproject.data.models.ShortDataUser;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.buyhelp.nofoodsharingproject.domain.helpers.ValidateUser;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.needy.NeedyProfileViewModel;
import com.buyhelp.nofoodsharingproject.presentation.factories.needy.NeedyProfileFactory;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

public class NeedyProfileFragment extends Fragment {
    private FragmentNeedyProfileBinding binding;
    private WeakReference<FragmentNeedyProfileBinding> mBinding;
    private ShortDataUser user;
    private DefineUser defineUser;
    private NeedyProfileViewModel viewModel;
    private NeedyRepository needyRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationCore app = (ApplicationCore) requireActivity().getApplication();
        needyRepository = app.getAppComponent().getNeedyRepository();
        defineUser = app.getHelpersComponent().getDefineUser();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNeedyProfileBinding.inflate(inflater);
        mBinding = new WeakReference<>(binding);

        user = defineUser.getUser();

        viewModel = new ViewModelProvider(requireActivity(),
                new NeedyProfileFactory(requireActivity().getApplication(), needyRepository))
                .get(NeedyProfileViewModel.class);

        binding.needyProfileLogin.setText(user.getLogin());
        binding.needyProfilePhone.setText(user.getPhone());

        binding.needyProfileLogout.setOnClickListener(View -> logout());
        binding.needyProfileSave.setOnClickListener(View -> editProfile());

        binding.needyProfileOpenChat.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_needyProfileF_to_chatsListFragment));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding.clear();
    }

    private void editProfile() {
        String newLogin = binding.needyProfileEditLogin.getText().toString();
        String newPhone = binding.needyProfileEditPhone.getText().toString();
        String newPassword = binding.needyProfileEditPassword.getText().toString();
        String oldPasswordText = binding.needyProfileEditOldPassword.getText().toString();

        if (!ValidateUser.validatePhone(newPhone)) {
            Snackbar.make(requireContext(), requireView(), getString(R.string.uncorrect_number_phone), Snackbar.LENGTH_LONG).show();
        } else if (!ValidateUser.validateLogin(newLogin)) {
            Snackbar.make(requireContext(), requireView(), getString(R.string.uncorrect_name), Snackbar.LENGTH_LONG).show();
        } else if (!ValidateUser.validatePassword(newPassword)) {
            Snackbar.make(requireContext(), requireView(), getString(R.string.uncorrect_password), Snackbar.LENGTH_LONG).show();
        } else {
            binding.needyProfileSave.setEnabled(false);
            viewModel.editProfile(defineUser, new RequestNeedyEditProfile(user.getX5_Id(), newLogin, newPhone, newPassword, oldPasswordText)).observe(requireActivity(), needy -> {
                int code = viewModel.getStatusCode();
                if (code == 403) Snackbar.make(requireContext(), requireView(), getString(R.string.used_data), Snackbar.LENGTH_SHORT).show();
                else if (code > 299) Snackbar.make(requireContext(), requireView(), getString(R.string.your_password_uncorrect), Snackbar.LENGTH_SHORT).show();

                if (needy != null) {
                    binding.needyProfileLogin.setText(needy.getLogin());
                    binding.needyProfilePhone.setText(needy.getPhone());

                    defineUser.editProfileInfo(needy.getLogin(), needy.getPhone());

                    binding.needyProfileEditLogin.setText("");
                    binding.needyProfileEditPassword.setText("");
                    binding.needyProfileEditPhone.setText("");
                    binding.needyProfileEditOldPassword.setText("");

                    Snackbar.make(requireContext(), requireView(), getString(R.string.sucses), Snackbar.LENGTH_SHORT).show();
                }

                binding.needyProfileSave.setEnabled(true);
            });
        }
    }

    /**
     * Этот метод позволяет выйти из аккаунта
     */
    private void logout() {
        defineUser.clearData();

        Intent intent = new Intent(requireActivity().getApplicationContext(), MainAuthActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }
}