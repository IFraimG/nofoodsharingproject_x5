package com.example.nofoodsharingproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nofoodsharingproject.ApplicationCore;
import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.data.api.adverts.dto.RequestDoneAdvert;
import com.example.nofoodsharingproject.data.api.notifications.dto.ResponseFCMToken;
import com.example.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.example.nofoodsharingproject.data.api.getter.GetterRepository;
import com.example.nofoodsharingproject.data.api.notifications.NotificationRepository;
import com.example.nofoodsharingproject.databinding.ActivitySetterAdvertBinding;
import com.example.nofoodsharingproject.models.Advertisement;
import com.example.nofoodsharingproject.models.Notification;
import com.example.nofoodsharingproject.models.Setter;
import com.example.nofoodsharingproject.utils.DefineUser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import io.socket.client.Socket;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetterAdvertActivity extends AppCompatActivity {
    private ActivitySetterAdvertBinding binding;
    private Advertisement advertisement;
    private String fcmToken;
    private Socket socket;
    private ArrayAdapter<String> productsAdapter;
    private DefineUser<Setter> defineUser;
    private GetterRepository getterRepository;
    private NotificationRepository notificationRepository;
    private AdvertsRepository advertsRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetterAdvertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.defineUser = new DefineUser<>(this);
        advertsRepository = new AdvertsRepository();
        getterRepository = new GetterRepository();
        notificationRepository = new NotificationRepository();

        binding.setterAdvertBack.setOnClickListener(View -> finish());
        binding.setterAdvertAccept.setOnClickListener(View -> makeHelp());
        binding.setterAdvertCreateChat.setOnClickListener(View -> createChat());

        getAdvertisement(getIntent().getStringExtra("advertID"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (socket != null) socket.disconnect();
    }

    private void getAdvertisement(String advertID) {
        advertsRepository.getAdvertByID(getApplicationContext(), advertID).enqueue(new Callback<Advertisement>() {
            @Override
            public void onResponse(@NotNull Call<Advertisement> call, @NotNull Response<Advertisement> response) {
                if (!response.isSuccessful()) finish();
                else {
                    advertisement = response.body();
                    productsAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.item_getter_product_name, advertisement.getListProducts());
                    binding.setterAdvertProductItemListProducts.setAdapter(productsAdapter);
                    binding.setterAdvertAuthor.setText(advertisement.getAuthorName());
                    binding.setterAdvertTitle.setText(advertisement.getTitle());
                    binding.setterAdvertDate.setText(advertisement.getDateOfCreated());
                    if (advertisement.isDone()) binding.setterAdvertAccept.setEnabled(false);
                }
            }

            @Override
            public void onFailure(@NotNull Call<Advertisement> call, @NotNull Throwable t) {
                Toast.makeText(SetterAdvertActivity.this, R.string.smth_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void makeHelp() {
        binding.setterAdvertAccept.setEnabled(false);
        advertsRepository.makeDoneAdvert(getApplicationContext(), new RequestDoneAdvert(advertisement.getAuthorID(), defineUser.getTypeUser().first, Advertisement.generateID())).enqueue(new Callback<RequestDoneAdvert>() {
            @Override
            public void onResponse(@NotNull Call<RequestDoneAdvert> call, @NotNull Response<RequestDoneAdvert> response) {
                if (response.isSuccessful()) {
                    saveMessageForUser();
                    Intent intent = new Intent(SetterAdvertActivity.this, SetterHelpFinishActivity.class);
                    intent.putExtra("getterID", advertisement.getAuthorID());
                    startActivity(intent);
                    finish();
                } else {
                    binding.setterAdvertAccept.setEnabled(true);
                    Toast.makeText(getApplicationContext(), R.string.smth_not_good_try_again, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<RequestDoneAdvert> call, @NotNull Throwable t) {
                binding.setterAdvertAccept.setEnabled(true);
                t.printStackTrace();
            }
        });
    }

    private void getFCMTokenByUserID() {
        getterRepository.getFCMtoken(getApplicationContext(), advertisement.getAuthorID()).enqueue(new Callback<ResponseFCMToken>() {
            @Override
            public void onResponse(@NotNull Call<ResponseFCMToken> call, @NotNull Response<ResponseFCMToken> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fcmToken = response.body().getFcmToken();
                    sendNotification();
                } else Toast.makeText(SetterAdvertActivity.this, R.string.smth_wrong, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NotNull Call<ResponseFCMToken> call, @NotNull Throwable t) {
                Toast.makeText(SetterAdvertActivity.this, R.string.smth_wrong, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void sendNotification() {
            notificationRepository.requestNotifyDonateCall(fcmToken, getString(R.string.success_advert), getString(R.string.success_advert_body)).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(SetterAdvertActivity.this, R.string.sucses_notyfy_send, Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(SetterAdvertActivity.this, R.string.smth_problrs, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    Toast.makeText(SetterAdvertActivity.this, R.string.smth_problrs, Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
    }

    private void saveMessageForUser() {
        Notification notification = new Notification(getString(R.string.success_advert), getString(R.string.success_advert_body), advertisement.getAuthorID());
        notification.setFromUserID(defineUser.getTypeUser().first);
        notification.setListItems(advertisement.getListProducts());
        notification.setTypeOfUser("getter");
        notificationRepository.createNotification(getApplicationContext(), notification).enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(@NotNull Call<Notification> call, @NotNull Response<Notification> response) {
                if (!response.isSuccessful()) Toast.makeText(SetterAdvertActivity.this, R.string.smth_wrong, Toast.LENGTH_SHORT).show();
                else getFCMTokenByUserID();
            }

            @Override
            public void onFailure(@NotNull Call<Notification> call, @NotNull Throwable t) {
                Toast.makeText(SetterAdvertActivity.this, R.string.smth_wrong, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void createChat() {
        ApplicationCore app = (ApplicationCore) getApplication();
        socket = app.getSocket();
        socket.connect();
        try {
            JSONArray arr = new JSONArray(new String[]{defineUser.getTypeUser().first, advertisement.getAuthorID()});
            socket.emit("create_chat", arr);
        } catch (JSONException err) {
            Log.d("msg", err.getMessage());
        }

        socket.on("getCreatedChat", args -> {
            Intent intent = new Intent(SetterAdvertActivity.this, ChatsListActivity.class);
            startActivity(intent);
        });
    }
}
