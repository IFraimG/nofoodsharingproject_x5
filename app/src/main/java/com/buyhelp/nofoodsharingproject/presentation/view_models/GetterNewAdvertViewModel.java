package com.buyhelp.nofoodsharingproject.presentation.view_models;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.buyhelp.nofoodsharingproject.data.models.Advertisement;
import com.buyhelp.nofoodsharingproject.data.models.Getter;
import com.buyhelp.nofoodsharingproject.domain.utils.DefineUser;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetterNewAdvertViewModel extends AndroidViewModel {
    private final MutableLiveData<List<String>> userProductItems = new MutableLiveData<>();
    private final List<String> userItems = new ArrayList<>();
    private final MutableLiveData<Advertisement> advert = new MutableLiveData<>();

    private final String[] productItems = new String[]{"Хлеб", "Картофель", "Мороженая рыба", "Сливочное масло",
            "Подсолнечное масло", "Яйца куриные", "Молоко", "Чай", "Кофе", "Соль", "Сахар",
            "Мука", "Лук", "Макаронные изделия", "Пшено", "Шлифованный рис", "Гречневая крупа",
            "Белокочанная капуста", "Морковь", "Яблоки", "Свинина", "Баранина", "Курица"};

    public GetterNewAdvertViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Advertisement> createAdvert(String getterAdvertInputTitle) {
        DefineUser<Getter> defineUser = new DefineUser<>(getApplication().getApplicationContext());
        Getter result = defineUser.defineGetter();

        Advertisement advertisement = new Advertisement(getterAdvertInputTitle, result.getX5_Id(), result.getLogin());
        advertisement.setGettingProductID(Advertisement.generateID());

        if (userProductItems != null && userProductItems.getValue().size() > 0) advertisement.setListProductsCustom(userProductItems.getValue());

        AdvertsRepository advertsRepository = new AdvertsRepository();
        advertsRepository.createAdvert(getApplication(), advertisement).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<Advertisement> call, @NotNull Response<Advertisement> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), R.string.problems, Toast.LENGTH_SHORT).show();

                } else {
                    advert.setValue(response.body());
                    Toast.makeText(getApplication(), R.string.advert_sucesfully_create, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Advertisement> call, @NotNull Throwable t) {
                Toast.makeText(getApplication(), R.string.smth_not_good, Toast.LENGTH_SHORT).show();
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

    public LiveData<List<String>> updateItem(int pos) {
        if (userItems.size() > 3) {
            Toast.makeText(getApplication(), R.string.lot_of_product, Toast.LENGTH_SHORT).show();
        } else if (!userItems.contains(productItems[pos])) {
            userItems.add(productItems[pos]);
            userProductItems.setValue(userItems);
            Toast.makeText(getApplication(), R.string.added, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplication(), R.string.this_product_added, Toast.LENGTH_SHORT).show();
        }

        return userProductItems;
    }

    public LiveData<List<String>> removeItem(int pos) {
        userItems.remove(pos);
        userProductItems.setValue(userItems);

        return userProductItems;
    }
}
