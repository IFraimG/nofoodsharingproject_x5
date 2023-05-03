package com.example.nofoodsharingproject.fragments.getter;

import static androidx.fragment.app.FragmentManager.TAG;
import static java.lang.Math.floor;
import static java.lang.Math.random;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.nofoodsharingproject.MainActivity;
import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.activities.GetterNewAdvert;
import com.example.nofoodsharingproject.fragments.MarketsMapF;

import java.util.Random;
import java.util.TimerTask;

public class GetterAdvrsF extends Fragment {

    public static final String APP_PREFERENCES_NAME = "Nickname"; // имя кота
    public static final String APP_PREFERENCES_AGE = "Age"; // возраст кота
    private SharedPreferences sp;



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
        TextView numberAdvertisement = view.findViewById(R.id.number_of_advertisment);
        Button buttonNewAdvertisement = view.findViewById(R.id.create_new_request);
        Button buttonZaborProducts = view.findViewById(R.id.pick_up_order);
        Button buttonStopAdvert = view.findViewById(R.id.stop_advert);
        TextView textNewAdvert = view.findViewById(R.id.text_number_of_advert);
        TextView timeAdvert = view.findViewById(R.id.timer_to_advert);


        buttonNewAdvertisement.setVisibility(View.VISIBLE);



        CountDownTimer timerView = new CountDownTimer(3600000 * 2, 1000){
            @Override
            public void onTick(long l) {
                l /= 1000;
                int seconds = (int) l % 60;
                l /= 60;
                int minuts = (int) l % 60;
                l /= 60;
                int hours = (int) l % 60;

                timeAdvert.setText("" + hours + ":" + minuts + ":" + seconds);

            }
            @Override
            public void onFinish() {
                timeAdvert.setText("Время вышло! Объявление отменится само");
            }
        };
//        adresShop.setText();
//        timeAdvert.setText();




        // Замени все getFragmentManager на getSupportFragmentManager !!
        // Замени все getFragmentManager на getSupportFragmentManager !!
        // Замени все getFragmentManager на getSupportFragmentManager !!
        // Замени все getFragmentManager на getSupportFragmentManager !!
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
                Intent intent = new Intent(getActivity(), GetterNewAdvert.class);
                startActivity(intent);
                int x = 0;
                // Отправляем на сервер
                do{x = (int)(Math.random() * 10000);
                } while (x < 1000 || x > 9999);
                numberAdvertisement.setText(""+x);

                timerView.start();
//                Для демонстрации
                buttonStopAdvert.setVisibility(View.VISIBLE);
                buttonNewAdvertisement.setVisibility(View.GONE);
                textNewAdvert.setVisibility(View.VISIBLE);
                numberAdvertisement.setVisibility(View.VISIBLE);

            }
        });

        buttonStopAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //   Для демонстрации
                buttonStopAdvert.setVisibility(View.GONE);
                buttonZaborProducts.setVisibility(View.VISIBLE);
                textNewAdvert.setVisibility(View.GONE);
                numberAdvertisement.setVisibility(View.GONE);
                timerView.cancel();
            }
        });

        buttonZaborProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Для демонстрации
                buttonZaborProducts.setVisibility(View.GONE);
                buttonNewAdvertisement.setVisibility(View.VISIBLE);
            }
        });



        return view;
    }
}