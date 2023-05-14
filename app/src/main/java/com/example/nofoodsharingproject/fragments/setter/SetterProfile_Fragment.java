package com.example.nofoodsharingproject.fragments.setter;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.activities.MainAuth_Activity;
import com.example.nofoodsharingproject.data.repository.AdvertsRepository;
import com.example.nofoodsharingproject.data.api.adverts.dto.ResponseHistoryAdverts;
import com.example.nofoodsharingproject.databinding.FragmentSetterProfileBinding;
import com.example.nofoodsharingproject.models.Advertisement;
import com.example.nofoodsharingproject.models.Setter;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SetterProfile_Fragment extends Fragment {
    private SharedPreferences settings;
    private FragmentSetterProfileBinding binding;
    private SwitchCompat switchLocation;
    private SwitchCompat switchNotification;
    private ListView historyList;

    private String[] advertisementsHistory;
    private TextView userName;
    private TextView successProducts;
    private Button openVk;
    private Button logoutBtn;
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayAdapter<String> arrayAdapter;
    private Setter user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        settings = requireActivity().getSharedPreferences("prms", Context.MODE_PRIVATE);
    }


    @Override
    public void onResume() {
        super.onResume();

        switchLocation.setChecked(checkLocationPermissions() && settings.getBoolean("location", false));
        switchNotification.setChecked(settings.getBoolean("notification", true));
    }

    private void setToPreferences(String key, boolean value) {
        SharedPreferences.Editor editor = settings.edit();

        editor.putBoolean(key, value);
        editor.apply();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetterProfileBinding.inflate(inflater);

        switchLocation = binding.setterProfileLocation;
        switchNotification = binding.setterProfileNotifications;
        historyList = binding.setterProfileHistoryList;
        userName = binding.setterProfileName;
        successProducts = binding.setterProfileCount;
        openVk = binding.setterOpenVk;
        toolbar = binding.setterProfileToolbar;
        swipeRefreshLayout = binding.setterProfileSwiper;

        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile_leave:
                        logout();
                        break;
                    case R.id.edit_settings:
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        this.user = defineUser();
        userName.setText(user.getLogin());

        getHistoryList();

        openVk.setOnClickListener(View -> vkLoad());
        switchLocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) requestPermissions();
                setToPreferences("location", checkLocationPermissions());
            } else setToPreferences("location", false);

            switchLocation.setChecked(isChecked);
        });

        switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> setToPreferences("notification", isChecked));

        swipeRefreshLayout.setOnRefreshListener(() -> {
            getHistoryList();
        });

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        requireActivity().getMenuInflater().inflate(R.menu.profile_left_panel_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
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

    private boolean checkLocationPermissions() {
        int firstPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        int secondPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);

        return firstPermission == PackageManager.PERMISSION_GRANTED && secondPermission == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void requestPermissions() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                }, 200);
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{ android.Manifest.permission.ACCESS_BACKGROUND_LOCATION }, 201);
        }
    }

    private Setter defineUser() {
        try {
            MasterKey masterKey = new MasterKey.Builder(requireActivity().getApplicationContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(requireActivity().getApplicationContext(), "user", masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            String login = sharedPreferences.getString("login", "");
            String phone = sharedPreferences.getString("phone", "");
            String userID = sharedPreferences.getString("X5_id", "");

            Setter user = new Setter();
            user.setLogin(login);
            user.setPhone(phone);
            user.setX5_Id(userID);

            return user;
        } catch (IOException | GeneralSecurityException err) {
            Log.e("auth error", err.toString());
            err.printStackTrace();
        }

        return new Setter();
    }

    private void logout() {
        try {
            MasterKey masterKey = new MasterKey.Builder(requireActivity().getApplicationContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(requireActivity().getApplicationContext(), "user", masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(requireActivity(), MainAuth_Activity.class);
            startActivity(intent);
            requireActivity().finish();
        } catch (IOException | GeneralSecurityException err) {
            err.printStackTrace();
        }
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