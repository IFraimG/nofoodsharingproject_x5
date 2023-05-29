package com.buyhelp.nofoodsharingproject.presentation.view_models;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.buyhelp.nofoodsharingproject.data.api.adverts.dto.ResponseDeleteAdvert;
import com.buyhelp.nofoodsharingproject.data.api.map.MapRepository;
import com.buyhelp.nofoodsharingproject.data.api.map.dto.MarketTitleResponse;
import com.buyhelp.nofoodsharingproject.data.api.notifications.NotificationRepository;
import com.buyhelp.nofoodsharingproject.data.api.notifications.dto.ResponseFCMToken;
import com.buyhelp.nofoodsharingproject.data.api.setter.SetterRepository;
import com.buyhelp.nofoodsharingproject.data.models.Advertisement;
import com.buyhelp.nofoodsharingproject.data.models.LoaderStatus;
import com.buyhelp.nofoodsharingproject.data.models.Notification;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdvertisementOneViewModel extends AndroidViewModel {
    private final MutableLiveData<Advertisement> _advert = new MutableLiveData<>();
    private final MutableLiveData<String> _market = new MutableLiveData<>();
    private final MutableLiveData<LoaderStatus> _statusNotifications = new MutableLiveData<>();
    private final MutableLiveData<LoaderStatus> _status = new MutableLiveData<>();
    private final MutableLiveData<LoaderStatus> _statusRemove = new MutableLiveData<>();

    public AdvertisementOneViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Advertisement> getAdvert(String userID) {
        AdvertsRepository advertsRepository = new AdvertsRepository();
        _status.setValue(LoaderStatus.LOADING);
        advertsRepository.getOwnAdvert(getApplication().getApplicationContext(), userID).enqueue(new Callback<Advertisement>() {
            @Override
            public void onResponse(@NotNull Call<Advertisement> call, @NotNull Response<Advertisement> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _advert.setValue(response.body());
                    _status.setValue(LoaderStatus.LOADED);
                } else {
                    if (response.code() == 404) _advert.setValue(null);
                    _status.setValue(LoaderStatus.FAILURE);
                }
            }

            @Override
            public void onFailure(@NotNull Call<Advertisement> call, @NotNull Throwable t) {
                _status.setValue(LoaderStatus.FAILURE);
                t.printStackTrace();
            }
        });

        return _advert;
    }

    public LiveData<LoaderStatus> getLoaderStatus() {
        return _status;
    }

    public LiveData<LoaderStatus> getLoaderNotificationStatus() {
        return _statusNotifications;
    }

    public LiveData<LoaderStatus> getLoaderRemoveStatus() {
        return _statusRemove;
    }


    public void removeAdvertisement() {
        AdvertsRepository advertsRepository = new AdvertsRepository();
        _statusRemove.setValue(LoaderStatus.LOADING);
        advertsRepository.deleteAdvert(getApplication().getApplicationContext(), _advert.getValue().getAdvertsID()).enqueue(new Callback<ResponseDeleteAdvert>() {
            @Override
            public void onResponse(@NotNull Call<ResponseDeleteAdvert> call, @NotNull Response<ResponseDeleteAdvert> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isDelete) {
                    _advert.setValue(null);
                    _statusRemove.setValue(LoaderStatus.LOADED);
                } else _statusRemove.setValue(LoaderStatus.FAILURE);
            }

            @Override
            public void onFailure(@NotNull Call<ResponseDeleteAdvert> call, @NotNull Throwable t) {
                _statusRemove.setValue(LoaderStatus.FAILURE);
            }
        });
    }

    public LiveData<String> getAddress(String userID, boolean isGetter) {
        String userData = isGetter ? "getter" : "setter";

        MapRepository.getPinMarket(getApplication().getApplicationContext(), userData, userID).enqueue(new Callback<MarketTitleResponse>() {
            @Override
            public void onResponse(@NotNull Call<MarketTitleResponse> call, @NotNull Response<MarketTitleResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _market.setValue(response.body().market);
                }
            }

            @Override
            public void onFailure(@NotNull Call<MarketTitleResponse> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });

        return _market;
    }

    public String getMarket() {
        return _market.getValue();
    }


    public String getTimer() {
        if (_advert.getValue() != null) {

            String timeStart = _advert.getValue().getDateDone();

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
            dateFormat2.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
            dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
            Date currentDate = new Date();
            String timeEnd = dateFormat.format(currentDate);

            try {
                Date firstDate = dateFormat.parse(timeStart);
                Date secondDate = dateFormat.parse(timeEnd);

                long diffInMillis = 2 * 60 * 60 * 1000 - (secondDate.getTime() - firstDate.getTime());

                long hours = TimeUnit.MILLISECONDS.toHours(diffInMillis);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) - TimeUnit.HOURS.toMinutes(hours);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis) - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours);

                return String.format("%02d:%02d:%02d", hours, minutes, seconds);
            } catch (ParseException err) {
                err.printStackTrace();
            }
        }
        return "00:00:00";
    }

    public void takeProducts(String userID) {
        AdvertsRepository advertsRepository = new AdvertsRepository();
        _statusNotifications.setValue(LoaderStatus.LOADING);
        advertsRepository.takingProducts(getApplication().getApplicationContext(), userID).enqueue(new Callback<Advertisement>() {
            @Override
            public void onResponse(@NotNull Call<Advertisement> call, @NotNull Response<Advertisement> response) {
                if (response.isSuccessful()) sendNotification();
            }

            @Override
            public void onFailure(@NotNull Call<Advertisement> call, @NotNull Throwable t) {
                _statusNotifications.setValue(LoaderStatus.FAILURE);
                t.printStackTrace();
            }
        });
    }

    // Шаг 2 - сохраняем сообщение об этом у отдающего
    private void sendNotification() {
        String body =  "Благодарим вас за помощь! Пользователь " + _advert.getValue().getAuthorName() + " забрал продукты";
        Notification notification = new Notification();
        notification.setTitle(getApplication().getApplicationContext().getString(R.string.success_deal));
        notification.setDescription(body);
        notification.setTypeOfUser("setter");
        notification.setFromUserID(_advert.getValue().getAuthorID());
        notification.setUserID(_advert.getValue().getUserDoneID());

        NotificationRepository notificationRepository = new NotificationRepository();
        notificationRepository.createNotification(getApplication().getApplicationContext(), notification).enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(@NotNull Call<Notification> call, @NotNull Response<Notification> response) {
                if (response.body() != null && response.isSuccessful()) getFMCToken(body);
            }

            @Override
            public void onFailure(@NotNull Call<Notification> call, @NotNull Throwable t) {
                _statusNotifications.setValue(LoaderStatus.FAILURE);
                t.printStackTrace();
            }
        });
    }

    // Шаг 3 - Получаем fmc token
    private void getFMCToken(String body) {
        SetterRepository setterRepository = new SetterRepository();
        setterRepository.getFCMtoken(getApplication().getApplicationContext(), _advert.getValue().getUserDoneID()).enqueue(new Callback<ResponseFCMToken>() {
            @Override
            public void onResponse(@NotNull Call<ResponseFCMToken> call, @NotNull Response<ResponseFCMToken> response) {
                if (response.isSuccessful() && response.body() != null) sendFMCMessage(response.body(), body);
            }

            @Override
            public void onFailure(@NotNull Call<ResponseFCMToken> call, @NotNull Throwable t) {
                _statusNotifications.setValue(LoaderStatus.FAILURE);
                t.printStackTrace();
            }
        });
    }

    // Шаг 4 - Отправляем через firebase сообщение об успешно полученном токене
    private void sendFMCMessage(ResponseFCMToken response, String body) {
        NotificationRepository notificationRepository = new NotificationRepository();

        notificationRepository.requestNotifyDonateCall(response.getFcmToken(), getApplication().getApplicationContext().getString(R.string.success_deal), body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    _advert.setValue(null);
                    _statusNotifications.setValue(LoaderStatus.LOADED);
                }
            }
            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                _statusNotifications.setValue(LoaderStatus.FAILURE);
                t.printStackTrace();
            }
        });
    }

}
