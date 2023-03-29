package com.example.nofoodsharingproject.fragments.setter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import com.example.nofoodsharingproject.R;

public class SetterProfileF extends Fragment {

    SwitchCompat switchLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public boolean checkLocationPermissions() {
        int firstPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        int secondPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int thirdPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION);

        return firstPermission == PackageManager.PERMISSION_GRANTED && secondPermission == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (checkLocationPermissions()) {
            switchLocation.setChecked(true);
        } else switchLocation.setChecked(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setter_profile, container, false);
        switchLocation = (SwitchCompat) view.findViewById(R.id.setter_profile_location);

        switchLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{
                                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                                    android.Manifest.permission.ACCESS_FINE_LOCATION,
//                                    android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                            }, 200);
                } else {
                    // тут в sharedpreferences добавится флаг о том, что геолокацию включать нельзя
                }
                switchLocation.setChecked(isChecked);
            }
        });

        return view;
    }
}