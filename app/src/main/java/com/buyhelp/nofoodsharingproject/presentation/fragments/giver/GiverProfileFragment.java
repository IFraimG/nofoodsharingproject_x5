package com.buyhelp.nofoodsharingproject.presentation.fragments.giver;

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
import com.buyhelp.nofoodsharingproject.data.api.giver.GiverRepository;
import com.buyhelp.nofoodsharingproject.data.api.needy.dto.RequestNeedyEditProfile;
import com.buyhelp.nofoodsharingproject.databinding.FragmentGiverProfileBinding;
import com.buyhelp.nofoodsharingproject.presentation.ApplicationCore;
import com.buyhelp.nofoodsharingproject.presentation.activities.GiverActivity;
import com.buyhelp.nofoodsharingproject.presentation.activities.MainAuthActivity;
import com.buyhelp.nofoodsharingproject.data.models.Giver;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.buyhelp.nofoodsharingproject.domain.helpers.PermissionHandler;
import com.buyhelp.nofoodsharingproject.domain.helpers.ValidateUser;
import com.buyhelp.nofoodsharingproject.presentation.di.components.DaggerPermissionComponent;
import com.buyhelp.nofoodsharingproject.presentation.di.components.PermissionComponent;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.ActivityModule;
import com.buyhelp.nofoodsharingproject.presentation.di.modules.PermissionHandlerModule;
import com.buyhelp.nofoodsharingproject.presentation.factories.giver.GiverProfileFactory;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.giver.GiverProfileViewModel;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.List;


public class GiverProfileFragment extends Fragment {
    private FragmentGiverProfileBinding binding;
    private WeakReference<FragmentGiverProfileBinding> mBinding;
    private ArrayAdapter<String> arrayAdapter;
    private Giver user;
    private boolean isCheckedLocation = false;
    private boolean isCheckedNotification = false;

    private DefineUser defineUser;
    private GiverProfileViewModel viewModel;
    private GiverRepository giverRepository;
    private AdvertsRepository advertsRepository;
    private PermissionHandler permissionHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationCore app = (ApplicationCore) requireActivity().getApplication();
        giverRepository = app.getAppComponent().getGiverRepository();
        advertsRepository = app.getAppComponent().getAdvertsRepository();
        defineUser = app.getHelpersComponent().getDefineUser();

        PermissionComponent permissionComponent = DaggerPermissionComponent.builder().defineActivity(new ActivityModule((AppCompatActivity) requireActivity())).definePermissions(new PermissionHandlerModule()).build();
        permissionHandler = permissionComponent.getPermissionHandler();

        setHasOptionsMenu(true);


        this.user = defineUser.defineGiver();

        isCheckedLocation = defineUser.getPreferences("location");
        isCheckedNotification = defineUser.getPreferences("notificaiton");
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGiverProfileBinding.inflate(inflater);
        mBinding = new WeakReference<>(binding);

        AppCompatActivity appCompatActivity = (AppCompatActivity) requireActivity();
        appCompatActivity.setSupportActionBar(binding.giverProfileToolbar);

        binding.giverProfileName.setText(user.getLogin());
        binding.giverProfilePhone.setText(user.getPhone());
        binding.giverProfileLocation.setChecked(isCheckedLocation);
        binding.giverProfileOpenChat.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_giverProfileF_to_chatsListFragment));

        handlers();

        viewModel = new ViewModelProvider(requireActivity(),
                new GiverProfileFactory(requireActivity().getApplication(), giverRepository, advertsRepository))
                .get(GiverProfileViewModel.class);

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

        if (getActivity() instanceof GiverActivity) {
            ((GiverActivity) requireActivity()).setBottomNavigationVisibility(true);
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
        binding.giverProfileToolbar.setOnMenuItemClickListener(this::toolbarHandle);
        binding.giverProfileSave.setOnClickListener(View -> closeEdit());
        binding.giverProfileCancel.setOnClickListener(View -> removeEdit());

        binding.giverProfileSwiper.setOnRefreshListener(() -> viewModel.getHistoryAdverts(user.getX5_Id()).observe(requireActivity(), this::getHistoryList));
        binding.giverOpenVk.setOnClickListener(View -> vkLoad());

        binding.giverProfileLocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) permissionHandler.requestPermissions((AppCompatActivity) requireActivity());
            isCheckedLocation = isChecked && permissionHandler.checkPermissions((AppCompatActivity) requireActivity());
            binding.giverProfileLocation.setChecked(isCheckedLocation);
        });

        binding.giverProfileNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> isCheckedNotification = isChecked);
    }

    private void getHistoryList(List<String> result) {
        if (result != null) {
            binding.giverProfileCount.setText("Успешных передач продуктов: " + Integer.toString(result.size()));
            if (arrayAdapter == null) {
                arrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.item_needy_product_name, result);
                binding.giverProfileHistoryList.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
            } else {
                arrayAdapter.clear();
                arrayAdapter.addAll(result);
                arrayAdapter.notifyDataSetChanged();
            }
        }
        binding.giverProfileSwiper.setRefreshing(false);
    }

    private void editProfile() {
        binding.giverProfileHistoryList.setVisibility(View.GONE);
        binding.giverOpenVk.setVisibility(View.GONE);
        binding.giverProfileHistoryTitle.setVisibility(View.GONE);
        binding.giverProfileOpenChat.setVisibility(View.GONE);
        binding.giverProfileEdit.setVisibility(View.VISIBLE);
        binding.giverProfileSwiper.setEnabled(false);

        if (getActivity() instanceof GiverActivity) {
            ((GiverActivity) requireActivity()).setBottomNavigationVisibility(false);
        }
    }

    private void closeEdit() {
        String editPhoneString = binding.giverProfileEditPhone.getText().toString();
        String editLoginString = binding.giverProfileEditLogin.getText().toString();
        String editPasswordString = binding.giverProfileEditOldPassword.getText().toString();

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
        String newLogin = binding.giverProfileEditLogin.getText().toString();
        String newPhone = binding.giverProfileEditPhone.getText().toString();
        String newPassword = binding.giverProfileEditPassword.getText().toString();
        String oldPasswordText = binding.giverProfileEditOldPassword.getText().toString();

        viewModel.editProfile(new RequestNeedyEditProfile(user.getX5_Id(), newLogin, newPhone, newPassword, oldPasswordText)).observe(requireActivity(), giver -> {
            int code = viewModel.getStatusCode();
            if (code == 403) Snackbar.make(requireContext(), requireView(), getString(R.string.used_data), Snackbar.LENGTH_SHORT).show();
            else if (code > 299) Snackbar.make(requireContext(), requireView(), getString(R.string.uncorrect_password), Snackbar.LENGTH_SHORT).show();

            if (giver != null) {
                Snackbar.make(requireContext(), requireView(), getString(R.string.sucses), Snackbar.LENGTH_SHORT).show();
                binding.giverProfileName.setText(giver.getLogin());
                binding.giverProfilePhone.setText(giver.getPhone());

                defineUser.editProfileInfo(giver.getLogin(), giver.getPhone());
                Snackbar.make(requireContext(), requireView(), getString(R.string.sucses), Snackbar.LENGTH_SHORT).show();
            }
            enabledButton(true);
            removeEdit();
        });
    }

    private void enabledButton(boolean isEnable) {
        binding.giverProfileSave.setEnabled(isEnable);
        binding.giverProfileLocation.setEnabled(isEnable);
        binding.giverProfileNotifications.setEnabled(isEnable);
    }

    private void removeEdit() {
        binding.giverProfileEditLogin.setText("");
        binding.giverProfileEditPassword.setText("");
        binding.giverProfileEditPhone.setText("");
        binding.giverProfileEditOldPassword.setText("");

        binding.giverProfileHistoryList.setVisibility(View.VISIBLE);
        binding.giverOpenVk.setVisibility(View.VISIBLE);
        binding.giverProfileHistoryTitle.setVisibility(View.VISIBLE);
        binding.giverProfileOpenChat.setVisibility(View.VISIBLE);
        binding.giverProfileEdit.setVisibility(View.GONE);
        binding.giverProfileSwiper.setEnabled(true);

        if (getActivity() instanceof GiverActivity) {
            ((GiverActivity) requireActivity()).setBottomNavigationVisibility(true);
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