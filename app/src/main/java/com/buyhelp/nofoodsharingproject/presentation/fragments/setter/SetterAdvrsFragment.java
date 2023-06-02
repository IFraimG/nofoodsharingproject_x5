package com.buyhelp.nofoodsharingproject.presentation.fragments.setter;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.buyhelp.nofoodsharingproject.data.api.map.MapRepository;
import com.buyhelp.nofoodsharingproject.presentation.ApplicationCore;
import com.buyhelp.nofoodsharingproject.presentation.adapters.SetterAdvertListAdapter;
import com.buyhelp.nofoodsharingproject.databinding.FragmentSetterAdvrsBinding;
import com.buyhelp.nofoodsharingproject.data.models.LoaderStatus;
import com.buyhelp.nofoodsharingproject.data.models.Setter;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.buyhelp.nofoodsharingproject.presentation.factories.setters.SetterAdvrsFactory;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.advertisements.AdvertisementListViewModel;
import org.jetbrains.annotations.NotNull;

public class SetterAdvrsFragment extends Fragment {
    private AdvertisementListViewModel viewModel;
    private FragmentSetterAdvrsBinding binding;
    private DefineUser<Setter> defineUser;
    private AdvertsRepository advertsRepository;
    private MapRepository mapRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationCore app = (ApplicationCore) requireActivity().getApplication();
        advertsRepository = app.getAppComponent().getAdvertsRepository();
        mapRepository = app.getAppComponent().getMapRepository();

        defineUser = new DefineUser<>(requireActivity());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetterAdvrsBinding.inflate(inflater);
        RecyclerView recyclerView = binding.setterListAdvert;

        SetterAdvertListAdapter setterAdvertListAdapter = new SetterAdvertListAdapter(getContext());
        recyclerView.setAdapter(setterAdvertListAdapter);
        recyclerView.setVerticalScrollBarEnabled(true);

        viewModel = new ViewModelProvider(requireActivity(),
                new SetterAdvrsFactory(requireActivity().getApplication(), advertsRepository, mapRepository))
                .get(AdvertisementListViewModel.class);

        viewModel.getAllAdverts(defineUser.getUser().getX5_Id()).observe(requireActivity(), setterAdvertListAdapter::updateAdverts);
        viewModel.getLoaderStatus().observe(requireActivity(), this::renderStatus);

        binding.setterAdvertSwiper.setOnRefreshListener(() -> {
            viewModel.getAllAdverts(defineUser.getUser().getX5_Id()).observe(requireActivity(), setterAdvertListAdapter::updateAdverts);
            binding.setterAdvertSwiper.setRefreshing(false);
        });

        binding.setterAdvertFaq.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_setterAdvrsF_to_faqFragment);
        });

        initFilter();

        return binding.getRoot();
    }

    private void renderStatus(LoaderStatus loaderStatus) {
        switch (loaderStatus.getStatus()) {
            case LOADING:
                binding.setterListAdvert.setVisibility(View.INVISIBLE);
                binding.setterLoader.setVisibility(View.VISIBLE);
                break;
            case LOADED:
                binding.setterListAdvert.setVisibility(View.VISIBLE);
                binding.setterLoader.setVisibility(View.INVISIBLE);
                binding.setterAdvertSwiper.setRefreshing(false);
                break;
            case FAILURE:
                binding.setterListAdvert.setVisibility(View.INVISIBLE);
                binding.setterLoader.setVisibility(View.INVISIBLE);
                binding.setterAdvertSwiper.setRefreshing(false);
                break;
        }
    }

    private void initFilter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.market_item, viewModel.getFullListMarkets());
        adapter.setDropDownViewResource(R.layout.map_dropdown_text);
        binding.setterAdvertFilter.setAdapter(adapter);
        viewModel.getMarket().observe(requireActivity(), s -> {
            int pos = viewModel.getActiveMarketPosition();
            if (pos != -1) binding.setterAdvertFilter.setSelection(pos);
        });

        binding.setterAdvertFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.setActiveMarket(position);
                binding.setterAdvertFilter.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}