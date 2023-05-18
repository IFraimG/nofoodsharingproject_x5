package com.example.nofoodsharingproject.fragments.getter;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.activities.FaqActivity;
import com.example.nofoodsharingproject.activities.GetterNewAdvertActivity;
import com.example.nofoodsharingproject.data.api.adverts.dto.ResponseDeleteAdvert;
import com.example.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.example.nofoodsharingproject.data.api.map.MapRepository;
import com.example.nofoodsharingproject.databinding.FragmentGetterAdvrsBinding;
import com.example.nofoodsharingproject.models.Advertisement;
import com.example.nofoodsharingproject.data.api.map.dto.MarketTitleResponse;
import com.example.nofoodsharingproject.models.Getter;
import com.example.nofoodsharingproject.utils.DefineUser;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetterAdvrsFragment extends Fragment {
    private FragmentGetterAdvrsBinding binding;
    private TextView addressShop;
    private TextView numberAdvertisement;
    private Button buttonNewAdvertisement;
    private Button buttonTakenProducts;
    private Button buttonStopAdvert;
    private TextView textNewAdvert;
    private TextView titleAdvert;
    private LinearLayout statusAdvert;
    private ListView listViewProducts;
    private LinearLayout getterAdvertLayout;

    private Advertisement advertisement;
    private ArrayAdapter<String> arrayAdapter;
    private String market;
    private Pair<String, Boolean> userType;
    private DefineUser<Getter> defineUser;
    private TextView linkFaq;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        defineUser = new DefineUser<Getter>(requireActivity());
        userType = defineUser.getTypeUser();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        binding = FragmentGetterAdvrsBinding.inflate(inflater);

        addressShop = binding.addressShop;
        numberAdvertisement = binding.numberOfAdvertisement;
        buttonNewAdvertisement = binding.createNewRequest;
        buttonTakenProducts = binding.pickUpOrder;
        buttonStopAdvert = binding.stopAdvert;
        textNewAdvert = binding.textNumberOfAdvert;
        titleAdvert = binding.getterAdvertTitleProducts;
        listViewProducts = binding.getterAdvertProducts;
        statusAdvert = binding.getterAdvertStatus;
        getterAdvertLayout = binding.getterAdvertLayout;
        linkFaq = binding.getterAdvertFaq;

        getAddress();
        getAdvertisement();

        textNewAdvert.setVisibility(View.GONE);
        buttonStopAdvert.setVisibility(View.GONE);
        getterAdvertLayout.setVisibility(View.GONE);

        buttonNewAdvertisement.setOnClickListener(View -> {
            if (market == null || market.length() == 0) Toast.makeText(getContext(), getString(R.string.pin_market), Toast.LENGTH_LONG).show();
            else startActivity(new Intent(getActivity(), GetterNewAdvertActivity.class));
        });
        buttonStopAdvert.setOnClickListener(View -> removeAdvertisement());
        buttonTakenProducts.setOnClickListener(View -> takeProducts());

        linkFaq.setOnClickListener(View -> {
            Intent intent = new Intent(getContext(), FaqActivity.class);
            startActivity(intent);
        });


        return binding.getRoot();
    }

    private void getAdvertisement() {
        AdvertsRepository.getOwnAdvert(userType.first).enqueue(new Callback<Advertisement>() {
            @Override
            public void onResponse(@NotNull Call<Advertisement> call, @NotNull Response<Advertisement> response) {
                if (response.code() == 400) {
                    buttonNewAdvertisement.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
                }
                if (response.code() == 404) buttonNewAdvertisement.setVisibility(View.VISIBLE);
                if (response.code() == 200 && response.body() != null) showAdvertisementElements(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<Advertisement> call, @NotNull Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeAdvertisement() {
        AdvertsRepository.deleteAdvert(advertisement.getAdvertsID()).enqueue(new Callback<ResponseDeleteAdvert>() {
            @Override
            public void onResponse(@NotNull Call<ResponseDeleteAdvert> call, @NotNull Response<ResponseDeleteAdvert> response) {
                if (response.code() != 400 && response.body() != null && response.body().isDelete) {
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

    private void takeProducts() {
        AdvertsRepository.takingProducts(userType.first).enqueue(new Callback<Advertisement>() {
            @Override
            public void onResponse(@NotNull Call<Advertisement> call, @NotNull Response<Advertisement> response) {
                if (response.code() == 404) Toast.makeText(getContext(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
                else if (response.code() == 201) {
                    Toast.makeText(getContext(), R.string.deal_correct, Toast.LENGTH_LONG).show();
                    hideAdvertisementElements();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Advertisement> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAddress() {
        String userData = userType.second ? "getter" : "setter";

        MapRepository.getPinMarket(userData, userType.first).enqueue(new Callback<MarketTitleResponse>() {
            @Override
            public void onResponse(@NotNull Call<MarketTitleResponse> call, @NotNull Response<MarketTitleResponse> response) {
                if (response.code() != 404 && response.code() != 400 && response.body() != null) {
                    market = response.body().market;
                    addressShop.setText(response.body().market);
                    addressShop.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NotNull Call<MarketTitleResponse> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void hideAdvertisementElements() {
        advertisement = null;
        statusAdvert.setVisibility(View.VISIBLE);
        buttonStopAdvert.setVisibility(View.GONE);
        arrayAdapter.notifyDataSetChanged();
        buttonTakenProducts.setVisibility(View.GONE);
        numberAdvertisement.setVisibility(View.GONE);
        textNewAdvert.setVisibility(View.GONE);
        getterAdvertLayout.setVisibility(View.GONE);
    }

    private void showAdvertisementElements(Advertisement advert) {
        titleAdvert.setText(advert.getTitle());
        statusAdvert.setVisibility(View.GONE);
        buttonStopAdvert.setVisibility(View.VISIBLE);
        arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.item_getter_product_name, advert.getListTitleProducts());
        listViewProducts.setAdapter(arrayAdapter);
        getterAdvertLayout.setVisibility(View.VISIBLE);
        advertisement = advert;

        if (advert.getGettingProductID() != null && advert.getGettingProductID().length() > 0) {
            buttonTakenProducts.setVisibility(View.VISIBLE);
            numberAdvertisement.setVisibility(View.VISIBLE);
            textNewAdvert.setVisibility(View.VISIBLE);
            numberAdvertisement.setText(advert.getGettingProductID());
        }
    }
}