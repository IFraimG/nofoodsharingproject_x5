package com.example.nofoodsharingproject.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import androidx.annotation.NonNull;
import com.example.nofoodsharingproject.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class CustomMessagingService extends FirebaseMessagingService {
    final String CHANNEL_ID = "HEADS_UP_NOTIFICATION";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }


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
