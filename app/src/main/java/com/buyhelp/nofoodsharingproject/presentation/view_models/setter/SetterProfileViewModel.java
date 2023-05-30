package com.buyhelp.nofoodsharingproject.presentation.view_models.setter;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.buyhelp.nofoodsharingproject.data.api.adverts.dto.ResponseHistoryAdverts;
import com.buyhelp.nofoodsharingproject.data.api.getter.dto.RequestGetterEditProfile;
import com.buyhelp.nofoodsharingproject.data.api.setter.SetterRepository;
import com.buyhelp.nofoodsharingproject.data.models.Advertisement;
import com.buyhelp.nofoodsharingproject.data.models.Setter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetterProfileViewModel extends AndroidViewModel {
    private final MutableLiveData<List<String>> _adverts = new MutableLiveData<>();
    private final MutableLiveData<Setter> _profile = new MutableLiveData<>();
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


    public LiveData<Setter> editProfile(RequestGetterEditProfile requestGetterEditProfile) {
        SetterRepository setterRepository = new SetterRepository();
        setterRepository.editProfile(getApplication(), requestGetterEditProfile).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<Setter> call, @NotNull Response<Setter> response) {
                if (response.code() == 400) {
                    _profile.setValue(null);
                    Toast.makeText(getApplication(), R.string.your_password_uncorrect, Toast.LENGTH_SHORT).show();
                }
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getApplication(), R.string.sucses, Toast.LENGTH_SHORT).show();
                    _profile.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NotNull Call<Setter> call, @NotNull Throwable t) {
                t.printStackTrace();
                _profile.setValue(null);
                Toast.makeText(getApplication(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
            }
        });

        return _profile;
    }
}
