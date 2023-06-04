package com.buyhelp.nofoodsharingproject.presentation.viewmodels.needy;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.buyhelp.nofoodsharingproject.data.api.needy.NeedyRepository;
import com.buyhelp.nofoodsharingproject.data.api.needy.dto.RequestNeedyEditProfile;
import com.buyhelp.nofoodsharingproject.data.models.Needy;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NeedyProfileViewModel extends AndroidViewModel {
    private final MutableLiveData<Needy> _profile = new MutableLiveData<>();
    private int statusCode = 0;
    private final NeedyRepository needyRepository;

    public NeedyProfileViewModel(Application application, NeedyRepository needyRepository) {
        super(application);
        this.needyRepository = needyRepository;
    }

    public LiveData<Needy> editProfile(DefineUser defineUser, RequestNeedyEditProfile requestNeedyEditProfile) {
        statusCode = 0;
        needyRepository.editProfile(requestNeedyEditProfile).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<Needy> call, @NotNull Response<Needy> response) {
                statusCode = response.code();
                if (response.isSuccessful() && response.body() != null) {
                    _profile.setValue(response.body());
                    defineUser.editProfileInfo(response.body().getLogin(), response.body().getPhone());
                } else _profile.setValue(null);
            }

            @Override
            public void onFailure(@NotNull Call<Needy> call, @NotNull Throwable t) {
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
