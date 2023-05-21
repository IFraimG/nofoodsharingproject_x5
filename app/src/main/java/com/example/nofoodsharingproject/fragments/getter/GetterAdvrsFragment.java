package com.example.nofoodsharingproject.fragments.getter;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.activities.FaqActivity;
import com.example.nofoodsharingproject.activities.GetterNewAdvertActivity;
import com.example.nofoodsharingproject.databinding.FragmentGetterAdvrsBinding;
import com.example.nofoodsharingproject.models.Advertisement;

import com.example.nofoodsharingproject.models.Getter;
import com.example.nofoodsharingproject.models.LoaderStatus;
import com.example.nofoodsharingproject.utils.DateNowChecker;
import com.example.nofoodsharingproject.utils.DateNowCheckerOld;
import com.example.nofoodsharingproject.utils.DefineUser;
import com.example.nofoodsharingproject.view_models.AdvertisementOneViewModel;

import org.jetbrains.annotations.NotNull;

public class GetterAdvrsFragment extends Fragment {
    private FragmentGetterAdvrsBinding binding;
    private AdvertisementOneViewModel viewModel;
    private ArrayAdapter<String> arrayAdapter;
    private DefineUser<Getter> defineUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        defineUser = new DefineUser<>(requireActivity());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentGetterAdvrsBinding.inflate(inflater);

        binding.textNumberOfAdvert.setVisibility(View.GONE);
        binding.stopAdvert.setVisibility(View.GONE);
        binding.getterAdvertLayout.setVisibility(View.GONE);

        viewModel = new ViewModelProvider(requireActivity(),
                (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(AdvertisementOneViewModel.class);

        viewModel.getAddress(defineUser.getUser().getX5_Id(), defineUser.getTypeUser().second).observe(requireActivity(), market -> {
            binding.addressShop.setText(market);
            binding.addressShop.setVisibility(View.VISIBLE);
        });

        viewModel.getLoaderStatus().observe(requireActivity(), this::renderStatus);
        viewModel.getLoaderRemoveStatus().observe(requireActivity(), this::renderStatusRemove);
        viewModel.getLoaderNotificationStatus().observe(requireActivity(), this::renderStatusNotification);

        getAdvertisement();
        initHandlers();

        return binding.getRoot();
    }

    private void renderStatus(LoaderStatus loaderStatus) {
        switch (loaderStatus.getStatus()) {
            case LOADING:
                binding.getterAdvertSwiper.setRefreshing(true);
                hideAdvertisementElements();
                break;
            case FAILURE:
                hideAdvertisementElements();
                break;
        }
    }

    private void renderStatusRemove(LoaderStatus loaderStatus) {
        switch (loaderStatus.getStatus()) {
            case LOADING:
                binding.getterAdvertSwiper.setRefreshing(true);
                break;
            case LOADED:
                hideAdvertisementElements();
                break;
            case FAILURE:
                binding.getterAdvertSwiper.setRefreshing(false);
                break;
        }
    }

    private void renderStatusNotification(LoaderStatus loaderStatus) {
        if (loaderStatus.getStatus() == LoaderStatus.Status.LOADED) hideAdvertisementElements();
    }


    private void initHandlers() {
        binding.createNewRequest.setOnClickListener(View -> {
            if (viewModel.getMarket() == null || viewModel.getMarket().length() == 0) Toast.makeText(getContext(), getString(R.string.pin_market), Toast.LENGTH_LONG).show();
            else startActivity(new Intent(getActivity(), GetterNewAdvertActivity.class));
        });

        binding.stopAdvert.setOnClickListener(View -> viewModel.removeAdvertisement());
        binding.pickUpOrder.setOnClickListener(View -> viewModel.takeProducts(defineUser.getUser().getX5_Id()));

        binding.getterAdvertFaq.setOnClickListener(View -> {
            Intent intent = new Intent(getContext(), FaqActivity.class);
            startActivity(intent);
        });

        binding.getterAdvertSwiper.setOnRefreshListener(this::getAdvertisement);
    }

    private void hideAdvertisementElements() {
        binding.getterAdvertStatus.setVisibility(View.VISIBLE);
        showCreateButton();
        binding.stopAdvert.setVisibility(View.GONE);
        if (arrayAdapter != null) {
            arrayAdapter.clear();
            arrayAdapter.notifyDataSetChanged();
        }
        binding.pickUpOrder.setVisibility(View.GONE);
        binding.numberOfAdvertisement.setVisibility(View.GONE);
        binding.textNumberOfAdvert.setVisibility(View.GONE);
        binding.getterAdvertLayout.setVisibility(View.GONE);
        binding.getterAdvertSwiper.setRefreshing(false);
    }

    private void showAdvertisementElements(Advertisement advert) {
        binding.getterAdvertTitleProducts.setText(advert.getTitle());
        binding.getterAdvertStatus.setVisibility(View.GONE);
        binding.createNewRequest.setVisibility(View.GONE);
        binding.stopAdvert.setVisibility(View.VISIBLE);
        binding.getterAdvertLayout.setVisibility(View.VISIBLE);

        if (advert.getGettingProductID() != null && advert.getGettingProductID().length() > 0) {
            binding.pickUpOrder.setVisibility(View.VISIBLE);
            binding.numberOfAdvertisement.setVisibility(View.VISIBLE);
            binding.textNumberOfAdvert.setVisibility(View.VISIBLE);
            binding.numberOfAdvertisement.setText(advert.getGettingProductID());
        }
        binding.getterAdvertSwiper.setRefreshing(false);
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

    private void getAdvertisement() {
        viewModel.getAdvert(defineUser.getUser().getX5_Id()).observe(requireActivity(), advert -> {
            if (advert != null) {
                binding.getterAdvertTitleProducts.setText(advert.getTitle());
                arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.item_getter_product_name, advert.getListTitleProducts());
                binding.getterAdvertProducts.setAdapter(arrayAdapter);
                showAdvertisementElements(advert);
            }
        });
    }
}