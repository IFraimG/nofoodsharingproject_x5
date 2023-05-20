package com.example.nofoodsharingproject.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.data.api.getter.GetterRepository;
import com.example.nofoodsharingproject.data.api.setter.SetterRepository;
import com.example.nofoodsharingproject.models.Getter;
import com.example.nofoodsharingproject.models.Setter;
import com.example.nofoodsharingproject.utils.DefineUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomMessagingService extends FirebaseMessagingService {
    final String CHANNEL_ID = "HEADS_UP_NOTIFICATION";
    private DefineUser defineUser;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        defineUser = new DefineUser<>(getApplicationContext());
        if (defineUser.getTypeUser().second.equals(true)) {
            Getter getter = defineUser.defineGetter();
            GetterRepository.changeToken(getter.getX5_Id(), getter.getTokenFCM()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) defineUser.changeFCMtoken(token);
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    t.printStackTrace();
                }
            });

        } else {
            Setter setter = defineUser.defineSetter();
            SetterRepository.changeToken(setter.getX5_Id(), setter.getTokenFCM()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) defineUser.changeFCMtoken(token);
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String text = remoteMessage.getNotification().getBody();

        if (remoteMessage.getNotification() != null) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "test_channel", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);

            Notification.Builder notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setAutoCancel(true);

            notificationManager.notify(0, notification.build());
        }
    }
}
