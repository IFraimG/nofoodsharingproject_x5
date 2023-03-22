package com.example.nofoodsharingproject.fragments.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nofoodsharingproject.MainActivity;
import com.example.nofoodsharingproject.R;

public class SetterLoginAuthF extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setter_login_auth, container, false);

        Button btn = (Button) view.findViewById(R.id.login_auth_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // перед этим очень много запросов и проверок на валидацию, не думай, что тут так все просто !!!!
                Intent intent = new Intent(getContext(), MainActivity.class);

                // НА ДАННЫЙ МОМЕНТ - КОСТЫЛЬ!
                intent.putExtra("isAuth", true);

                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }
}