package com.example.nofoodsharingproject.fragments.getter;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.activities.GetterNewAdvert;
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
        TextView numberAdvertisement = view.findViewById(R.id.number_advertisement);
        TextView numberAdvertisementVsp = view.findViewById(R.id.number_advertisement_vspomogalka);
        Button buttonNewAdvertisement = view.findViewById(R.id.create_new_request);


        // ИЗ БД и сервера
        activeAdvertisement.setText("Сейчас это объявление не активно");
        adresShop.setText("Адрес недоступен");
        product1.setText("продукт не найден");
        product2.setText("продукт не найден");

        //только если активен заказ
        numberAdvertisement.setText(null);
        numberAdvertisementVsp.setText(null);
        //


        // Замени все getFragmentManager на getSupportFragmentManager !!
        // Замени все getFragmentManager на getSupportFragmentManager !!
        // Замени все getFragmentManager на getSupportFragmentManager !!

        buttonMapOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment someFragment = new MarketsMapF();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_getter_fragment, someFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        buttonNewAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity newAct = new GetterNewAdvert();
            }
        });
        return view;
    }
}