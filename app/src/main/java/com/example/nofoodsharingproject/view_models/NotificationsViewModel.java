package com.example.nofoodsharingproject.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nofoodsharingproject.data.api.notifications.dto.ResponseNotificationsList;
import com.example.nofoodsharingproject.data.api.notifications.NotificationRepository;
import com.example.nofoodsharingproject.models.LoaderStatus;
import com.example.nofoodsharingproject.models.Notification;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsViewModel extends AndroidViewModel {
    public List<Notification> notifications = new ArrayList<>();
    private final MutableLiveData<List<Notification>> _notifications = new MutableLiveData<>();
    private final MutableLiveData<LoaderStatus> _status = new MutableLiveData<>();

//    public LiveData<LoaderStatus> status = _status;

    public NotificationsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Notification>> getAllNotifications(String userID, String typeOfUser) {
        _status.setValue(LoaderStatus.LOADING);
        NotificationRepository.getNotifications(getApplication().getApplicationContext(), userID, typeOfUser).enqueue(new Callback<ResponseNotificationsList>() {
            @Override
            public void onResponse(@NotNull Call<ResponseNotificationsList> call, @NotNull Response<ResponseNotificationsList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Notification> notificationsRes = Arrays.asList(response.body().getResult());
                    _notifications.setValue(notificationsRes);
                    _status.setValue(LoaderStatus.LOADED);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseNotificationsList> call, @NotNull Throwable t) {
                _status.setValue(LoaderStatus.FAILURE);
                t.printStackTrace();
            }
        });

        return _notifications;
    }

    public LiveData<LoaderStatus> getLoaderStatus() {
        return _status;
    }
}
