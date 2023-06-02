package com.buyhelp.nofoodsharingproject.presentation.viewmodels.setter;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.buyhelp.nofoodsharingproject.data.api.auth.AuthRepository;
import com.buyhelp.nofoodsharingproject.data.api.auth.dto.SignUpResponseI;
import com.buyhelp.nofoodsharingproject.data.models.Getter;
import com.buyhelp.nofoodsharingproject.data.models.Setter;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetterAuthViewModel extends AndroidViewModel {
    private final MutableLiveData<String> tokenFCM = new MutableLiveData<>();
    private int code = 0;
    private AuthRepository authRepository;
    private final MutableLiveData<SignUpResponseI<Setter>> createdUser = new MutableLiveData<>();

    public SetterAuthViewModel(@NonNull Application application, AuthRepository authRepository) {
        super(application);
        this.authRepository = authRepository;
    }

    private void pushData(SignUpResponseI<Setter> result) {
        DefineUser defineUser = new DefineUser(getApplication().getApplicationContext());
        defineUser.saveUserDataSetter(false, result.user.getX5_Id(), result);
    }

    public LiveData<SignUpResponseI<Setter>> login(String login, String password) {
        code = 0;
        authRepository.setterLogin(login, password).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<SignUpResponseI<Setter>> call, @NotNull Response<SignUpResponseI<Setter>> response) {
                code = response.code();
                if (response.isSuccessful() && response.body() != null && response.body().token != null) {
                    createdUser.setValue(response.body());
                    pushData(response.body());
                } else createdUser.setValue(null);
            }

            @Override
            public void onFailure(@NotNull Call<SignUpResponseI<Setter>> call, @NotNull Throwable t) {
                t.printStackTrace();
                code = 400;
                createdUser.setValue(null);
            }
        });

        return createdUser;
    }

    public LiveData<SignUpResponseI<Setter>> signup(String tokenFCM, String dtoPhone, String dtoLogin, String dtoPassword) {
        code = 0;
        authRepository.setterRegistration(dtoPhone, dtoLogin, dtoPassword, tokenFCM).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<SignUpResponseI<Setter>> call, @NotNull Response<SignUpResponseI<Setter>> response) {
                code = response.code();
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().token != null) {
                        createdUser.setValue(response.body());
                        pushData(response.body());
                    }
                } else createdUser.setValue(null);
            }

            @Override
            public void onFailure(@NotNull Call<SignUpResponseI<Setter>> call, @NotNull Throwable t) {
                t.printStackTrace();
                code = 400;
                createdUser.setValue(null);
            }
        });

        return createdUser;
    }

    public int getStatusCode() {
        return code;
    }
}
