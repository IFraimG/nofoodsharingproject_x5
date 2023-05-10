package com.example.nofoodsharingproject.fragments.setter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.activities.MainAuth_Activity;
import com.example.nofoodsharingproject.data.repository.AdvertsRepository;
import com.example.nofoodsharingproject.data.api.adverts.ResponseHistoryAdverts;
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
    private SwitchCompat switchLocation;
    private SwitchCompat switchNotification;
    private ListView historyList;

    private String[] advertisementsHistory;
    private TextView userName;
    private TextView successProducts;
    private ArrayAdapter<String> arrayAdapter;

    private Setter user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = getActivity().getSharedPreferences("prms", Context.MODE_PRIVATE);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setter_profile, container, false);

        Button logoutBtn = (Button) view.findViewById(R.id.setter_profile_logout);
        logoutBtn.setOnClickListener(View -> logout());

        switchLocation = (SwitchCompat) view.findViewById(R.id.setter_profile_location);
        switchNotification = (SwitchCompat) view.findViewById(R.id.setter_profile_notifications);
        historyList = (ListView) view.findViewById(R.id.setter_profile_history_list);
        userName = (TextView) view.findViewById(R.id.setter_profile_name);
        successProducts = (TextView) view.findViewById(R.id.setter_profile_count);

        this.user = defineUser();
        userName.setText(user.getLogin());

        getHistoryList();

        switchLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    requestPermissions();
                    setToPreferences("location", checkLocationPermissions());
                } else setToPreferences("location", false);

                switchLocation.setChecked(isChecked);
            }
        });

        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setToPreferences("notification", isChecked);
            }
        });

        return view;
    }

    private void getHistoryList() {
        AdvertsRepository.findSetterAdvertisements(this.user.getX5_Id()).enqueue(new Callback<ResponseHistoryAdverts>() {
            @Override
            public void onResponse(@NotNull Call<ResponseHistoryAdverts> call, @NotNull Response<ResponseHistoryAdverts> response) {
                if (response.code() == 400) Toast.makeText(getContext(), "Что-то пошло не так!", Toast.LENGTH_SHORT).show();
                else if (response.code() != 404) {
                    Advertisement[] result = response.body().getAdvertisements();
                    successProducts.setText(Integer.toString(result.length) + " успешных передач продуктов");

                    advertisementsHistory = new String[result.length];
                    for (int i = 0; i < result.length; i++) {
                        advertisementsHistory[i] = result[i].getTitle() + " - " + result[i].getListProducts().length + " продукта - " + result[i].getDateOfCreated();
                    }

                    arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.item_getter_product_name, advertisementsHistory);
                    historyList.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onFailure(Call<ResponseHistoryAdverts> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean checkLocationPermissions() {
        int firstPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        int secondPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);

        return firstPermission == PackageManager.PERMISSION_GRANTED && secondPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                }, 200);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{ android.Manifest.permission.ACCESS_BACKGROUND_LOCATION }, 201);
        }
    }

    public Setter defineUser() {
        try {
            MasterKey masterKey = new MasterKey.Builder(getActivity().getApplicationContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(getActivity().getApplicationContext(), "user", masterKey,
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

        return null;
    }

    public void logout() {
        try {
            MasterKey masterKey = new MasterKey.Builder(getActivity().getApplicationContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(getActivity().getApplicationContext(), "user", masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(getActivity(), MainAuth_Activity.class);
            startActivity(intent);
            getActivity().finish();
        } catch (IOException | GeneralSecurityException err) {
            err.printStackTrace();
        }
    }
}