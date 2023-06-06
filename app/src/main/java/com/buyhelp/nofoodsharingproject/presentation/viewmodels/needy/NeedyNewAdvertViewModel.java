package com.buyhelp.nofoodsharingproject.presentation.viewmodels.needy;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.buyhelp.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.buyhelp.nofoodsharingproject.data.models.Advertisement;
import com.buyhelp.nofoodsharingproject.data.models.Needy;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NeedyNewAdvertViewModel extends AndroidViewModel {
    private final MutableLiveData<List<String>> userProductItems = new MutableLiveData<>();
    private final List<String> userItems = new ArrayList<>();
    private final MutableLiveData<Advertisement> advert = new MutableLiveData<>();
    private int statusCode = 0;
    private final AdvertsRepository advertsRepository;
    private final MutableLiveData<Boolean> isCreatedNavigate = new MutableLiveData<>(false);

    private final String[] productItems = new String[]{"Хлеб", "Картофель", "Мороженая рыба", "Сливочное масло",
            "Подсолнечное масло", "Яйца куриные", "Молоко", "Чай", "Кофе", "Соль", "Сахар",
            "Мука", "Лук", "Макаронные изделия", "Пшено", "Шлифованный рис", "Гречневая крупа",
            "Белокочанная капуста", "Морковь", "Яблоки", "Свинина", "Баранина", "Курица"};

    public NeedyNewAdvertViewModel(@NonNull Application application, AdvertsRepository advertsRepository) {
        super(application);
        this.advertsRepository = advertsRepository;
    }

    public LiveData<Advertisement> createAdvert(String needyAdvertInputTitle) {
        DefineUser defineUser = new DefineUser(getApplication().getApplicationContext());
        Needy result = defineUser.defineNeedy();

        Advertisement advertisement = new Advertisement(needyAdvertInputTitle, result.getX5_Id(), result.getLogin());
        advertisement.setGettingProductID(Advertisement.generateID());

        if (userProductItems != null && userProductItems.getValue().size() > 0) advertisement.setListProductsCustom(userProductItems.getValue());

        statusCode = 0;
        advertsRepository.createAdvert(advertisement).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<Advertisement> call, @NotNull Response<Advertisement> response) {
                statusCode = response.code();
                if (response.isSuccessful()) {
                    advert.setValue(response.body());
                    isCreatedNavigate.setValue(true);
                }
            }

            @Override
            public void onFailure(@NotNull Call<Advertisement> call, @NotNull Throwable t) {
                statusCode = 400;
                t.printStackTrace();
            }
        });

        return advert;
    }

    public String[] getProductItems() {
        return this.productItems;
    }

    public List<String> getUserItems() {
        return this.userItems;
    }

    public boolean isContainsItem(int pos) {
        return userItems.contains(productItems[pos]);
    }

    public LiveData<List<String>> updateItem(int pos) {
        userItems.add(productItems[pos]);
        userProductItems.setValue(userItems);

        return userProductItems;
    }

    public LiveData<List<String>> removeItem(int pos) {
        userItems.remove(pos);
        userProductItems.setValue(userItems);

        return userProductItems;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public LiveData<Boolean> isNavigate() {
        return isCreatedNavigate;
    }

    public void cancelNavigate() {
        isCreatedNavigate.setValue(false);
    }
}
