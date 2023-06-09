package com.buyhelp.nofoodsharingproject.presentation.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.buyhelp.nofoodsharingproject.data.api.map.MapRepository;
import com.buyhelp.nofoodsharingproject.data.api.map.dto.MarketTitleResponse;
import com.buyhelp.nofoodsharingproject.data.models.Giver;
import com.buyhelp.nofoodsharingproject.data.models.Needy;
import com.buyhelp.nofoodsharingproject.data.models.Market;
import com.yandex.mapkit.geometry.Point;


import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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


    private final MutableLiveData<String> choosenMarket = new MutableLiveData<>();
    private MutableLiveData<Integer> oldPosition = new MutableLiveData<>(-1);
    private String[] listMarkets;
    private final MutableLiveData<List<String>> _markets = new MutableLiveData<>();
    private final MapRepository mapRepository;

    public MapViewModel(@NonNull Application application, MapRepository mapRepository) {
        super(application);

        this.mapRepository = mapRepository;
    }

    public LiveData<List<String>> getPinnedMarketInfo(String userType, String userID) {
        mapRepository.getPinMarket(userType, userID).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MarketTitleResponse> call, @NonNull Response<MarketTitleResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                        listMarkets = new String[fullListMarkets.length - 1];
                        choosenMarket.setValue(response.body().market);
                        for (int i = 1; i < fullListMarkets.length; i++) {
                            if (fullListMarkets[i].getTitle().equals(choosenMarket.getValue())) {
                                if (oldPosition.getValue() != null) {
                                    if (oldPosition.getValue() == -1) oldPosition.setValue(i);
                                    else oldPosition.setValue(i - 1);
                                }
                            }
                            listMarkets[i - 1] = fullListMarkets[i].getTitle();
                        }
                } else {
                    choosenMarket.setValue("");
                    listMarkets = new String[fullListMarkets.length];
                    for (int i = 0; i < fullListMarkets.length; i++) {
                        listMarkets[i] = fullListMarkets[i].getTitle();
                    }
                }

                _markets.setValue(Arrays.asList(listMarkets));
            }

            @Override
            public void onFailure(@NonNull Call<MarketTitleResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });

        return _markets;
    }


    public int getOldPosition() {
        if (oldPosition.getValue() == null) return -1;
        return oldPosition.getValue();
    }

    public void changePosition(int position) {
        if (_markets.getValue() != null) {
            choosenMarket.setValue(_markets.getValue().get(position));
            oldPosition.setValue(position);
        }
    }

    public void updateMarket(boolean isNeedy, String userID) {
        if (choosenMarket.getValue() != null && !choosenMarket.getValue().equals("Выберите магазин")) {
            if (isNeedy) {
                mapRepository.setNeedyMarket(userID, choosenMarket.getValue()).enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NotNull Call<Needy> call, @NotNull retrofit2.Response<Needy> response) {}

                    @Override
                    public void onFailure(@NotNull Call<Needy> call, @NotNull Throwable t) {
                        t.printStackTrace();
                    }
                });
            } else {
                mapRepository.setGiverMarket(userID, choosenMarket.getValue()).enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NotNull Call<Giver> call, @NotNull retrofit2.Response<Giver> response) {}

                    @Override
                    public void onFailure(@NotNull Call<Giver> call, @NotNull Throwable t) {
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
