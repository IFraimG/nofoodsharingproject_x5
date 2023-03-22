package com.example.nofoodsharingproject.fragments.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nofoodsharingproject.MainActivity;
import com.example.nofoodsharingproject.R;

public class SetterAuthF extends Fragment {

    public SetterAuthF() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setter_auth, container, false);

        Button btnRegistration = (Button) view.findViewById(R.id.setter_auth_btn_reg);
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_setterAuthF_to_setterLoginAuthF);
            }
        });

        Button btnLogin = (Button) view.findViewById(R.id.setter_auth_btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);

                // костыль !
                intent.putExtra("isAuth", true);

                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }
}