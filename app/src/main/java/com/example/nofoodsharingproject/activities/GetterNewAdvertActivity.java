package com.example.nofoodsharingproject.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.example.nofoodsharingproject.databinding.ActivityGetterCreateNewAdvertismentBinding;
import com.example.nofoodsharingproject.models.Advertisement;
import com.example.nofoodsharingproject.models.Getter;
import com.example.nofoodsharingproject.services.AdvertisementExpires;
import com.example.nofoodsharingproject.utils.DefineUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetterNewAdvertActivity extends AppCompatActivity {
    private final String[] productItems = new String[]{"Хлеб", "Картофель", "Мороженая рыба", "Сливочное масло",
            "Подсолнечное масло", "Яйца куриные", "Молоко", "Чай", "Кофе", "Соль", "Сахар",
            "Мука", "Лук", "Макаронные изделия", "Пшено", "Шлифованный рис", "Гречневая крупа",
            "Белокочанная капуста", "Морковь", "Яблоки", "Свинина", "Баранина", "Курица"};
    private final List<String> userProductItems = new ArrayList<String>();
    private ActivityGetterCreateNewAdvertismentBinding binding;
    private ArrayAdapter<String> arrayAdapterChoose;
    private ArrayAdapter<String> arrayAdapterChoosenItems;
    private DefineUser<Getter> defineUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGetterCreateNewAdvertismentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        defineUser = new DefineUser<>(this);

        arrayAdapterChoose = new ArrayAdapter<>(this, R.layout.item_getter_product_name, this.productItems);
        arrayAdapterChoosenItems = new ArrayAdapter<>(this, R.layout.item_getter_product_done_name, this.userProductItems);

        binding.productChoice.setAdapter(arrayAdapterChoose);
        binding.productChoosenItems.setAdapter(arrayAdapterChoosenItems);

        binding.productChoice.setOnItemClickListener((parent, view, position, id) -> chooseItem(position));

        binding.productChoosenItems.setOnItemClickListener((parent, view, position, id) -> {
            userProductItems.remove(position);
            arrayAdapterChoosenItems.notifyDataSetChanged();

            Toast.makeText(GetterNewAdvertActivity.this, R.string.deleted, Toast.LENGTH_SHORT).show();
        });

        binding.buttonBack.setOnClickListener(View -> finish());
        binding.readyToCreate.setOnClickListener(View -> pushData());
    }

    private void pushData() {
        if (userProductItems.size() == 0)
            Toast.makeText(GetterNewAdvertActivity.this, R.string.add_to_list_product, Toast.LENGTH_SHORT).show();
        else if (binding.getterAdvertInputTitle.getText().toString().length() == 0) {
            Toast.makeText(GetterNewAdvertActivity.this, R.string.edit_name, Toast.LENGTH_SHORT).show();
        } else if (userProductItems.size() > 3) {
            Toast.makeText(GetterNewAdvertActivity.this, R.string.many_products, Toast.LENGTH_SHORT).show();
        } else {
            Getter result = defineUser.defineGetter();
            Advertisement advertisement = new Advertisement(binding.getterAdvertInputTitle.getText().toString(), result.getX5_Id(), result.getLogin());
            advertisement.setGettingProductID(Advertisement.generateID());
            if (userProductItems.size() > 0) advertisement.setListProductsCustom(userProductItems);

            binding.readyToCreate.setEnabled(false);
            AdvertsRepository.createAdvert(getApplicationContext(), advertisement).enqueue(new Callback<Advertisement>() {
                @Override
                public void onResponse(@NotNull Call<Advertisement> call, @NotNull Response<Advertisement> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(GetterNewAdvertActivity.this, R.string.problems, Toast.LENGTH_SHORT).show();
                        binding.readyToCreate.setEnabled(true);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                R.string.advert_sucesfully_create, Toast.LENGTH_SHORT).show();
                        setAlarm();
                        finish();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<Advertisement> call, @NotNull Throwable t) {
                    binding.readyToCreate.setEnabled(true);
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
            Toast.makeText(GetterNewAdvertActivity.this, R.string.lot_of_product, Toast.LENGTH_SHORT).show();
        } else if (!userProductItems.contains(result)) {
            userProductItems.add(result);
            arrayAdapterChoosenItems.notifyDataSetChanged();
            Toast.makeText(GetterNewAdvertActivity.this, R.string.added, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(GetterNewAdvertActivity.this, R.string.this_product_added, Toast.LENGTH_SHORT).show();
        }
    }

    private void setAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AdvertisementExpires.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        long triggerAtMillis = System.currentTimeMillis() + (2 * 60 * 60 * 1000);

        if (alarmManager != null) alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
    }
}
