package com.buyhelp.nofoodsharingproject.presentation.viewmodels.getter;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.buyhelp.nofoodsharingproject.data.api.getter.GetterRepository;
import com.buyhelp.nofoodsharingproject.data.api.getter.dto.RequestGetterEditProfile;
import com.buyhelp.nofoodsharingproject.data.models.Getter;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetterProfileViewModel extends AndroidViewModel {
    private final MutableLiveData<Getter> _profile = new MutableLiveData<>();
    private int statusCode = 0;
    private final GetterRepository getterRepository;

    public GetterProfileViewModel(Application application, GetterRepository getterRepository) {
        super(application);
        this.getterRepository = getterRepository;
    }

    public LiveData<Getter> editProfile(DefineUser<Getter> defineUser, RequestGetterEditProfile requestGetterEditProfile) {
        statusCode = 0;
        getterRepository.editProfile(requestGetterEditProfile).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<Getter> call, @NotNull Response<Getter> response) {
                statusCode = response.code();
                if (response.isSuccessful() && response.body() != null) {
                    _profile.setValue(response.body());
                    defineUser.editProfileInfo(response.body().getLogin(), response.body().getPhone());
                } else _profile.setValue(null);
            }

            @Override
            public void onFailure(@NotNull Call<Getter> call, @NotNull Throwable t) {
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
