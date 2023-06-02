package com.buyhelp.nofoodsharingproject.presentation.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.buyhelp.nofoodsharingproject.data.api.notifications.NotificationRepository;
import com.buyhelp.nofoodsharingproject.data.api.notifications.dto.ResponseNotificationsList;
import com.buyhelp.nofoodsharingproject.data.models.LoaderStatus;
import com.buyhelp.nofoodsharingproject.data.models.Notification;

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
    private final NotificationRepository notificationRepository;


    public NotificationsViewModel(@NonNull Application application, NotificationRepository notificationRepository) {
        super(application);
        this.notificationRepository = notificationRepository;
    }

    public LiveData<List<Notification>> getAllNotifications(String userID, String typeOfUser) {
        _status.setValue(LoaderStatus.LOADING);
        notificationRepository.getNotifications(userID, typeOfUser).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseNotificationsList> call, @NonNull Response<ResponseNotificationsList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Notification> notificationsRes = Arrays.asList(response.body().getResult());
                    _notifications.setValue(notificationsRes);
                    _status.setValue(LoaderStatus.LOADED);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseNotificationsList> call, @NonNull Throwable t) {
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
