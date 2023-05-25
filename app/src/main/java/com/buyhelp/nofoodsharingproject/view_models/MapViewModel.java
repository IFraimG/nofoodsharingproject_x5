package com.buyhelp.nofoodsharingproject.view_models;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.buyhelp.nofoodsharingproject.data.api.map.MapRepository;
import com.buyhelp.nofoodsharingproject.data.api.map.dto.MarketTitleResponse;
import com.buyhelp.nofoodsharingproject.models.Getter;
import com.buyhelp.nofoodsharingproject.models.Market;
import com.buyhelp.nofoodsharingproject.models.Setter;
import com.yandex.mapkit.geometry.Point;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
public class MapViewModel extends AndroidViewModel {
    private final Market[] fullListMarkets = new Market[]{
            new Market("Выберите магазин", 0, 0, false),
            new Market("Большая Андроньевская улица, 22", 55.740813, 37.670078),
            new Market("1, микрорайон Парковый, Котельники", 55.660216, 37.875793),
            new Market("Ковров пер., 8, стр. 1", 55.740582, 37.681854),
            new Market("Нижегородская улица, 34", 55.736351, 37.695708)
    };

    private final List<Point> marketPoints = Arrays.asList(
            fullListMarkets[1].getPoint(),
            fullListMarkets[2].getPoint(),
            fullListMarkets[3].getPoint(),
            fullListMarkets[4].getPoint()
    );

    private String choosenMarket;
    private int oldPosition = -1;
    private String[] listMarkets;
    private final MutableLiveData<List<String>> _markets = new MutableLiveData<>();

    public MapViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<String>> getPinnedMarketInfo(String userType, String userID) {
        MapRepository.getPinMarket(getApplication().getApplicationContext(), userType, userID).enqueue(new Callback<MarketTitleResponse>() {
            @Override
            public void onResponse(@NotNull Call<MarketTitleResponse> call, @NotNull retrofit2.Response<MarketTitleResponse> response) {
                if (response.code() == 404) {
                    choosenMarket = "";
                    listMarkets = new String[fullListMarkets.length];
                    for (int i = 0; i < fullListMarkets.length; i++) {
                        listMarkets[i] = fullListMarkets[i].getTitle();
                    }
                } else {
                    if (response.isSuccessful() && response.body() != null) {
                        listMarkets = new String[fullListMarkets.length - 1];
                        choosenMarket = response.body().market;
                        for (int i = 1; i < fullListMarkets.length; i++) {
                            if (fullListMarkets[i].getTitle().equals(choosenMarket)) {
                                oldPosition = i;
                            }
                            listMarkets[i - 1] = fullListMarkets[i].getTitle();
                        }
                    }
                }

                _markets.setValue(Arrays.asList(listMarkets));
            }

            @Override
            public void onFailure(@NotNull Call<MarketTitleResponse> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });

        return _markets;
    }


    public int getOldPosition() {
        return this.oldPosition;
    }

    public void changePosition(int position) {
        if (_markets.getValue() != null) {
            choosenMarket = _markets.getValue().get(position);
            oldPosition = position;
        }
    }

    public void updateMarket(boolean isGetter, String userID) {
        if (!choosenMarket.equals("Выберите магазин")) {
            if (isGetter) {
                MapRepository.setGetterMarket(getApplication().getApplicationContext(), userID, choosenMarket).enqueue(new Callback<Getter>() {
                    @Override
                    public void onResponse(@NotNull Call<Getter> call, @NotNull retrofit2.Response<Getter> response) {}

                    @Override
                    public void onFailure(@NotNull Call<Getter> call, @NotNull Throwable t) {
                        t.printStackTrace();
                    }
                });
            } else {
                MapRepository.setSetterMarket(getApplication().getApplicationContext(), userID, choosenMarket).enqueue(new Callback<Setter>() {
                    @Override
                    public void onResponse(@NotNull Call<Setter> call, @NotNull retrofit2.Response<Setter> response) {}

                    @Override
                    public void onFailure(@NotNull Call<Setter> call, @NotNull Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        }
    }

    public Market[] getFullListMarkets() {
        return this.fullListMarkets;
    }

    public List<Point> getMarketPoints() {
        return marketPoints;
    }
}
