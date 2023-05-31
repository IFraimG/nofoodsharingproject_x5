package com.buyhelp.nofoodsharingproject.presentation.viewmodels.setter;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.buyhelp.nofoodsharingproject.data.api.adverts.dto.RequestDoneAdvert;
import com.buyhelp.nofoodsharingproject.data.api.getter.GetterRepository;
import com.buyhelp.nofoodsharingproject.data.api.notifications.NotificationRepository;
import com.buyhelp.nofoodsharingproject.data.api.notifications.dto.ResponseFCMToken;
import com.buyhelp.nofoodsharingproject.data.models.Advertisement;
import com.buyhelp.nofoodsharingproject.data.models.Notification;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;

import org.jetbrains.annotations.NotNull;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetterAdvertViewModel extends AndroidViewModel {
    private final MutableLiveData<Advertisement> _advert = new MutableLiveData<>();

    public SetterAdvertViewModel(Application application) {
        super(application);
    }

    public LiveData<Advertisement> getAdvert(String advertID) {
        AdvertsRepository advertsRepository = new AdvertsRepository();
        advertsRepository.getAdvertByID(getApplication(), advertID).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<Advertisement> call, @NotNull Response<Advertisement> response) {
                if (response.isSuccessful()) _advert.setValue(response.body());
                else _advert.setValue(null);
            }

            @Override
            public void onFailure(@NotNull Call<Advertisement> call, @NotNull Throwable t) {
                Toast.makeText(getApplication(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
            }
        });

        return _advert;
    }

    public LiveData<Advertisement> makeHelp(DefineUser defineUser) {
        String generateID = Advertisement.generateID();
        AdvertsRepository advertsRepository = new AdvertsRepository();

        advertsRepository.makeDoneAdvert(getApplication(), new RequestDoneAdvert(_advert.getValue().getAuthorID(), (String) defineUser.getTypeUser().first, generateID)).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<RequestDoneAdvert> call, @NotNull Response<RequestDoneAdvert> response) {
                if (response.isSuccessful()) saveMessageForUser(defineUser);
                else Toast.makeText(getApplication(), R.string.smth_not_good_try_again, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NotNull Call<RequestDoneAdvert> call, @NotNull Throwable t) {
                Toast.makeText(getApplication(), R.string.smth_not_good_try_again, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

        return _advert;
    }

    private void saveMessageForUser(DefineUser defineUser) {
        NotificationRepository notificationRepository = new NotificationRepository();

        Notification notification = new Notification(getApplication().getString(R.string.success_advert), getApplication().getString(R.string.success_advert_body), _advert.getValue().getAuthorID());
        notification.setFromUserID((String) defineUser.getTypeUser().first);
        notification.setListItems(_advert.getValue().getListProducts());
        notification.setTypeOfUser("getter");
        notificationRepository.createNotification(getApplication(), notification).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<Notification> call, @NotNull Response<Notification> response) {
                if (!response.isSuccessful()) Toast.makeText(getApplication(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
                else getFCMTokenByUserID();
            }

            @Override
            public void onFailure(@NotNull Call<Notification> call, @NotNull Throwable t) {
                Toast.makeText(getApplication(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void getFCMTokenByUserID() {
        GetterRepository getterRepository = new GetterRepository();
        getterRepository.getFCMtoken(getApplication(), _advert.getValue().getAuthorID()).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<ResponseFCMToken> call, @NotNull Response<ResponseFCMToken> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sendNotification(response.body().getFcmToken());
                } else Toast.makeText(getApplication(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NotNull Call<ResponseFCMToken> call, @NotNull Throwable t) {
                Toast.makeText(getApplication(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void sendNotification(String fcmToken) {
        NotificationRepository notificationRepository = new NotificationRepository();
        notificationRepository.requestNotifyDonateCall(fcmToken, getApplication().getString(R.string.success_advert), getApplication().getString(R.string.success_advert_body)).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplication(), R.string.sucses_notyfy_send, Toast.LENGTH_SHORT).show();
                } else Toast.makeText(getApplication(), R.string.smth_problrs, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Toast.makeText(getApplication(), R.string.smth_problrs, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    public Advertisement getAdvert() {
        return _advert.getValue();
    }
}
