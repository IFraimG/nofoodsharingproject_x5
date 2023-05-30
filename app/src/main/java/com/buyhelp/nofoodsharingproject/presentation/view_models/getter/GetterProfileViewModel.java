package com.buyhelp.nofoodsharingproject.presentation.view_models.getter;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.buyhelp.nofoodsharingproject.R;
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

    public GetterProfileViewModel(Application application) {
        super(application);
    }

    public LiveData<Getter> editProfile(DefineUser<Getter> defineUser, RequestGetterEditProfile requestGetterEditProfile) {
        GetterRepository getterRepository = new GetterRepository();
        getterRepository.editProfile(getApplication(), requestGetterEditProfile).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<Getter> call, @NotNull Response<Getter> response) {
                if (response.code() == 400) {
                    Toast.makeText(getApplication(), R.string.your_password_uncorrect, Toast.LENGTH_SHORT).show();
                    _profile.setValue(null);
                }
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getApplication(), R.string.sucses, Toast.LENGTH_SHORT).show();
                    _profile.setValue(response.body());
                    defineUser.editProfileInfo(response.body().getLogin(), response.body().getPhone());
                } else Toast.makeText(getApplication(), getApplication().getString(R.string.unvisinle_error), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NotNull Call<Getter> call, @NotNull Throwable t) {
                t.printStackTrace();
                _profile.setValue(null);
                Toast.makeText(getApplication(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
            }
        });

        return _profile;
    }
}
