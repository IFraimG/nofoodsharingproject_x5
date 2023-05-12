package com.example.nofoodsharingproject.fragments.getter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.activities.GetterNewAdvert_Activity;
import com.example.nofoodsharingproject.data.api.adverts.ResponseDeleteAdvert;
import com.example.nofoodsharingproject.data.repository.AdvertsRepository;
import com.example.nofoodsharingproject.data.repository.MapRepository;
import com.example.nofoodsharingproject.databinding.FragmentGetterAdvrsBinding;
import com.example.nofoodsharingproject.models.Advertisement;
import com.example.nofoodsharingproject.data.api.map.MarketTitleResponse;

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
    private Button buttonTakenProducts;
    private Button buttonStopAdvert;
    private TextView textNewAdvert;
    private TextView titleAdvert;
    private LinearLayout statusAdvert;
    private ListView listViewProducts;
    private LinearLayout getterAdvertLayout;

    private Advertisement advertisement;
    private ArrayAdapter<String> arrayAdapter;
    private String market;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        binding = FragmentGetterAdvrsBinding.inflate(inflater);

        addressShop = binding.addressShop;
        numberAdvertisement = binding.numberOfAdvertisement;
        buttonNewAdvertisement = binding.createNewRequest;
        buttonTakenProducts = binding.pickUpOrder;
        buttonStopAdvert = binding.stopAdvert;
        textNewAdvert = binding.textNumberOfAdvert;
        timeAdvert = binding.timerToAdvert;
        titleAdvert = binding.getterAdvertTitleProducts;
        listViewProducts = binding.getterAdvertProducts;
        statusAdvert = binding.getterAdvertStatus;
        getterAdvertLayout = binding.getterAdvertLayout;

        getAddress();
        getAdvertisement();
        timerInit();


        textNewAdvert.setVisibility(View.INVISIBLE);
        buttonStopAdvert.setVisibility(View.INVISIBLE);
        getterAdvertLayout.setVisibility(View.INVISIBLE);

        buttonNewAdvertisement.setOnClickListener(View -> startActivity(new Intent(getActivity(), GetterNewAdvert_Activity.class)));
        buttonStopAdvert.setOnClickListener(View -> removeAdvertisement());
        buttonTakenProducts.setOnClickListener(View -> takeProducts());

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
                timeAdvert.setText(R.string.time_is_up);
            }
        };
    }

    private void getAdvertisement() {
        AdvertsRepository.getOwnAdvert(defineTypeUser().first).enqueue(new Callback<Advertisement>() {
            @Override
            public void onResponse(@NotNull Call<Advertisement> call, @NotNull Response<Advertisement> response) {
                if (response.code() == 400) {
                    buttonNewAdvertisement.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
                }
                if (response.code() == 404) {
                    buttonNewAdvertisement.setVisibility(View.VISIBLE);
                }
                if (response.code() == 200) showAdvertisementElements(response.body());
            }

            @Override
            public void onFailure(Call<Advertisement> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeAdvertisement() {
        AdvertsRepository.deleteAdvert(advertisement.getAdvertsID()).enqueue(new Callback<ResponseDeleteAdvert>() {
            @Override
            public void onResponse(@NotNull Call<ResponseDeleteAdvert> call, @NotNull Response<ResponseDeleteAdvert> response) {
                if (response.code() != 400 && response.body().isDelete) {
                    Toast.makeText(getContext(), R.string.sucsesfully_deleted, Toast.LENGTH_SHORT).show();
                    hideAdvertisementElements();
                } else Toast.makeText(getContext(), R.string.error_on_delated, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseDeleteAdvert> call, Throwable t) {
                Toast.makeText(getContext(), R.string.error_on_delated, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void takeProducts() {
        AdvertsRepository.takingProducts(defineTypeUser().first).enqueue(new Callback<Advertisement>() {
            @Override
            public void onResponse(@NotNull Call<Advertisement> call, @NotNull Response<Advertisement> response) {
                if (response.code() == 404) Toast.makeText(getContext(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
                else if (response.code() == 201) {
                    Toast.makeText(getContext(), R.string.deal_correct, Toast.LENGTH_LONG).show();
                    hideAdvertisementElements();
                }
            }

            @Override
            public void onFailure(Call<Advertisement> call, Throwable t) {
                Toast.makeText(getContext(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
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
                    market = response.body().market;
                    addressShop.setText(response.body().market);
                    addressShop.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<MarketTitleResponse> call, Throwable t) {
                Toast.makeText(getContext(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void hideAdvertisementElements() {
        advertisement = null;
        statusAdvert.setVisibility(View.VISIBLE);
        buttonStopAdvert.setVisibility(View.INVISIBLE);
        arrayAdapter.notifyDataSetChanged();
        buttonTakenProducts.setVisibility(View.INVISIBLE);
        numberAdvertisement.setVisibility(View.INVISIBLE);
        textNewAdvert.setVisibility(View.INVISIBLE);
    }
    private void showAdvertisementElements(Advertisement advert) {
        titleAdvert.setText(advert.getTitle());
        statusAdvert.setVisibility(View.INVISIBLE);
        buttonStopAdvert.setVisibility(View.VISIBLE);
        arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.item_getter_product_name, advert.getListTitleProducts());
        listViewProducts.setAdapter(arrayAdapter);
        getterAdvertLayout.setVisibility(View.VISIBLE);
        advertisement = advert;
        if (advert.getGettingProductID() != null && advert.getGettingProductID().length() > 0) {
            buttonTakenProducts.setVisibility(View.VISIBLE);
            numberAdvertisement.setVisibility(View.VISIBLE);
            textNewAdvert.setVisibility(View.VISIBLE);
            numberAdvertisement.setText(advert.getGettingProductID());
        }
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
            Toast.makeText(getContext(), R.string.unvisinle_error, Toast.LENGTH_SHORT).show();
            Log.e("esp_error", err.toString());
        }
        return null;
    }
}