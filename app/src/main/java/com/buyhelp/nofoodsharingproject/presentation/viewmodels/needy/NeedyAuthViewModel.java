package com.buyhelp.nofoodsharingproject.presentation.viewmodels.needy;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.data.api.auth.AuthRepository;
import com.buyhelp.nofoodsharingproject.data.api.auth.dto.SignUpResponseI;
import com.buyhelp.nofoodsharingproject.data.models.Needy;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NeedyAuthViewModel extends AndroidViewModel {
    private final MutableLiveData<String> tokenFCM = new MutableLiveData<>();
    private final MutableLiveData<SignUpResponseI<Needy>> createdUser = new MutableLiveData<>();
    private final MutableLiveData<Integer> statusCode = new MutableLiveData<>(0);
    private final AuthRepository authRepository;

    public NeedyAuthViewModel(@NonNull Application application, AuthRepository authRepository) {
        super(application);
        this.authRepository = authRepository;
    }

    private void pushData(SignUpResponseI<Needy> result) {
        DefineUser defineUser = new DefineUser(getApplication().getApplicationContext());
        defineUser.saveUserDataNeedy(true, result.user.getX5_Id(), result);
    }

    public LiveData<Integer> login(String phone, String login, String password) {
        statusCode.setValue(0);
        authRepository.needyLogin(phone, login, password).enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<SignUpResponseI<Needy>> call, @NotNull Response<SignUpResponseI<Needy>> response) {
                    statusCode.setValue(response.code());
                    if (response.isSuccessful() && response.body() != null && response.body().token != null) {
                        createdUser.setValue(response.body());
                        pushData(response.body());
                    } else createdUser.setValue(null);
                }

                @Override
                public void onFailure(@NotNull Call<SignUpResponseI<Needy>> call, @NotNull Throwable t) {
                    t.printStackTrace();
                    statusCode.setValue(400);
                    createdUser.setValue(null);
                }
            });

        return statusCode;
    }

    public LiveData<Integer> signup(String tokenFCM, String dtoPhone, String dtoLogin, String dtoPassword) {
        statusCode.setValue(0);
        authRepository.needyRegistration(dtoPhone, dtoLogin, dtoPassword, tokenFCM).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<SignUpResponseI<Needy>> call, @NotNull Response<SignUpResponseI<Needy>> response) {
                statusCode.setValue(response.code());
                if (response.isSuccessful() && response.body() != null && response.body().token != null) {
                    createdUser.setValue(response.body());
                    pushData(response.body());
                } else createdUser.setValue(null);
            }

            @Override
            public void onFailure(@NotNull Call<SignUpResponseI<Needy>> call, @NotNull Throwable t) {
                t.printStackTrace();
                statusCode.setValue(400);
                createdUser.setValue(null);
            }
        });

        return statusCode;
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

    public LiveData<Integer> getStatusCode() {
        return statusCode;
    }

    public void clearStatusCode() {
        statusCode.setValue(0);
    }
}
