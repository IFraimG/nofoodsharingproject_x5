package com.example.nofoodsharingproject.view_models;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nofoodsharingproject.data.api.notifications.ResponseNotificationsList;
import com.example.nofoodsharingproject.data.repository.NotificationRepository;
import com.example.nofoodsharingproject.models.Notification;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Notifications_ViewModel extends AndroidViewModel {
    public List<Notification> notifications = new ArrayList<>();
    private final MutableLiveData<List<Notification>> _notifications = new MutableLiveData<>();

//    private final MutableLiveData<LoaderStatus> _status = new MutableLiveData<>();
//    public LiveData<LoaderStatus> status = _status;

    public Notifications_ViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Notification>> getAllNotifications(String userID, String typeOfUser) {
        NotificationRepository.getNotifications(userID, typeOfUser).enqueue(new Callback<ResponseNotificationsList>() {
            @Override
            public void onResponse(@NotNull Call<ResponseNotificationsList> call, @NotNull Response<ResponseNotificationsList> response) {
                if (response.code() != 404 && response.code() != 400) {
                    List<Notification> notificationsRes = Arrays.asList(response.body().getResult());
                    _notifications.setValue(notificationsRes);
                }
            }

            @Override
            public void onFailure(Call<ResponseNotificationsList> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return _notifications;
    }
}
