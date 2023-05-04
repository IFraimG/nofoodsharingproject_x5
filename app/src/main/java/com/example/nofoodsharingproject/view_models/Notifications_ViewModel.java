package com.example.nofoodsharingproject.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nofoodsharingproject.models.Notification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Notifications_ViewModel extends AndroidViewModel {
    public List<Notification> notifications = new ArrayList<>();
    private final MutableLiveData<List<Notification>> _notifications = new MutableLiveData<>();

//    private final MutableLiveData<LoaderStatus> _status = new MutableLiveData<>();
//    public LiveData<LoaderStatus> status = _status;


    public Notifications_ViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Notification>> getAllNotifications() {
        for (int i = 0; i < 5; i++) {
            notifications.add(new Notification("Краболов нуждается в помощи!", "Описание необходимой помощи", "link", new Date()));
        }
        _notifications.setValue(notifications);
        return _notifications;
    }
}
