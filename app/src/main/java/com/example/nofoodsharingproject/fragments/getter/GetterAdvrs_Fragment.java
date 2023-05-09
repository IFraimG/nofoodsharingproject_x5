package com.example.nofoodsharingproject.fragments.getter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import android.os.CountDownTimer;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.activities.GetterNewAdvert_Activity;
import com.example.nofoodsharingproject.data.repository.AdvertsRepository;
import com.example.nofoodsharingproject.data.repository.MapRepository;
import com.example.nofoodsharingproject.databinding.FragmentGetterAdvrsBinding;
import com.example.nofoodsharingproject.databinding.FragmentSetterAdvrsBinding;
import com.example.nofoodsharingproject.fragments.MarketsMap_Fragment;
import com.example.nofoodsharingproject.models.Advertisement;
import com.example.nofoodsharingproject.utils.MarketTitleResponse;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetterAdvrs_Fragment extends Fragment {
    private FragmentGetterAdvrsBinding binding;

    private TextView addressShop;
    private TextView timeAdvert;
    private CountDownTimer timerView;
    private TextView numberAdvertisement;
    private Button buttonNewAdvertisement;
    private Button buttonZaborProducts;
    private Button buttonStopAdvert;
    private TextView textNewAdvert;
    private TextView titleAdvert;
    private LinearLayout statusAdvert;
    private ListView listViewProducts;
    private LinearLayout getterAdvertLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        binding = FragmentGetterAdvrsBinding.inflate(inflater);

        addressShop = binding.addressShop;
        numberAdvertisement = binding.numberOfAdvertisment;
        buttonNewAdvertisement = binding.createNewRequest;
        buttonZaborProducts = binding.pickUpOrder;
        buttonStopAdvert = binding.stopAdvert;
        textNewAdvert = binding.textNumberOfAdvert;
        timeAdvert = binding.timerToAdvert;
        titleAdvert = binding.getterAdvertTitleProducts;
        listViewProducts = binding.getterAdvertProducts;
        statusAdvert = binding.getterAdvertStatus;
        getterAdvertLayout = binding.getterAdvertLayout;

        buttonNewAdvertisement.setVisibility(View.VISIBLE);

        getAddress();
        getAdvertisement();
        timerInit();

        buttonNewAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GetterNewAdvert_Activity.class);
                startActivity(intent);

                timerView.start();
//                Для демонстрации
                buttonStopAdvert.setVisibility(View.VISIBLE);
                buttonNewAdvertisement.setVisibility(View.GONE);
                textNewAdvert.setVisibility(View.VISIBLE);
//                numberAdvertisement.setVisibility(View.VISIBLE);

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

        return binding.getRoot();
    }

    private void timerInit() {
        timerView = new CountDownTimer(3600000 * 2, 1000){
            @Override
            public void onTick(long l) {
                l /= 1000;
                int seconds = (int) l % 60;
                l /= 60;
                int minuts = (int) l % 60;
                l /= 60;
                int hours = (int) l % 60;

                String timer = hours + ":";

                if(minuts < 10){
                    timer += "0" + minuts + ":";
                }else{
                    timer += minuts + ":";
                }
                if(seconds < 10){
                    timer += "0" + seconds;
                }else{
                    timer+= seconds;
                }

                timeAdvert.setText(timer);

            }
            @Override
            public void onFinish() {
                timeAdvert.setText("Время вышло! Объявление отменится само");
            }
        };
    }

    private void getAdvertisement() {
        AdvertsRepository.getOwnAdvert(defineTypeUser().first).enqueue(new Callback<Advertisement>() {
            @Override
            public void onResponse(@NotNull Call<Advertisement> call, @NotNull Response<Advertisement> response) {
                if (response.code() == 400) Toast.makeText(getContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                if (response.code() == 404) getterAdvertLayout.setVisibility(View.INVISIBLE);
                if (response.code() == 200) {
                    titleAdvert.setText(response.body().getTitle());
                    statusAdvert.setVisibility(View.INVISIBLE);
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.item_getter_product_name, response.body().getListTitleProducts());
                    listViewProducts.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onFailure(Call<Advertisement> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAddress() {
        Pair<String, Boolean> userData = defineTypeUser();
        String userType = userData.second ? "getter" : "setter";

        MapRepository.getPinMarket(userType, userData.first).enqueue(new Callback<MarketTitleResponse>() {
            @Override
            public void onResponse(@NotNull Call<MarketTitleResponse> call, @NotNull Response<MarketTitleResponse> response) {
                if (response.code() != 404 && response.code() != 400) {
                    addressShop.setText(response.body().market);
                }
            }

            @Override
            public void onFailure(Call<MarketTitleResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private Pair<String, Boolean> defineTypeUser() {
        try {
            MasterKey masterKey = new MasterKey.Builder(getActivity().getApplicationContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(getActivity().getApplicationContext(), "user", masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

            String userID = sharedPreferences.getString("X5_id", "");
            boolean isUser = sharedPreferences.getBoolean("isGetter", false);
            return new Pair<>(userID, isUser);
        } catch (GeneralSecurityException | IOException err) {
            Toast.makeText(getContext(), "Непредвиденная ошибка!", Toast.LENGTH_SHORT).show();
            Log.e("esp_error", err.toString());
        }
        return null;
    }
}