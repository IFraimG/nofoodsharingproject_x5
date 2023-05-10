package com.example.nofoodsharingproject.view_models;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.nofoodsharingproject.data.api.adverts.ResponseActiveAdverts;
import com.example.nofoodsharingproject.data.repository.AdvertsRepository;
import com.example.nofoodsharingproject.models.Advertisement;
import com.example.nofoodsharingproject.utils.LoaderStatus;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdvertisementList_ViewModel extends AndroidViewModel {
    public List<Advertisement> adverts = new ArrayList<>();
    private final MutableLiveData<List<Advertisement>> _adverts = new MutableLiveData<>();
    private final MutableLiveData<LoaderStatus> _status = new MutableLiveData<>();
    public LiveData<LoaderStatus> status = _status;

    public AdvertisementList_ViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Advertisement>> getAllAdverts() {
        _status.setValue(LoaderStatus.LOADING);
        AdvertsRepository.getListAdverts().enqueue(new Callback<ResponseActiveAdverts>() {
            @Override
            public void onResponse(@NotNull Call<ResponseActiveAdverts> call, @NotNull Response<ResponseActiveAdverts> response) {
                if (response.body() != null) {
                    _status.setValue(LoaderStatus.LOADED);
                    _adverts.setValue(Arrays.asList(response.body().getAdvertisements()));
                }
            }

            @Override
            public void onFailure(Call<ResponseActiveAdverts> call, Throwable t) {
                _status.setValue(LoaderStatus.FAILURE);
                t.printStackTrace();
            }
        });
        return _adverts;
    }
}