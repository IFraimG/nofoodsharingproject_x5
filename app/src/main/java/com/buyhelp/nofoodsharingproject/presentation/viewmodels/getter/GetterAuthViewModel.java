package com.buyhelp.nofoodsharingproject.presentation.viewmodels.getter;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.data.api.auth.AuthRepository;
import com.buyhelp.nofoodsharingproject.data.api.auth.dto.SignUpResponseI;
import com.buyhelp.nofoodsharingproject.data.models.Getter;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.google.firebase.messaging.FirebaseMessaging;
import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetterAuthViewModel extends AndroidViewModel {
    private final MutableLiveData<String> tokenFCM = new MutableLiveData<>();
    private final MutableLiveData<SignUpResponseI<Getter>> createdUser = new MutableLiveData<>();
    private int statusCode = 0;

    public GetterAuthViewModel(@NonNull Application application) {
        super(application);
    }

    private void pushData(SignUpResponseI<Getter> result) {
        DefineUser<Getter> defineUser = new DefineUser<>(getApplication().getApplicationContext());
        defineUser.saveUserData(true, result.user.getX5_Id(), result);
    }

    public LiveData<SignUpResponseI<Getter>> login(String phone, String login, String password) {
        AuthRepository authRepository = new AuthRepository();
        statusCode = 0;
        authRepository.getterLogin(getApplication(), phone, login, password).enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<SignUpResponseI<Getter>> call, @NotNull Response<SignUpResponseI<Getter>> response) {
                    statusCode = response.code();
                    if (response.isSuccessful() && response.body() != null && response.body().token != null) {
                        createdUser.setValue(response.body());
                        pushData(response.body());
                    } else createdUser.setValue(null);
                }

                @Override
                public void onFailure(@NotNull Call<SignUpResponseI<Getter>> call, @NotNull Throwable t) {
                    t.printStackTrace();
                    statusCode = 400;
                    createdUser.setValue(null);
                }
            });

        return createdUser;
    }

    public LiveData<SignUpResponseI<Getter>> signup(String tokenFCM, String dtoPhone, String dtoLogin, String dtoPassword) {
        AuthRepository authRepository = new AuthRepository();
        statusCode = 0;
        authRepository.getterRegistration(getApplication(), dtoPhone, dtoLogin, dtoPassword, tokenFCM).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<SignUpResponseI<Getter>> call, @NotNull Response<SignUpResponseI<Getter>> response) {
                statusCode = response.code();
                if (response.isSuccessful() && response.body() != null && response.body().token != null) {
                    createdUser.setValue(response.body());
                    pushData(response.body());
                } else createdUser.setValue(null);
            }

            @Override
            public void onFailure(@NotNull Call<SignUpResponseI<Getter>> call, @NotNull Throwable t) {
                t.printStackTrace();
                statusCode = 400;
                createdUser.setValue(null);
            }
        });

        return createdUser;
    }

    public LiveData<String> sendToNotifyAccount() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w("err", getApplication().getString(R.string.error_fmc), task.getException());
                tokenFCM.setValue("");
            } else tokenFCM.setValue(task.getResult());
        });

        return tokenFCM;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
