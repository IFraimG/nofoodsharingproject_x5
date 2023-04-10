package com.example.nofoodsharingproject.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.nofoodsharingproject.models.Advertisement;
import com.example.nofoodsharingproject.models.ShortDataWithDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class AdvertisementListViewModel extends AndroidViewModel {
    List<Advertisement> adverts = new ArrayList<>();
    MutableLiveData<List<Advertisement>> all = new MutableLiveData<>();


    public AdvertisementListViewModel(@NonNull Application application) {
        super(application);

        // Запрос на сервер
        List<Advertisement> advertisementsTestArray = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            advertisementsTestArray.add(new Advertisement("ПОМОГИТЕ МНЕ !!!!", "Кабачки и две капусты требую", "айдиавтора", "Автор"));
        }
        all = new MutableLiveData<>(advertisementsTestArray);
    }

    public LiveData<List<Advertisement>> getAllAdverts() {
        return all;
    }

    public void addAdvert(Advertisement advertisement) {
        adverts.add(advertisement);
        all.setValue(adverts);
        // запрос на сервер
    }

    // потом удалим, если будет не нужен + Transactions.switchMap
    public LiveData<List<ShortDataWithDate>> getAdvertsExpiresAt() {
        return Transformations.map(getAllAdverts(), new Function<List<Advertisement>, List<ShortDataWithDate>>() {
            @Override
            public List<ShortDataWithDate> apply(List<Advertisement> input) {
                List<ShortDataWithDate> result = new ArrayList<>();
                for (Advertisement ad: input) result.add(new Advertisement(ad.getDateOfCreated(), ad.authorID, ad.adversID, ad.gettingProductID));

                return result;
            }
        });
    }



    public void removeAdvert(String advertID) {
        // запрос на сервер
    }
}
