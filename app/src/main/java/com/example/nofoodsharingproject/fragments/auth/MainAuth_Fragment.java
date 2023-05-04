package com.example.nofoodsharingproject.fragments.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nofoodsharingproject.R;


public class MainAuth_Fragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
//        Button btnGetter = getActivity().findViewById(R.id.main_auth_btn_getter);
//        Button btnSetter = getActivity().findViewById(R.id.main_auth_btn_setter);

//        btnGetter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Navigation.findNavController(v).navigate(R.id.action_mainAuthF_to_getterAuthF);
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_auth, container, false);

        Button btnGetter = (Button) view.findViewById(R.id.main_auth_btn_getter);
        Button btnSetter = (Button) view.findViewById(R.id.main_auth_btn_setter);

        btnSetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_mainAuthF_to_setterAuthF);
            }
        });

        btnGetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_mainAuthF_to_getterAuthF);
            }
        });

        return view;
    }


}