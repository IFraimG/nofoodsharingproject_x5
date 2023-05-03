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
import android.widget.Button;
import android.widget.CompoundButton;
import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.activities.MainAuthAC;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SetterProfileF extends Fragment {
    SharedPreferences settings;
    SwitchCompat switchLocation;
    SwitchCompat switchNotification;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = getActivity().getSharedPreferences("prms", Context.MODE_PRIVATE);
    }

    public boolean checkLocationPermissions() {
        int firstPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        int secondPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);

        return firstPermission == PackageManager.PERMISSION_GRANTED && secondPermission == PackageManager.PERMISSION_GRANTED;
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

        switchLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{
                                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                            }, 200);
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{ android.Manifest.permission.ACCESS_BACKGROUND_LOCATION }, 201);
                        setToPreferences("location", checkLocationPermissions());
                    }

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

    public void logout() {
        try {
            MasterKey masterKey = new MasterKey.Builder(getActivity().getApplicationContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(getActivity().getApplicationContext(), "user", masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(getActivity(), MainAuthAC.class);
            startActivity(intent);
            getActivity().finish();
        } catch (IOException | GeneralSecurityException err) {
            err.printStackTrace();
        }
    }
}