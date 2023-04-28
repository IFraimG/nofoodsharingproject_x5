package com.example.nofoodsharingproject.fragments.getter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nofoodsharingproject.R;


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


        return inflater.inflate(R.layout.fragment_getter_profile, container, false);
    }
}