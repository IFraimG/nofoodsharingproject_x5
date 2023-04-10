package com.example.nofoodsharingproject.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.nofoodsharingproject.models.Advertisement;

import java.util.ArrayList;
import java.util.List;

public class AdvertisementListViewModel extends AndroidViewModel {

    public AdvertisementListViewModel(@NonNull Application application) {
        super(application);
    }

    public List<Advertisement> getAllAdverts() {
        return new ArrayList<>();
    }

    public void addAdvert(String advertID) {

    }

    public void removeAdvert(String advertID) {

    }
}
