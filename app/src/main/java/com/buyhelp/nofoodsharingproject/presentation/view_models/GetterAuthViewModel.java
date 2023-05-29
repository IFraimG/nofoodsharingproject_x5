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
import com.buyhelp.nofoodsharingproject.data.models.Getter;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.buyhelp.nofoodsharingproject.domain.utils.ValidateUser;
import com.google.firebase.messaging.FirebaseMessaging;
import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetterAuthViewModel extends AndroidViewModel {
    private final MutableLiveData<String> tokenFCM = new MutableLiveData<>();
    private final MutableLiveData<SignUpResponseI<Getter>> createdUser = new MutableLiveData<>();

    public GetterAuthViewModel(@NonNull Application application) {
        super(application);
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

    private void pushData(SignUpResponseI<Getter> result) {
        DefineUser<Getter> defineUser = new DefineUser<>(getApplication().getApplicationContext());
        defineUser.saveUserData(true, result.user.getX5_Id(), result);
    }

    public LiveData<SignUpResponseI<Getter>> login(String phone, String login, String password) {
        AuthRepository authRepository = new AuthRepository();
        authRepository.getterLogin(getApplication(), phone, login, password).enqueue(new Callback<>() {
                @Override
                public void onResponse(@NotNull Call<SignUpResponseI<Getter>> call, @NotNull Response<SignUpResponseI<Getter>> response) {
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
                public void onFailure(@NotNull Call<SignUpResponseI<Getter>> call, @NotNull Throwable t) {
                    t.printStackTrace();
                    createdUser.setValue(null);
                }
            });

        return createdUser;
    }

    public LiveData<SignUpResponseI<Getter>> signup(String tokenFCM, String dtoPhone, String dtoLogin, String dtoPassword) {
        AuthRepository authRepository = new AuthRepository();
        authRepository.getterRegistration(getApplication(), dtoPhone, dtoLogin, dtoPassword, tokenFCM).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<SignUpResponseI<Getter>> call, @NotNull Response<SignUpResponseI<Getter>> response) {
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
            public void onFailure(@NotNull Call<SignUpResponseI<Getter>> call, @NotNull Throwable t) {
                t.printStackTrace();
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
}
