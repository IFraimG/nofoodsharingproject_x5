package com.example.nofoodsharingproject.view_models;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.example.nofoodsharingproject.data.api.adverts.dto.ResponseHistoryAdverts;
import com.example.nofoodsharingproject.models.Advertisement;
import com.example.nofoodsharingproject.models.LoaderStatus;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetterProfileViewModel extends AndroidViewModel {
    private final MutableLiveData<List<String>> _adverts = new MutableLiveData<>();
    private final MutableLiveData<LoaderStatus> _status = new MutableLiveData<>();
    private List<String> advertisementsHistory;


    public SetterProfileViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<String>> getHistoryAdverts(String userID) {
        AdvertsRepository advertsRepository = new AdvertsRepository();

        advertsRepository.findSetterAdvertisements(getApplication().getApplicationContext(), userID).enqueue(new Callback<ResponseHistoryAdverts>() {
            @Override
            public void onResponse(@NotNull Call<ResponseHistoryAdverts> call, @NotNull Response<ResponseHistoryAdverts> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Advertisement[] result = response.body().getAdvertisements();

                    advertisementsHistory = new ArrayList<>();
                    for (Advertisement advertisement : result) {
                        advertisementsHistory.add(advertisement.getTitle() + " - продуктов: " + advertisement.getListProducts().length + " - " + advertisement.getDateOfCreated());
                    }

                    _adverts.setValue(advertisementsHistory);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseHistoryAdverts> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });

        return _adverts;
    }

    public LiveData<LoaderStatus> getLoaderStatus() {
        return _status;
    }


}
