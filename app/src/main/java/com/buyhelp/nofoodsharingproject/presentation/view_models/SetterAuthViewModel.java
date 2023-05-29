package com.buyhelp.nofoodsharingproject.presentation.view_models;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.data.api.auth.AuthRepository;
import com.buyhelp.nofoodsharingproject.data.api.auth.dto.SignUpResponseI;
import com.buyhelp.nofoodsharingproject.data.models.Setter;
import com.buyhelp.nofoodsharingproject.domain.utils.DefineUser;
import com.buyhelp.nofoodsharingproject.domain.utils.ValidateUser;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetterAuthViewModel extends AndroidViewModel {
    private final MutableLiveData<String> tokenFCM = new MutableLiveData<>();
    private final MutableLiveData<SignUpResponseI<Setter>> createdUser = new MutableLiveData<>();

    public SetterAuthViewModel(@NonNull Application application) {
        super(application);
    }

    private void pushData(SignUpResponseI<Setter> result) {
        DefineUser<Setter> defineUser = new DefineUser<>(getApplication().getApplicationContext());
        defineUser.saveUserData(false, result.user.getX5_Id(), result);
    }

    public LiveData<SignUpResponseI<Setter>> login(String login, String password) {
        AuthRepository authRepository = new AuthRepository();
        authRepository.setterLogin(getApplication(), login, password).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<SignUpResponseI<Setter>> call, @NotNull Response<SignUpResponseI<Setter>> response) {
                if (response.code() == 400) {
                    Toast.makeText(getApplication(), R.string.not_right_password, Toast.LENGTH_SHORT).show();
                    createdUser.setValue(null);
                } else if (response.code() == 404) {
                    Toast.makeText(getApplication(), R.string.account_not_exist, Toast.LENGTH_SHORT).show();
                    createdUser.setValue(null);
                } else if (response.isSuccessful() && response.body() != null && response.body().token != null) {
                    createdUser.setValue(response.body());
                    pushData(response.body());
                } else {
                    Toast.makeText(getApplication(), R.string.unvisinle_error, Toast.LENGTH_SHORT).show();
                    createdUser.setValue(null);
                }
            }

            @Override
            public void onFailure(@NotNull Call<SignUpResponseI<Setter>> call, @NotNull Throwable t) {
                t.printStackTrace();
                createdUser.setValue(null);
            }
        });

        return createdUser;
    }

    public LiveData<SignUpResponseI<Setter>> signup(String tokenFCM, String dtoPhone, String dtoLogin, String dtoPassword) {
        AuthRepository authRepository = new AuthRepository();
        authRepository.setterRegistration(getApplication(), dtoPhone, dtoLogin, dtoPassword, tokenFCM).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<SignUpResponseI<Setter>> call, @NotNull Response<SignUpResponseI<Setter>> response) {
                if (response.code() == 400) {
                    Toast.makeText(getApplication(), R.string.account_created, Toast.LENGTH_SHORT).show();
                    createdUser.setValue(null);
                } else if (response.isSuccessful()) {
                    if (response.body() != null && response.body().token != null) {
                        createdUser.setValue(response.body());
                        pushData(response.body());
                    }
                } else {
                    Toast.makeText(getApplication(), R.string.unvisinle_error, Toast.LENGTH_SHORT).show();
                    createdUser.setValue(null);
                }
            }

            @Override
            public void onFailure(@NotNull Call<SignUpResponseI<Setter>> call, @NotNull Throwable t) {
                t.printStackTrace();
                createdUser.setValue(null);
            }
        });

        return createdUser;
    }

    public boolean validate(String phone, String login, String password) {
        if (!ValidateUser.validatePhone(phone)) {
            Toast.makeText(getApplication(), R.string.uncorrect_number_phone, Toast.LENGTH_LONG).show();
            return false;
        }
        if (!ValidateUser.validateLogin(login)) {
            Toast.makeText(getApplication(), R.string.uncorrect_name, Toast.LENGTH_LONG).show();
            return false;
        }
        if (!ValidateUser.validatePassword(password)) {
            Toast.makeText(getApplication(), R.string.uncorrect_password, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
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
}
