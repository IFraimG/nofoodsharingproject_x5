package com.example.nofoodsharingproject.fragments.getter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.activities.MainAuthAC;

import java.io.IOException;
import java.security.GeneralSecurityException;


public class GetterProfileF extends Fragment {


    private SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_getter_profile, container, false);


        // изменение данных пользователя - сделать позже
        sp = requireActivity().getSharedPreferences("User_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Location_shop_getter_sp", "dsjdnjsd");
        editor.apply();

        Button button = view.findViewById(R.id.create_new_request);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

            }
        });


        Button buttonLogout = (Button) view.findViewById(R.id.getter_profile_logout);
        buttonLogout.setOnClickListener(View -> logout());

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
        } catch (IOException | GeneralSecurityException err) {
            err.printStackTrace();
        }
        Intent intent = new Intent(getActivity().getApplicationContext(), MainAuthAC.class);
        startActivity(intent);
        getActivity().finish();
    }
}