package com.example.nofoodsharingproject.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.adapters.GetterProductListAdapter;
import com.example.nofoodsharingproject.data.repository.AdvertsRepository;
import com.example.nofoodsharingproject.databinding.ActivityGetterCreateNewAdvertismentBinding;
import com.example.nofoodsharingproject.databinding.ActivityMainBinding;
import com.example.nofoodsharingproject.models.GetterProductItem;
import com.example.nofoodsharingproject.models.Advertisement;
import com.example.nofoodsharingproject.models.Getter;
import com.example.nofoodsharingproject.models.Product;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetterNewAdvert_Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Product> productItems = new ArrayList<>();
    private ActivityGetterCreateNewAdvertismentBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGetterCreateNewAdvertismentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initalazeData();
        Button button_ready = findViewById(R.id.ready_to_create);
        Button button_back = findViewById(R.id.button_back);
        Button button_re = findViewById(R.id.re_advertisment);
        RecyclerView recyclerView = findViewById(R.id.product_choice);
        GetterProductListAdapter productListAdapter = new GetterProductListAdapter(this, productItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productListAdapter);

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        button_ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ОТПРАВИТЬ ДАНЫЕ НА СЕРВЕР
                Getter result = getUserInfo();
                Advertisement advertisement = new Advertisement("test title", "test desc", result.getX5_Id(), result.getLogin());
                advertisement.setGettingProductID("testid");

                // костыль
                advertisement.setListProducts(productItems);

                button_ready.setEnabled(false);
                AdvertsRepository.createAdvert(advertisement).enqueue(new Callback<Advertisement>() {
                    @Override
                    public void onResponse(@NotNull Call<Advertisement> call, @NotNull Response<Advertisement> response) {
                        Advertisement result = response.body();
                        if (response.code() != 400) {
                            Toast.makeText(getApplicationContext(),
                                    R.string.advert_sucesfully_create, Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            button_ready.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<Advertisement> call, Throwable t) {
                        button_ready.setEnabled(true);
                        Toast.makeText(getApplicationContext(),
                                R.string.smth_not_good, Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });
            }
        });



        button_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ОТПРАВИТЬ ДАНЫЕ НА СЕРВЕР
                Toast.makeText(getApplicationContext(),
                        R.string.advert_sucesfully_create, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void initalazeData() {
        String[] array = new String[]{"Хлеб", "Картофель", "Мороженая рыба", "Сливочное масло",
                "Подсолнечное масло", "Яйца куриные", "Молоко", "Чай", "Кофе", "Соль", "Сахар",
                "Мука", "Лук", "Макаронные изделия", "Пшено", "Шлифованный рис", "Гречневая крупа",
                "Белокочанная капуста", "Морковь", "Яблоки", "Свинина", "Баранина", "Курица"};

        for (int i = 0; i < 23; i++) {
            productItems.add(new GetterProductItem(array[i]));
        }
    }


    public Getter getUserInfo() {
        try {
            MasterKey masterKey = new MasterKey.Builder(getApplicationContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(getApplicationContext(), "user", masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            String login = sharedPreferences.getString("login", "");
            String authorID = sharedPreferences.getString("X5_id", "");

            Getter userRequestData = new Getter();
            userRequestData.setLogin(login);
            userRequestData.setX5_Id(authorID);

            return userRequestData;
        } catch (IOException | GeneralSecurityException err) {
            Log.e("getting info error", err.toString());
            err.printStackTrace();
        }

        return null;
    }
}
