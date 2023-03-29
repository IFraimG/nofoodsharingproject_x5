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
import com.example.nofoodsharingproject.fragments.MarketsMapF;

public class GetterAdvrsF extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_getter_advrs, container, false);
        ImageButton buttonMapOpen = view.findViewById(R.id.open_map);
        TextView adresShop = view.findViewById(R.id.adress_shop);
        TextView activeAdvertisement = view.findViewById(R.id.on_advertisement);
        TextView product1 = view.findViewById(R.id.product1);
        TextView product2 = view.findViewById(R.id.product2);

        // ИЗ БД
        activeAdvertisement.setText("Сейчас это объявление не активно");
        adresShop.setText("Адрес недоступен");
        product1.setText("продукт не найден");
        product2.setText("продукт не найден");
        //

        buttonMapOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //не робит new MarketsMapF();
            }
        });


        return view;
    }
}