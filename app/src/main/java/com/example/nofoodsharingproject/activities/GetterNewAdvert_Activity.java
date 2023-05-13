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
import com.example.nofoodsharingproject.models.Advertisement;
import com.example.nofoodsharingproject.models.Getter;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
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
    private Button button_ready;
    private Button button_back;
    private ListView listViewChoose;
    private ListView listViewChoosenItems;
    private EditText titleAdvert;
    private ArrayAdapter<String> arrayAdapterChoose;
    private ArrayAdapter<String> arrayAdapterChoosenItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGetterCreateNewAdvertismentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        button_ready = binding.readyToCreate;
        button_back = binding.buttonBack;
        listViewChoose = binding.productChoice;
        listViewChoosenItems = binding.productChoosenItems;
        titleAdvert = binding.getterAdvertInputTitle;

        arrayAdapterChoose = new ArrayAdapter<String>(this, R.layout.item_getter_product_name, this.productItems);
        arrayAdapterChoosenItems = new ArrayAdapter<String>(this, R.layout.item_getter_product_done_name, this.userProductItems);

        listViewChoose.setAdapter(arrayAdapterChoose);
        listViewChoosenItems.setAdapter(arrayAdapterChoosenItems);

        listViewChoose.setOnItemClickListener((parent, view, position, id) -> chooseItem(position));

        listViewChoosenItems.setOnItemClickListener((parent, view, position, id) -> {
            userProductItems.remove(position);
            arrayAdapterChoosenItems.notifyDataSetChanged();

            Toast.makeText(GetterNewAdvert_Activity.this, R.string.deleted, Toast.LENGTH_SHORT).show();
        });

        button_back.setOnClickListener(View -> finish());
        button_ready.setOnClickListener(View -> pushData());
    }

    private void pushData() {
        if (userProductItems.size() == 0)
            Toast.makeText(GetterNewAdvert_Activity.this, R.string.add_to_list_product, Toast.LENGTH_SHORT).show();
        else if (titleAdvert.getText().toString().length() == 0) {
            Toast.makeText(GetterNewAdvert_Activity.this, R.string.edit_name, Toast.LENGTH_SHORT).show();
        } else {
            Getter result = getUserInfo();
            Advertisement advertisement = new Advertisement(titleAdvert.getText().toString(), result.getX5_Id(), result.getLogin());
            advertisement.setGettingProductID(Advertisement.generateID());
            if (userProductItems.size() > 0) advertisement.setListProductsCustom(userProductItems);

            button_ready.setEnabled(false);
            AdvertsRepository.createAdvert(advertisement).enqueue(new Callback<Advertisement>() {
                @Override
                public void onResponse(@NotNull Call<Advertisement> call, @NotNull Response<Advertisement> response) {
                    Advertisement result = response.body();
                    if (response.code() == 400) {
                        Toast.makeText(GetterNewAdvert_Activity.this, R.string.problems, Toast.LENGTH_SHORT).show();
                        button_ready.setEnabled(true);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                R.string.advert_sucesfully_create, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<Advertisement> call, @NotNull Throwable t) {
                    button_ready.setEnabled(true);
                    Toast.makeText(getApplicationContext(),
                            R.string.smth_not_good, Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        }
    }

    private void chooseItem(int position) {
        String result = productItems[position];
        if (userProductItems.size() > 3) {
            Toast.makeText(GetterNewAdvert_Activity.this, R.string.lot_of_product, Toast.LENGTH_SHORT).show();
        } else if (!userProductItems.contains(result)) {
            userProductItems.add(result);
            arrayAdapterChoosenItems.notifyDataSetChanged();
            Toast.makeText(GetterNewAdvert_Activity.this, R.string.added, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(GetterNewAdvert_Activity.this, R.string.this_product_added, Toast.LENGTH_SHORT).show();
        }
    }

    private Getter getUserInfo() {
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

        return new Getter();
    }
}
