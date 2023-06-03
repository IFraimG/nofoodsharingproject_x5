package com.buyhelp.nofoodsharingproject.presentation.fragments.getter;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.buyhelp.nofoodsharingproject.data.api.map.MapRepository;
import com.buyhelp.nofoodsharingproject.data.api.notifications.NotificationRepository;
import com.buyhelp.nofoodsharingproject.data.api.setter.SetterRepository;
import com.buyhelp.nofoodsharingproject.databinding.FragmentGetterAdvrsBinding;
import com.buyhelp.nofoodsharingproject.data.models.Advertisement;

import com.buyhelp.nofoodsharingproject.data.models.Getter;
import com.buyhelp.nofoodsharingproject.data.models.LoaderStatus;
import com.buyhelp.nofoodsharingproject.domain.utils.DateNowChecker;
import com.buyhelp.nofoodsharingproject.domain.utils.DateNowCheckerOld;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.buyhelp.nofoodsharingproject.presentation.ApplicationCore;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.advertisements.AdvertisementOneViewModel;
import com.buyhelp.nofoodsharingproject.presentation.factories.getters.GetterAdvrsFactory;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

public class GetterAdvrsFragment extends Fragment {
    private FragmentGetterAdvrsBinding binding;
    private WeakReference<FragmentGetterAdvrsBinding> mBinding;
    private AdvertisementOneViewModel viewModel;
    private ArrayAdapter<String> arrayAdapter;
    private DefineUser defineUser;
    private NotificationRepository notificationRepository;
    private SetterRepository setterRepository;
    private AdvertsRepository advertsRepository;
    private MapRepository mapRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationCore app = (ApplicationCore) requireActivity().getApplication();
        notificationRepository = app.getAppComponent().getNotificationRepository();
        setterRepository = app.getAppComponent().getSetterRepository();
        advertsRepository = app.getAppComponent().getAdvertsRepository();
        mapRepository = app.getAppComponent().getMapRepository();
        defineUser = app.getHelpersComponent().getDefineUser();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentGetterAdvrsBinding.inflate(inflater);
        mBinding = new WeakReference<>(binding);

        binding.textNumberOfAdvert.setVisibility(View.GONE);
        binding.stopAdvert.setVisibility(View.GONE);
        binding.getterAdvertLayout.setVisibility(View.GONE);
        viewModel = new ViewModelProvider(requireActivity(),
                new GetterAdvrsFactory(requireActivity().getApplication(), notificationRepository, setterRepository, advertsRepository, mapRepository))
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding.clear();
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
        binding.createNewRequest.setOnClickListener(v -> {
            if (viewModel.getMarket() == null || viewModel.getMarket().length() == 0) Toast.makeText(getContext(), getString(R.string.pin_market), Toast.LENGTH_LONG).show();
            else {
                Navigation.findNavController(v).navigate(R.id.action_getterAdvrsF_to_getterNewAdvertFragment);
            }
        });

        binding.stopAdvert.setOnClickListener(View -> viewModel.removeAdvertisement());
        binding.pickUpOrder.setOnClickListener(View -> viewModel.takeProducts(defineUser.getUser().getX5_Id()));

        binding.getterAdvertFaq.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_getterAdvrsF_to_faqFragment);
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
        binding.getterBlockTimer.setVisibility(View.GONE);
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
            binding.getterBlockTimer.setVisibility(View.VISIBLE);
            binding.timerToAdvert.setText(viewModel.getTimer());
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