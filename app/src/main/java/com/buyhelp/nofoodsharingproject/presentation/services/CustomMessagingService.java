package com.buyhelp.nofoodsharingproject.presentation.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import androidx.annotation.NonNull;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.data.api.getter.GetterRepository;
import com.buyhelp.nofoodsharingproject.data.api.setter.SetterRepository;
import com.buyhelp.nofoodsharingproject.data.models.Getter;
import com.buyhelp.nofoodsharingproject.data.models.Setter;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.buyhelp.nofoodsharingproject.presentation.ApplicationCore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomMessagingService extends FirebaseMessagingService {
    final String CHANNEL_ID = "HEADS_UP_NOTIFICATION";
    private DefineUser defineUser;
    private GetterRepository getterRepository;
    private SetterRepository setterRepository;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        defineUser = new DefineUser<>(getApplicationContext());

        ApplicationCore app = (ApplicationCore) getApplication();
        getterRepository = app.getAppComponent().getGetterRepository();
        setterRepository = app.getAppComponent().getSetterRepository();

        if (defineUser.getToken() != null && defineUser.getToken().length() > 0 && defineUser.getTypeUser().second.equals(true)) {
            changeGetterToken(token);
        } else changeSetterToken(token);
    }

    private void changeSetterToken(String token) {
        Setter setter = defineUser.defineSetter();
        setterRepository.changeToken(setter.getX5_Id(), setter.getTokenFCM()).enqueue(new Callback<>() {
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

    private void changeGetterToken(String token) {
        Getter getter = defineUser.defineGetter();
        getterRepository.changeToken(getter.getX5_Id(), getter.getTokenFCM()).enqueue(new Callback<>() {
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


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String text = remoteMessage.getNotification().getBody();

            if (remoteMessage.getNotification() != null) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                NotificationChannel notificationChannel = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    notificationChannel = new NotificationChannel(CHANNEL_ID, "test_channel", NotificationManager.IMPORTANCE_HIGH);
                    notificationManager.createNotificationChannel(notificationChannel);

                    Notification.Builder notification = new Notification.Builder(this, CHANNEL_ID)
                            .setContentTitle(title)
                            .setContentText(text)
                            .setSmallIcon(R.drawable.notifications_active)
                            .setAutoCancel(true);
                    notificationManager.notify(0, notification.build());
                }
            }
        }
    }
}
