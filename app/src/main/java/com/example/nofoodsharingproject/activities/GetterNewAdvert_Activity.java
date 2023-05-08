package com.example.nofoodsharingproject.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.data.repository.AdvertsRepository;
import com.example.nofoodsharingproject.databinding.ActivityGetterCreateNewAdvertismentBinding;
import com.example.nofoodsharingproject.models.GetterProductItem;
import com.example.nofoodsharingproject.models.Advertisement;
import com.example.nofoodsharingproject.models.Getter;
import com.example.nofoodsharingproject.models.Product;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetterNewAdvert_Activity extends AppCompatActivity {
    private final String[] productItems = new String[]{"Хлеб", "Картофель", "Мороженая рыба", "Сливочное масло",
            "Подсолнечное масло", "Яйца куриные", "Молоко", "Чай", "Кофе", "Соль", "Сахар",
            "Мука", "Лук", "Макаронные изделия", "Пшено", "Шлифованный рис", "Гречневая крупа",
            "Белокочанная капуста", "Морковь", "Яблоки", "Свинина", "Баранина", "Курица"};
    private final List<String> userProductItems = new ArrayList<String>();
    private ActivityGetterCreateNewAdvertismentBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGetterCreateNewAdvertismentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button button_ready = binding.readyToCreate;
        Button button_back = binding.buttonBack;
        Button button_re = binding.reAdvertisment;
        ListView listViewChoose = binding.productChoice;
        ListView listViewChoosenItems = binding.productChoosenItems;
        EditText titleAdvert = binding.getterAdvertInputTitle;

        ArrayAdapter<String> arrayAdapterChoose = new ArrayAdapter<String>(this, R.layout.item_getter_product_name, this.productItems);
        ArrayAdapter<String> arrayAdapterChoosenItems = new ArrayAdapter<String>(this, R.layout.item_getter_product_done_name, this.userProductItems);

        listViewChoose.setAdapter(arrayAdapterChoose);
        listViewChoosenItems.setAdapter(arrayAdapterChoosenItems);

        listViewChoose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String result = productItems[position];
                if (userProductItems.size() >= 5) {
                    Toast.makeText(GetterNewAdvert_Activity.this, "Вы не можете добавлять больше 5 продуктов", Toast.LENGTH_SHORT).show();
                } else if (!userProductItems.contains(result)) {
                    userProductItems.add(result);
                    arrayAdapterChoosenItems.notifyDataSetChanged();
//                    Snackbar.make(getCurrentFocus(), "Добавлено!", Snackbar.LENGTH_SHORT).show();
                    Toast.makeText(GetterNewAdvert_Activity.this, "Добавлено!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GetterNewAdvert_Activity.this, "Вы уже добавили этот продукт", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // пофиксить удаление
        listViewChoosenItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String result = productItems[position];
                if (userProductItems.contains(result)) {
                    for (int i = 0; i < userProductItems.size(); i++) {
                        if (userProductItems.get(i).equals(result)) {
                            userProductItems.remove(i);
                            arrayAdapterChoosenItems.notifyDataSetChanged();
                            break;
                        }
                    }
                    Toast.makeText(GetterNewAdvert_Activity.this, "Удалено!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        button_ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleAdvert.getText().toString().length() == 0) {
                    Toast.makeText(GetterNewAdvert_Activity.this, "Введите название!", Toast.LENGTH_SHORT).show();
                } else {
                    Getter result = getUserInfo();
                    Advertisement advertisement = new Advertisement(titleAdvert.getText().toString(), result.getX5_Id(), result.getLogin());
                    advertisement.setGettingProductID(Advertisement.generateID());
                    advertisement.setListProducts(userProductItems);

                    button_ready.setEnabled(false);
                    AdvertsRepository.createAdvert(advertisement).enqueue(new Callback<Advertisement>() {
                        @Override
                        public void onResponse(@NotNull Call<Advertisement> call, @NotNull Response<Advertisement> response) {
                            Advertisement result = response.body();
//                             потом обработать при коде 200!!!!!!!
//                            if (response.code() != 400) {
                                Toast.makeText(getApplicationContext(),
                                        R.string.advert_sucesfully_create, Toast.LENGTH_SHORT).show();
                                finish();
//                            } else {
//                                button_ready.setEnabled(true);
//                            }
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
