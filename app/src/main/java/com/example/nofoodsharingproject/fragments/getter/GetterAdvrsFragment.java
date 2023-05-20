package com.example.nofoodsharingproject.fragments.getter;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.activities.FaqActivity;
import com.example.nofoodsharingproject.activities.GetterNewAdvertActivity;
import com.example.nofoodsharingproject.data.api.adverts.dto.ResponseDeleteAdvert;
import com.example.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.example.nofoodsharingproject.data.api.map.MapRepository;
import com.example.nofoodsharingproject.data.api.notifications.NotificationRepository;
import com.example.nofoodsharingproject.data.api.notifications.dto.ResponseFCMToken;
import com.example.nofoodsharingproject.data.api.setter.SetterRepository;
import com.example.nofoodsharingproject.databinding.FragmentGetterAdvrsBinding;
import com.example.nofoodsharingproject.models.Advertisement;
import com.example.nofoodsharingproject.data.api.map.dto.MarketTitleResponse;
import com.example.nofoodsharingproject.models.Getter;
import com.example.nofoodsharingproject.models.Notification;
import com.example.nofoodsharingproject.utils.DateNowChecker;
import com.example.nofoodsharingproject.utils.DateNowCheckerOld;
import com.example.nofoodsharingproject.utils.DefineUser;

import org.jetbrains.annotations.NotNull;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetterAdvrsFragment extends Fragment {
    private FragmentGetterAdvrsBinding binding;
    private Advertisement advertisement;
    private ArrayAdapter<String> arrayAdapter;
    private String market;
    private Pair<String, Boolean> userType;
    private DefineUser<Getter> defineUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        defineUser = new DefineUser<>(requireActivity());
        userType = defineUser.getTypeUser();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentGetterAdvrsBinding.inflate(inflater);

        binding.textNumberOfAdvert.setVisibility(View.GONE);
        binding.stopAdvert.setVisibility(View.GONE);
        binding.getterAdvertLayout.setVisibility(View.GONE);

        getAddress();
        getAdvertisement();
        initHandlers();

        return binding.getRoot();
    }

    private void initHandlers() {
        binding.createNewRequest.setOnClickListener(View -> {
            if (market == null || market.length() == 0) Toast.makeText(getContext(), getString(R.string.pin_market), Toast.LENGTH_LONG).show();
            else startActivity(new Intent(getActivity(), GetterNewAdvertActivity.class));
        });

        binding.stopAdvert.setOnClickListener(View -> removeAdvertisement());
        binding.pickUpOrder.setOnClickListener(View -> takeProducts());

        binding.getterAdvertFaq.setOnClickListener(View -> {
            Intent intent = new Intent(getContext(), FaqActivity.class);
            startActivity(intent);
        });

        binding.getterAdvertSwiper.setOnRefreshListener(this::getAdvertisement);
    }

    private void hideAdvertisementElements() {
        advertisement = null;
        binding.getterAdvertStatus.setVisibility(View.VISIBLE);
        showCreateButton();
        binding.stopAdvert.setVisibility(View.GONE);
        arrayAdapter.notifyDataSetChanged();
        binding.pickUpOrder.setVisibility(View.GONE);
        binding.numberOfAdvertisement.setVisibility(View.GONE);
        binding.textNumberOfAdvert.setVisibility(View.GONE);
        binding.getterAdvertLayout.setVisibility(View.GONE);
    }

    private void showAdvertisementElements(Advertisement advert) {
        binding.getterAdvertTitleProducts.setText(advert.getTitle());
        binding.getterAdvertStatus.setVisibility(View.GONE);
        binding.createNewRequest.setVisibility(View.GONE);
        binding.stopAdvert.setVisibility(View.VISIBLE);
        arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.item_getter_product_name, advert.getListTitleProducts());
        binding.getterAdvertProducts.setAdapter(arrayAdapter);
        binding.getterAdvertLayout.setVisibility(View.VISIBLE);
        advertisement = advert;

        if (advert.getGettingProductID() != null && advert.getGettingProductID().length() > 0) {
            binding.pickUpOrder.setVisibility(View.VISIBLE);
            binding.numberOfAdvertisement.setVisibility(View.VISIBLE);
            binding.textNumberOfAdvert.setVisibility(View.VISIBLE);
            binding.numberOfAdvertisement.setText(advert.getGettingProductID());
        }
    }

    private void showCreateButton() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateNowChecker dateNowChecker = new DateNowChecker();
            if (dateNowChecker.getHour() >= 10 && dateNowChecker.getHour() < 23) {
                binding.createNewRequest.setVisibility(View.VISIBLE);
            }
        } else {
            DateNowCheckerOld dateNowCheckerOld = new DateNowCheckerOld();
            if (dateNowCheckerOld.getHour() >= 10 && dateNowCheckerOld.getHour() < 23) {
                binding.createNewRequest.setVisibility(View.VISIBLE);

            }
        }
    }

    // _______________ РАБОТА С ОБЪЯВЛЕНИЯМИ ____________________
    private void getAdvertisement() {
        AdvertsRepository.getOwnAdvert(userType.first).enqueue(new Callback<Advertisement>() {
            @Override
            public void onResponse(@NotNull Call<Advertisement> call, @NotNull Response<Advertisement> response) {
                if (response.code() == 400) {
                    showCreateButton();
                    Toast.makeText(getContext(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
                }
                if (response.code() == 404) showCreateButton();
                if (response.isSuccessful() && response.body() != null) {
                    showAdvertisementElements(response.body());
                }
                binding.getterAdvertSwiper.setRefreshing(false);
            }

            @Override
            public void onFailure(@NotNull Call<Advertisement> call, @NotNull Throwable t) {
                t.printStackTrace();
                binding.getterAdvertSwiper.setRefreshing(false);
                Toast.makeText(getContext(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeAdvertisement() {
        AdvertsRepository.deleteAdvert(advertisement.getAdvertsID()).enqueue(new Callback<ResponseDeleteAdvert>() {
            @Override
            public void onResponse(@NotNull Call<ResponseDeleteAdvert> call, @NotNull Response<ResponseDeleteAdvert> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isDelete) {
                    Toast.makeText(getContext(), R.string.sucsesfully_deleted, Toast.LENGTH_SHORT).show();
                    hideAdvertisementElements();
                } else Toast.makeText(getContext(), R.string.error_on_delated, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NotNull Call<ResponseDeleteAdvert> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), R.string.error_on_delated, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // _______________ РАБОТА С УВЕДОМЛЕНИЯМИ И СОХРАНЕНИЕМ ПРОДУКТОВ ____________________
    // Шаг 1 - забираем продукты
    private void takeProducts() {
        AdvertsRepository.takingProducts(userType.first).enqueue(new Callback<Advertisement>() {
            @Override
            public void onResponse(@NotNull Call<Advertisement> call, @NotNull Response<Advertisement> response) {
                if (!response.isSuccessful()) Toast.makeText(getContext(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
                else if (response.code() == 201) sendNotification();
            }

            @Override
            public void onFailure(@NotNull Call<Advertisement> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Шаг 2 - сохраняем сообщение об этом у отдающего
    private void sendNotification() {
        String body =  "Благодарим вас за помощь! Пользователь " + advertisement.getAuthorName() + " забрал продукты";
        Notification notification = new Notification();
        notification.setTitle(getString(R.string.success_deal));
        notification.setDescription(body);
        notification.setTypeOfUser("setter");
        notification.setFromUserID(advertisement.getAuthorID());
        notification.setUserID(advertisement.getUserDoneID());
        NotificationRepository.createNotification(notification).enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(@NotNull Call<Notification> call, @NotNull Response<Notification> response) {
                if (response.body() == null || !response.isSuccessful()) {
                    Toast.makeText(getContext(), R.string.unvisinle_error, Toast.LENGTH_SHORT).show();
                } else getFMCToken(body);
            }

            @Override
            public void onFailure(@NotNull Call<Notification> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), R.string.unvisinle_error, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    // Шаг 3 - Получаем fmc token
    private void getFMCToken(String body) {
        SetterRepository.getFCMtoken(advertisement.getUserDoneID()).enqueue(new Callback<ResponseFCMToken>() {
            @Override
            public void onResponse(@NotNull Call<ResponseFCMToken> call, @NotNull Response<ResponseFCMToken> response) {
                if (!response.isSuccessful() || response.body() == null) Toast.makeText(getContext(), R.string.unvisinle_error, Toast.LENGTH_SHORT).show();
                else sendFMCMessage(response.body(), body);
            }

            @Override
            public void onFailure(@NotNull Call<ResponseFCMToken> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), R.string.unvisinle_error, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    // Шаг 4 - Отправляем через firebase сообщение об успешно полученном токене
    private void sendFMCMessage(ResponseFCMToken response, String body) {
        NotificationRepository.requestNotifyDonateCall(response.getFcmToken(), getString(R.string.success_deal), body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), R.string.deal_correct, Toast.LENGTH_LONG).show();
                    hideAdvertisementElements();
                }
            }
            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), R.string.unvisinle_error, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    // _______________ ИНФОРМАЦИЯ О МАГАЗИНЕ ____________________
    private void getAddress() {
        String userData = userType.second ? "getter" : "setter";

        MapRepository.getPinMarket(userData, userType.first).enqueue(new Callback<MarketTitleResponse>() {
            @Override
            public void onResponse(@NotNull Call<MarketTitleResponse> call, @NotNull Response<MarketTitleResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    market = response.body().market;
                    binding.addressShop.setText(response.body().market);
                    binding.addressShop.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NotNull Call<MarketTitleResponse> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}