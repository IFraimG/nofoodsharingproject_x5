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
import android.widget.Toast;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.presentation.activities.MainAuthActivity;
import com.buyhelp.nofoodsharingproject.data.api.setter.SetterRepository;
import com.buyhelp.nofoodsharingproject.databinding.FragmentSetterProfileBinding;
import com.buyhelp.nofoodsharingproject.data.models.Setter;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.buyhelp.nofoodsharingproject.domain.helpers.PermissionHandler;
import com.buyhelp.nofoodsharingproject.domain.helpers.ValidateUser;
import com.buyhelp.nofoodsharingproject.presentation.activities.SetterActivity;
import com.buyhelp.nofoodsharingproject.presentation.view_models.SetterProfileViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SetterProfileFragment extends Fragment {
    private FragmentSetterProfileBinding binding;
    private ArrayAdapter<String> arrayAdapter;
    private Setter user;
    private boolean isCheckedLocation = false;
    private boolean isCheckedNotification = false;

    private DefineUser<Setter> defineUser;
    private SetterRepository setterRepository;
    private SetterProfileViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        defineUser = new DefineUser<>(requireActivity());

        setHasOptionsMenu(true);

        this.user = defineUser.defineSetter();
        this.setterRepository = new SetterRepository();

        isCheckedLocation = defineUser.getPreferences("location");
        isCheckedNotification = defineUser.getPreferences("notificaiton");
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetterProfileBinding.inflate(inflater);

        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(binding.setterProfileToolbar);

        binding.setterProfileName.setText(user.getLogin());
        binding.setterProfilePhone.setText(user.getPhone());
        binding.setterProfileLocation.setChecked(isCheckedLocation);
        binding.setterProfileOpenChat.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_setterProfileF_to_chatsListFragment));

        handlers();

        viewModel = new ViewModelProvider(requireActivity(),
                (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(SetterProfileViewModel.class);

        viewModel.getHistoryAdverts(user.getX5_Id()).observe(requireActivity(), this::getHistoryList);

        return binding.getRoot();
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
                arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.item_getter_product_name, result);
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
            Toast.makeText(getContext(), R.string.uncorrect_number_phone, Toast.LENGTH_LONG).show();
        } else if (!ValidateUser.validateLogin(editLoginString)) {
            Toast.makeText(getContext(), R.string.uncorrect_name, Toast.LENGTH_LONG).show();
        } else if (!ValidateUser.validatePassword(editPasswordString)) {
            Toast.makeText(getContext(), R.string.uncorrect_password, Toast.LENGTH_LONG).show();
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
        setterRepository.editProfile(requireContext(), user.getX5_Id(), newLogin, newPhone, newPassword, oldPasswordText).enqueue(new Callback<Setter>() {
            @Override
            public void onResponse(@NotNull Call<Setter> call, @NotNull Response<Setter> response) {
                if (response.code() == 400) Toast.makeText(getContext(), R.string.your_password_uncorrect, Toast.LENGTH_SHORT).show();
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), R.string.sucses, Toast.LENGTH_SHORT).show();
                    binding.setterProfileName.setText(response.body().getLogin());
                    binding.setterProfilePhone.setText(response.body().getPhone());

                    defineUser.editProfileInfo(response.body().getLogin(), response.body().getPhone());
                }

                enabledButton(true);
                removeEdit();
            }

            @Override
            public void onFailure(@NotNull Call<Setter> call, @NotNull Throwable t) {
                t.printStackTrace();
                enabledButton(true);
                Toast.makeText(getContext(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
            }
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
            Toast.makeText(getContext(), getString(R.string.smth_wrong), Toast.LENGTH_SHORT).show();
        }
    }
}