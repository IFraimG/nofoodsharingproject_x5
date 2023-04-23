package com.example.nofoodsharingproject.fragments.getter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nofoodsharingproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GetterCreateNewAdvertisment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_getter_create_new_advertisment, container, false);
        Button buttonReadyCreate = view.findViewById(R.id.ready_to_create);
        RecyclerView recyclerView =view.findViewById(R.id.recycler);

        return view;
    }
}