/**
 * Класс {@code MainActivity} - активность, перенаправляющая пользователя в GiverActivity/NeedyActivity/MainAuthActivity
 * @author Кулагин Александр
 */

package com.buyhelp.nofoodsharingproject.presentation.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import com.buyhelp.nofoodsharingproject.data.api.auth.dto.CheckAuthI;
import com.buyhelp.nofoodsharingproject.data.api.auth.AuthRepository;
import com.buyhelp.nofoodsharingproject.databinding.ActivityMainBinding;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.buyhelp.nofoodsharingproject.presentation.ApplicationCore;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Inject
    public AuthRepository authRepository;

    @Inject
    public DefineUser defineUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ApplicationCore app = (ApplicationCore) getApplication();
        app.getAppComponent().inject(this);

        String res = defineUser.isNeedy();
        if (res != null) {
            if (res.equals("giver")) authGiver();
            else if (res.equals("needy")) authNeedy();
            else redirectToAuth();
        } else redirectToAuth();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        binding = null;
    }

    public void authGiver() {
        authRepository.checkAuthGiver(defineUser.getToken()).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<CheckAuthI> call, @NotNull Response<CheckAuthI> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getIsAuth()) {
                    Intent intentGiver = new Intent(getApplicationContext(), GiverActivity.class);
                    startActivity(intentGiver);
                    finish();
                } else redirectToAuth();
            }

            @Override
            public void onFailure(@NotNull Call<CheckAuthI> call, @NotNull Throwable t) {
                t.printStackTrace();
                redirectToAuth();
            }
        });
    }

    public void authNeedy() {
        authRepository.checkAuthNeedy(defineUser.getToken()).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<CheckAuthI> call, @NotNull Response<CheckAuthI> response) {
                if (response.body() == null) redirectToAuth();
                else if (response.body().getIsAuth()) {
                    Intent intentNeedy = new Intent(getApplicationContext(), NeedyActivity.class);
                    startActivity(intentNeedy);
                    finish();
                } else redirectToAuth();
            }

            @Override
            public void onFailure(@NotNull Call<CheckAuthI> call, @NotNull Throwable t) {
                t.printStackTrace();
                redirectToAuth();
            }
        });
    }

    public void redirectToAuth() {
        Intent intentAuth = new Intent(getApplicationContext(), MainAuthActivity.class);
        startActivity(intentAuth);
        finish();
    }
}