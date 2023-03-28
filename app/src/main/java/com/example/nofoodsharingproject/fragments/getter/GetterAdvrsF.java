package com.example.nofoodsharingproject.fragments.getter;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.nofoodsharingproject.MainActivity;
import com.example.nofoodsharingproject.R;

public class GetterAdvrsF extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_getter_advrs, container, false);
        ImageButton goToMap = getActivity().findViewById(R.id.open_map);
        TextView adresShop = getActivity().findViewById(R.id.adress_shop);
        TextView activeAdvertisement = getActivity().findViewById(R.id.on_advertisement);



        return view;
    }
}