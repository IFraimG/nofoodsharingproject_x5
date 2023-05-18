package com.example.nofoodsharingproject.fragments.setter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.activities.MainAuthActivity;
import com.example.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.example.nofoodsharingproject.data.api.adverts.dto.ResponseHistoryAdverts;
import com.example.nofoodsharingproject.data.api.setter.SetterRepository;
import com.example.nofoodsharingproject.databinding.FragmentSetterProfileBinding;
import com.example.nofoodsharingproject.models.Advertisement;
import com.example.nofoodsharingproject.models.Setter;
import com.example.nofoodsharingproject.utils.DefineUser;
import com.example.nofoodsharingproject.utils.PermissionHandler;
import com.example.nofoodsharingproject.utils.ValidateUser;
import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SetterProfileFragment extends Fragment {
    private FragmentSetterProfileBinding binding;
    private SwitchCompat switchLocation;
    private SwitchCompat switchNotification;
    private ListView historyList;
    private String[] advertisementsHistory;
    private TextView userName;
    private TextView userPhone;
    private TextView successProducts;
    private TextView historyTitle;
    private Button openVk;
    private Button cancelEditButton;
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayAdapter<String> arrayAdapter;
    private LinearLayout editElements;
    private EditText editLogin;
    private EditText editPhone;
    private EditText editPassword;
    private EditText editNewPassword;
    private Button saveEdit;
    private Setter user;
    private boolean isCheckedLocation = false;
    private boolean isCheckedNotification = false;

    private DefineUser<Setter> defineUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        defineUser = new DefineUser<Setter>(requireActivity());

        setHasOptionsMenu(true);

        this.user = defineUser.defineSetter();

        isCheckedLocation = defineUser.getPreferences("location");
        isCheckedNotification = defineUser.getPreferences("notificaiton");
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetterProfileBinding.inflate(inflater);

        importElements();
        getHistoryList();
        handlers();

        return binding.getRoot();
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
        toolbar.setOnMenuItemClickListener(this::toolbarHandle);
        saveEdit.setOnClickListener(View -> closeEdit());
        cancelEditButton.setOnClickListener(View -> removeEdit());

        swipeRefreshLayout.setOnRefreshListener(this::getHistoryList);

        openVk.setOnClickListener(View -> vkLoad());

        switchLocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) PermissionHandler.requestPermissions(requireActivity());
            isCheckedLocation = isChecked && PermissionHandler.checkPermissions(requireContext());
            switchLocation.setChecked(isCheckedLocation);
        });

        switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> isCheckedNotification = isChecked);
    }

    private void importElements() {
        switchLocation = binding.setterProfileLocation;
        switchNotification = binding.setterProfileNotifications;
        historyList = binding.setterProfileHistoryList;
        userName = binding.setterProfileName;
        successProducts = binding.setterProfileCount;
        openVk = binding.setterOpenVk;
        toolbar = binding.setterProfileToolbar;
        swipeRefreshLayout = binding.setterProfileSwiper;
        editElements = binding.setterProfileEdit;
        editLogin = binding.setterProfileEditLogin;
        editPhone = binding.setterProfileEditPhone;
        editPassword = binding.setterProfileEditOldPassword;
        editNewPassword = binding.setterProfileEditPassword;
        saveEdit = binding.setterProfileSave;
        userPhone = binding.setterProfilePhone;
        historyTitle = binding.setterProfileHistoryTitle;
        cancelEditButton = binding.setterProfileCancel;

        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);

        userName.setText(user.getLogin());
        userPhone.setText(user.getPhone());
        switchLocation.setChecked(isCheckedLocation);
    }

    private void getHistoryList() {
        AdvertsRepository.findSetterAdvertisements(this.user.getX5_Id()).enqueue(new Callback<ResponseHistoryAdverts>() {
            @Override
            public void onResponse(@NotNull Call<ResponseHistoryAdverts> call, @NotNull Response<ResponseHistoryAdverts> response) {
                if (response.code() == 400) Toast.makeText(requireContext(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
                else if (response.code() != 404 && response.body() != null) {
                    Advertisement[] result = response.body().getAdvertisements();
                    successProducts.setText(Integer.toString(result.length) + getString(R.string.some_success_products));

                    advertisementsHistory = new String[result.length];
                    for (int i = 0; i < result.length; i++) {
                        advertisementsHistory[i] = result[i].getTitle() + " - " + result[i].getListProducts().length + getString(R.string.product_text_success) + result[i].getDateOfCreated();
                    }

                    arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.item_getter_product_name, advertisementsHistory);
                    historyList.setAdapter(arrayAdapter);

                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseHistoryAdverts> call, @NotNull Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void editProfile() {
        this.historyList.setVisibility(View.GONE);
        this.openVk.setVisibility(View.GONE);
        this.historyTitle.setVisibility(View.GONE);
        this.editElements.setVisibility(View.VISIBLE);
    }

    private void closeEdit() {
        String editPhoneString = editPhone.getText().toString();
        String editLoginString = editLogin.getText().toString();
        String editPasswordString = editPassword.getText().toString();

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
        String newLogin = editLogin.getText().toString();
        String newPhone = editPhone.getText().toString();
        String newPassword = editNewPassword.getText().toString();
        String oldPasswordText = editPassword.getText().toString();
        SetterRepository.editProfile(user.getX5_Id(), newLogin, newPhone, newPassword, oldPasswordText).enqueue(new Callback<Setter>() {
            @Override
            public void onResponse(@NotNull Call<Setter> call, @NotNull Response<Setter> response) {
                if (response.code() == 400) Toast.makeText(getContext(), R.string.your_password_uncorrect, Toast.LENGTH_SHORT).show();
                if (response.code() == 201) {
                    Toast.makeText(getContext(), R.string.sucses, Toast.LENGTH_SHORT).show();
                    userName.setText(response.body().getLogin());
                    userPhone.setText(response.body().getPhone());

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
        saveEdit.setEnabled(isEnable);
        switchLocation.setEnabled(isEnable);
        switchNotification.setEnabled(isEnable);
    }

    private void removeEdit() {
        editLogin.setText("");
        editNewPassword.setText("");
        editPhone.setText("");
        editPassword.setText("");

        this.historyList.setVisibility(View.VISIBLE);
        this.openVk.setVisibility(View.VISIBLE);
        this.historyTitle.setVisibility(View.VISIBLE);
        this.editElements.setVisibility(View.GONE);
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