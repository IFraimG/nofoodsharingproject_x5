package com.buyhelp.nofoodsharingproject.presentation.viewmodels.giver;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.buyhelp.nofoodsharingproject.data.api.auth.AuthRepository;
import com.buyhelp.nofoodsharingproject.data.api.auth.dto.SignUpResponseI;
import com.buyhelp.nofoodsharingproject.data.models.Giver;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GiverAuthViewModel extends AndroidViewModel {
    private final MutableLiveData<String> tokenFCM = new MutableLiveData<>();
    private MutableLiveData<Integer> code = new MutableLiveData<Integer>(0);
    private AuthRepository authRepository;
    private final MutableLiveData<SignUpResponseI<Giver>> createdUser = new MutableLiveData<>();

    public GiverAuthViewModel(@NonNull Application application, AuthRepository authRepository) {
        super(application);
        this.authRepository = authRepository;
    }

    private void pushData(SignUpResponseI<Giver> result) {
        DefineUser defineUser = new DefineUser(getApplication().getApplicationContext());
        defineUser.saveUserDataGiver(false, result.user.getX5_Id(), result);
    }

    public LiveData<SignUpResponseI<Giver>> login(String login, String password) {
        code.setValue(0);
        authRepository.giverLogin(login, password).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<SignUpResponseI<Giver>> call, @NotNull Response<SignUpResponseI<Giver>> response) {
                code.setValue(response.code());
                if (response.isSuccessful() && response.body() != null && response.body().token != null) {
                    createdUser.setValue(response.body());
                    pushData(response.body());
                } else createdUser.setValue(null);
            }

            @Override
            public void onFailure(@NotNull Call<SignUpResponseI<Giver>> call, @NotNull Throwable t) {
                t.printStackTrace();
                code.setValue(400);
                createdUser.setValue(null);
            }
        });

        return createdUser;
    }

    public LiveData<SignUpResponseI<Giver>> signup(String tokenFCM, String dtoPhone, String dtoLogin, String dtoPassword) {
        code.setValue(0);
        authRepository.giverRegistration(dtoPhone, dtoLogin, dtoPassword, tokenFCM).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<SignUpResponseI<Giver>> call, @NotNull Response<SignUpResponseI<Giver>> response) {
                code.setValue(response.code());
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().token != null) {
                        createdUser.setValue(response.body());
                        pushData(response.body());
                    }
                } else createdUser.setValue(null);
            }

            @Override
            public void onFailure(@NotNull Call<SignUpResponseI<Giver>> call, @NotNull Throwable t) {
                t.printStackTrace();
                code.setValue(400);
                createdUser.setValue(null);
            }
        });

        return createdUser;
    }

    public LiveData<Integer> getStatusCode() {
        return code;
    }

    public void clearStatusCode() {
        code.setValue(0);
    }
}
