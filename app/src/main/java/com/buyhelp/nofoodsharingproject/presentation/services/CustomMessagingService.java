/**
 * Класс {@code CustomMessagingService} - сервис отправки удаленных сообщений пользователям
 * @author Кулагин Александр
 */


package com.buyhelp.nofoodsharingproject.presentation.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import androidx.annotation.NonNull;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.data.api.giver.GiverRepository;
import com.buyhelp.nofoodsharingproject.data.api.needy.NeedyRepository;
import com.buyhelp.nofoodsharingproject.data.models.Needy;
import com.buyhelp.nofoodsharingproject.data.models.Giver;
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
    private NeedyRepository needyRepository;
    private GiverRepository giverRepository;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        defineUser = new DefineUser(getApplicationContext());

        ApplicationCore app = (ApplicationCore) getApplication();
        needyRepository = app.getAppComponent().getNeedyRepository();
        giverRepository = app.getAppComponent().getGiverRepository();

        if (defineUser.getToken() != null && defineUser.getToken().length() > 0 && defineUser.getTypeUser().second.equals(true)) {
            changeNeedyToken(token);
        } else changeGiverToken(token);
    }

    private void changeGiverToken(String token) {
        Giver giver = defineUser.defineGiver();
        giverRepository.changeToken(giver.getX5_Id(), giver.getTokenFCM()).enqueue(new Callback<>() {
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

    private void changeNeedyToken(String token) {
        Needy needy = defineUser.defineNeedy();
        needyRepository.changeToken(needy.getX5_Id(), needy.getTokenFCM()).enqueue(new Callback<>() {
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
