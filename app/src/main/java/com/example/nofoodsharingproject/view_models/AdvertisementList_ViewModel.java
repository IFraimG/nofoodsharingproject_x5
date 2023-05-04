package com.example.nofoodsharingproject.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.nofoodsharingproject.data.repository.AdvertsRepository;
import com.example.nofoodsharingproject.models.Advertisement;
import com.example.nofoodsharingproject.models.ShortDataWithDate;
import com.example.nofoodsharingproject.utils.LoaderStatus;

import java.util.ArrayList;
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
        AdvertsRepository.getListAdverts().enqueue(new Callback<List<Advertisement>>() {
            @Override
            public void onResponse(Call<List<Advertisement>> call, Response<List<Advertisement>> response) {
                _status.setValue(LoaderStatus.LOADED);
                _adverts.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Advertisement>> call, Throwable t) {
                _status.setValue(LoaderStatus.FAILURE);
                t.printStackTrace();
            }
        });
        return _adverts;
    }

    public void addAdvert(Advertisement advertisement) {
        adverts.add(advertisement);
        _adverts.setValue(adverts);
        // запрос на сервер
    }

    // потом удалим, если будет не нужен + Transactions.switchMap
    public LiveData<List<ShortDataWithDate>> getAdvertsExpiresAt() {
        return Transformations.map(getAllAdverts(), new Function<List<Advertisement>, List<ShortDataWithDate>>() {
            @Override
            public List<ShortDataWithDate> apply(List<Advertisement> input) {
                List<ShortDataWithDate> result = new ArrayList<>();
                for (Advertisement ad: input) result.add(new ShortDataWithDate(ad.getDateOfCreated(), ad.authorID, ad.advertsID, ad.gettingProductID));

                return result;
            }
        });
    }



    public void removeAdvert(String advertID) {
        // запрос на сервер
    }
}
