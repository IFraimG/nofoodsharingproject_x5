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

public class GetterAuthF extends Fragment {

    public GetterAuthF() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_getter_auth, container, false);
        Button btn = (Button) view.findViewById(R.id.auth_getter_btn_login);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);

                // Костыль !
                intent.putExtra("isAuth", true);

                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }
}