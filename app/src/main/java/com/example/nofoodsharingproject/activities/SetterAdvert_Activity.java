package com.example.nofoodsharingproject.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.data.api.adverts.dto.RequestDoneAdvert;
import com.example.nofoodsharingproject.data.api.notifications.dto.ResponseFCMToken;
import com.example.nofoodsharingproject.data.repository.AdvertsRepository;
import com.example.nofoodsharingproject.data.repository.GetterRepository;
import com.example.nofoodsharingproject.data.repository.NotificationRepository;
import com.example.nofoodsharingproject.databinding.ActivitySetterAdvertBinding;
import com.example.nofoodsharingproject.models.Advertisement;
import com.example.nofoodsharingproject.models.Notification;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.GeneralSecurityException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetterAdvert_Activity extends AppCompatActivity {
    private ActivitySetterAdvertBinding binding;
    private ListView listProducts;
    private ImageView backButton;
    private Button acceptBtn;
    private Advertisement advertisement;
    private TextView authorName;
    private TextView title;
    private TextView dateAdvert;
    private String fcmToken;

    private String successTitle = "На ваше объявление откликнулись!";
    private String successBody = "Заберите продукты как можно скорее.";

    private ArrayAdapter<String> productsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetterAdvertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        backButton = binding.setterAdvertBack;
        listProducts = binding.setterAdvertProductItemListProducts;
        acceptBtn = binding.setterAdvertAccept;
        authorName = binding.setterAdvertAuthor;
        title = binding.setterAdvertTitle;
        dateAdvert = binding.setterAdvertDate;

        backButton.setOnClickListener(View -> finish());
        acceptBtn.setOnClickListener(View -> makeHelp());

        getAdvertisement(getIntent().getStringExtra("advertID"));
    }

    private void getAdvertisement(String advertID) {

        AdvertsRepository.getAdvertByID(advertID).enqueue(new Callback<Advertisement>() {
            @Override
            public void onResponse(@NotNull Call<Advertisement> call, @NotNull Response<Advertisement> response) {
                if (response.code() == 404) finish();
                else {
                    advertisement = response.body();
                    productsAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.item_getter_product_name, advertisement.getListProducts());
                    listProducts.setAdapter(productsAdapter);
                    authorName.setText(advertisement.getAuthorName());
                    title.setText(advertisement.getTitle());
                    dateAdvert.setText(advertisement.getDateOfCreated());
                }
            }

            @Override
            public void onFailure(@NotNull Call<Advertisement> call, @NotNull Throwable t) {
                Toast.makeText(SetterAdvert_Activity.this, R.string.smth_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void makeHelp() {
        acceptBtn.setEnabled(false);
        AdvertsRepository.makeDoneAdvert(new RequestDoneAdvert(advertisement.getAuthorID(), getSetterID(), Advertisement.generateID())).enqueue(new Callback<RequestDoneAdvert>() {
            @Override
            public void onResponse(@NotNull Call<RequestDoneAdvert> call, @NotNull Response<RequestDoneAdvert> response) {
                if (response.code() == 400) {
                    acceptBtn.setEnabled(true);
                    Toast.makeText(getApplicationContext(), R.string.smth_not_good_try_again, Toast.LENGTH_SHORT).show();
                } else {
                    saveMessageForUser();
                    Intent intent = new Intent(SetterAdvert_Activity.this, SetterHelpFinish_Activity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(@NotNull Call<RequestDoneAdvert> call, @NotNull Throwable t) {
                acceptBtn.setEnabled(true);
                t.printStackTrace();
            }
        });
    }

    private void getFCMTokenByUserID() {
        GetterRepository.getFCMtoken(advertisement.getAuthorID()).enqueue(new Callback<ResponseFCMToken>() {
            @Override
            public void onResponse(@NotNull Call<ResponseFCMToken> call, @NotNull Response<ResponseFCMToken> response) {
                if (response.code() == 400) Toast.makeText(SetterAdvert_Activity.this, R.string.smth_wrong, Toast.LENGTH_SHORT).show();
                if (response.code() == 200) {
                    fcmToken = response.body().getFcmToken();
                    sendNotification();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseFCMToken> call, @NotNull Throwable t) {
                Toast.makeText(SetterAdvert_Activity.this, R.string.smth_wrong, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void sendNotification() {
            NotificationRepository.requestNotifyDonateCall(fcmToken, successTitle, successBody).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(SetterAdvert_Activity.this, R.string.sucses_notyfy_send, Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(SetterAdvert_Activity.this, R.string.smth_problrs, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(SetterAdvert_Activity.this, R.string.smth_problrs, Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
    }

    private void saveMessageForUser() {
        Notification notification = new Notification(successTitle, successBody, advertisement.getAuthorID());
        notification.setFromUserID(getSetterID());
        notification.setListItems(advertisement.getListProducts());
        notification.setTypeOfUser("getter");
        NotificationRepository.createNotification(notification).enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(@NotNull Call<Notification> call, @NotNull Response<Notification> response) {
                if (response.code() != 201) Toast.makeText(SetterAdvert_Activity.this, R.string.smth_wrong, Toast.LENGTH_SHORT).show();
                else getFCMTokenByUserID();
            }

            @Override
            public void onFailure(@NotNull Call<Notification> call, @NotNull Throwable t) {
                Toast.makeText(SetterAdvert_Activity.this, R.string.smth_wrong, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private String getSetterID() {
        try {
            MasterKey masterKey = new MasterKey.Builder(getApplicationContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(getApplicationContext(), "user", masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
            return sharedPreferences.getString("X5_id", "");
        } catch (IOException | GeneralSecurityException err) {
            Log.e(getString(R.string.unvisinle_error), err.toString());
            err.printStackTrace();
        }

        return null;
    }
}
