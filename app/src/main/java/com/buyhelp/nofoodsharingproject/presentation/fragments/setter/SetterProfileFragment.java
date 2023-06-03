package com.buyhelp.nofoodsharingproject.presentation.fragments.setter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.buyhelp.nofoodsharingproject.data.api.getter.dto.RequestGetterEditProfile;
import com.buyhelp.nofoodsharingproject.data.api.setter.SetterRepository;
import com.buyhelp.nofoodsharingproject.presentation.ApplicationCore;
import com.buyhelp.nofoodsharingproject.presentation.activities.MainAuthActivity;
import com.buyhelp.nofoodsharingproject.databinding.FragmentSetterProfileBinding;
import com.buyhelp.nofoodsharingproject.data.models.Setter;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.buyhelp.nofoodsharingproject.domain.helpers.PermissionHandler;
import com.buyhelp.nofoodsharingproject.domain.helpers.ValidateUser;
import com.buyhelp.nofoodsharingproject.presentation.activities.SetterActivity;
import com.buyhelp.nofoodsharingproject.presentation.factories.setters.SetterProfileFactory;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.setter.SetterProfileViewModel;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.List;


public class SetterProfileFragment extends Fragment {
    private FragmentSetterProfileBinding binding;
    private WeakReference<FragmentSetterProfileBinding> mBinding;
    private ArrayAdapter<String> arrayAdapter;
    private Setter user;
    private boolean isCheckedLocation = false;
    private boolean isCheckedNotification = false;

    private DefineUser defineUser;
    private SetterProfileViewModel viewModel;
    private SetterRepository setterRepository;
    private AdvertsRepository advertsRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationCore app = (ApplicationCore) requireActivity().getApplication();
        setterRepository = app.getAppComponent().getSetterRepository();
        advertsRepository = app.getAppComponent().getAdvertsRepository();
        defineUser = app.getHelpersComponent().getDefineUser();

        setHasOptionsMenu(true);

        this.user = defineUser.defineSetter();

        isCheckedLocation = defineUser.getPreferences("location");
        isCheckedNotification = defineUser.getPreferences("notificaiton");
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetterProfileBinding.inflate(inflater);
        mBinding = new WeakReference<>(binding);

        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(binding.setterProfileToolbar);

        binding.setterProfileName.setText(user.getLogin());
        binding.setterProfilePhone.setText(user.getPhone());
        binding.setterProfileLocation.setChecked(isCheckedLocation);
        binding.setterProfileOpenChat.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_setterProfileF_to_chatsListFragment));

        handlers();

        viewModel = new ViewModelProvider(requireActivity(),
                new SetterProfileFactory(requireActivity().getApplication(), setterRepository, advertsRepository))
                .get(SetterProfileViewModel.class);

        viewModel.getHistoryAdverts(user.getX5_Id()).observe(requireActivity(), this::getHistoryList);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (getActivity() instanceof SetterActivity) {
            ((SetterActivity) requireActivity()).setBottomNavigationVisibility(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        requireActivity().getMenuInflater().inflate(R.menu.profile_left_panel_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private boolean toolbarHandle(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile_leave:
                logout();
                break;
            case R.id.edit_settings:
                editProfile();
                break;
            default:
                break;
        }
        return false;
    }

    private void handlers() {
        binding.setterProfileToolbar.setOnMenuItemClickListener(this::toolbarHandle);
        binding.setterProfileSave.setOnClickListener(View -> closeEdit());
        binding.setterProfileCancel.setOnClickListener(View -> removeEdit());

        binding.setterProfileSwiper.setOnRefreshListener(() -> viewModel.getHistoryAdverts(user.getX5_Id()).observe(requireActivity(), this::getHistoryList));
        binding.setterOpenVk.setOnClickListener(View -> vkLoad());

        binding.setterProfileLocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) PermissionHandler.requestPermissions(requireActivity());
            isCheckedLocation = isChecked && PermissionHandler.checkPermissions(requireContext());
            binding.setterProfileLocation.setChecked(isCheckedLocation);
        });

        binding.setterProfileNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> isCheckedNotification = isChecked);
    }

    private void getHistoryList(List<String> result) {
        if (result != null) {
            binding.setterProfileCount.setText("Успешных передач продуктов: " + Integer.toString(result.size()));
            if (arrayAdapter == null) {
                arrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.item_getter_product_name, result);
                binding.setterProfileHistoryList.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
            } else {
                arrayAdapter.clear();
                arrayAdapter.addAll(result);
                arrayAdapter.notifyDataSetChanged();
            }
        }
        binding.setterProfileSwiper.setRefreshing(false);
    }

    private void editProfile() {
        binding.setterProfileHistoryList.setVisibility(View.GONE);
        binding.setterOpenVk.setVisibility(View.GONE);
        binding.setterProfileHistoryTitle.setVisibility(View.GONE);
        binding.setterProfileOpenChat.setVisibility(View.GONE);
        binding.setterProfileEdit.setVisibility(View.VISIBLE);
        binding.setterProfileSwiper.setEnabled(false);

        if (getActivity() instanceof SetterActivity) {
            ((SetterActivity) requireActivity()).setBottomNavigationVisibility(false);
        }
    }

    private void closeEdit() {
        String editPhoneString = binding.setterProfileEditPhone.getText().toString();
        String editLoginString = binding.setterProfileEditLogin.getText().toString();
        String editPasswordString = binding.setterProfileEditOldPassword.getText().toString();

        if (editLoginString.length() == 0 && editPhoneString.length() == 0 && editPasswordString.length() == 0) {
            defineUser.setToPreferences("location", isCheckedLocation);
            defineUser.setToPreferences("notification", isCheckedNotification);
            removeEdit();

            return;
        }

        if (!ValidateUser.validatePhone(editPhoneString)) {
            Snackbar.make(requireContext(), requireView(), getString(R.string.uncorrect_number_phone), Snackbar.LENGTH_LONG).show();
        } else if (!ValidateUser.validateLogin(editLoginString)) {
            Snackbar.make(requireContext(), requireView(), getString(R.string.uncorrect_name), Snackbar.LENGTH_LONG).show();
        } else if (!ValidateUser.validatePassword(editPasswordString)) {
            Snackbar.make(requireContext(), requireView(), getString(R.string.uncorrect_password), Snackbar.LENGTH_LONG).show();
        } else {
            enabledButton(false);

            defineUser.setToPreferences("location", isCheckedLocation);
            defineUser.setToPreferences("notification", isCheckedNotification);

            saveEditProfile();
        }
    }

    private void saveEditProfile() {
        String newLogin = binding.setterProfileEditLogin.getText().toString();
        String newPhone = binding.setterProfileEditPhone.getText().toString();
        String newPassword = binding.setterProfileEditPassword.getText().toString();
        String oldPasswordText = binding.setterProfileEditOldPassword.getText().toString();

        viewModel.editProfile(new RequestGetterEditProfile(user.getX5_Id(), newLogin, newPhone, newPassword, oldPasswordText)).observe(requireActivity(), setter -> {
            int code = viewModel.getStatusCode();
            if (code == 403) Snackbar.make(requireContext(), requireView(), getString(R.string.used_data), Snackbar.LENGTH_SHORT).show();
            else if (code > 299) Snackbar.make(requireContext(), requireView(), getString(R.string.uncorrect_password), Snackbar.LENGTH_SHORT).show();

            if (setter != null) {
                Snackbar.make(requireContext(), requireView(), getString(R.string.sucses), Snackbar.LENGTH_SHORT).show();
                binding.setterProfileName.setText(setter.getLogin());
                binding.setterProfilePhone.setText(setter.getPhone());

                defineUser.editProfileInfo(setter.getLogin(), setter.getPhone());
                Snackbar.make(requireContext(), requireView(), getString(R.string.sucses), Snackbar.LENGTH_SHORT).show();
            }
            enabledButton(true);
            removeEdit();
        });
    }

    private void enabledButton(boolean isEnable) {
        binding.setterProfileSave.setEnabled(isEnable);
        binding.setterProfileLocation.setEnabled(isEnable);
        binding.setterProfileNotifications.setEnabled(isEnable);
    }

    private void removeEdit() {
        binding.setterProfileEditLogin.setText("");
        binding.setterProfileEditPassword.setText("");
        binding.setterProfileEditPhone.setText("");
        binding.setterProfileEditOldPassword.setText("");

        binding.setterProfileHistoryList.setVisibility(View.VISIBLE);
        binding.setterOpenVk.setVisibility(View.VISIBLE);
        binding.setterProfileHistoryTitle.setVisibility(View.VISIBLE);
        binding.setterProfileOpenChat.setVisibility(View.VISIBLE);
        binding.setterProfileEdit.setVisibility(View.GONE);
        binding.setterProfileSwiper.setEnabled(true);

        if (getActivity() instanceof SetterActivity) {
            ((SetterActivity) requireActivity()).setBottomNavigationVisibility(true);
        }
    }

    private void logout() {
        defineUser.clearData();

        Intent intent = new Intent(requireActivity(), MainAuthActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    private void vkLoad() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.vk_link)));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Snackbar.make(requireContext(), requireView(), getString(R.string.smth_wrong), Snackbar.LENGTH_SHORT).show();
        }
    }
}