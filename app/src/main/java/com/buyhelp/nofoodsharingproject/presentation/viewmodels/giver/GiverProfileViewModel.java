package com.buyhelp.nofoodsharingproject.presentation.viewmodels.giver;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.buyhelp.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.buyhelp.nofoodsharingproject.data.api.adverts.dto.ResponseHistoryAdverts;
import com.buyhelp.nofoodsharingproject.data.api.needy.dto.RequestNeedyEditProfile;
import com.buyhelp.nofoodsharingproject.data.api.giver.GiverRepository;
import com.buyhelp.nofoodsharingproject.data.models.Advertisement;
import com.buyhelp.nofoodsharingproject.data.models.Giver;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GiverProfileViewModel extends AndroidViewModel {
    private final MutableLiveData<List<String>> _adverts = new MutableLiveData<>();
    private final MutableLiveData<Giver> _profile = new MutableLiveData<>();
    private List<String> advertisementsHistory;
    private int statusCode = 0;
    private final GiverRepository giverRepository;
    private final AdvertsRepository advertsRepository;

    public GiverProfileViewModel(@NonNull Application application, GiverRepository giverRepository, AdvertsRepository advertsRepository) {
        super(application);
        this.giverRepository = giverRepository;
        this.advertsRepository = advertsRepository;
    }

    public LiveData<List<String>> getHistoryAdverts(String userID) {
        advertsRepository.findGiverAdvertisements(userID).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<ResponseHistoryAdverts> call, @NotNull Response<ResponseHistoryAdverts> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Advertisement[] result = response.body().getAdvertisements();

                    advertisementsHistory = new ArrayList<>();
                    for (Advertisement advertisement : result) {
                        advertisementsHistory.add(advertisement.getTitle() + " - продуктов: " + advertisement.getListProducts().length + " - " + advertisement.getDateOfCreated());
                    }

                    _adverts.setValue(advertisementsHistory);
                } else _adverts.setValue(new ArrayList<>());
            }

            @Override
            public void onFailure(@NotNull Call<ResponseHistoryAdverts> call, @NotNull Throwable t) {
                t.printStackTrace();
                _adverts.setValue(new ArrayList<>());
            }
        });

        return _adverts;
    }


    public LiveData<Giver> editProfile(RequestNeedyEditProfile requestNeedyEditProfile) {
        statusCode = 0;
        giverRepository.editProfile(requestNeedyEditProfile).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<Giver> call, @NotNull Response<Giver> response) {
                statusCode = response.code();
                if (response.isSuccessful() && response.body() != null) _profile.setValue(response.body());
                else _profile.setValue(null);
            }

            @Override
            public void onFailure(@NotNull Call<Giver> call, @NotNull Throwable t) {
                t.printStackTrace();
                _profile.setValue(null);
                statusCode = 400;
            }
        });

        return _profile;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
