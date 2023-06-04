/**
 * Класс {@code GiverAdvrsFragment} - фрагмент главной страницы отдающего со списком объявлений
 * @author Кулагин Александр
 */

package com.buyhelp.nofoodsharingproject.presentation.fragments.giver;

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
import com.buyhelp.nofoodsharingproject.databinding.FragmentGiverAdvrsBinding;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.buyhelp.nofoodsharingproject.presentation.ApplicationCore;
import com.buyhelp.nofoodsharingproject.presentation.adapters.GiverAdvertListAdapter;
import com.buyhelp.nofoodsharingproject.data.models.LoaderStatus;
import com.buyhelp.nofoodsharingproject.presentation.factories.giver.GiverAdvrsFactory;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.advertisements.AdvertisementListViewModel;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;


public class GiverAdvrsFragment extends Fragment {
    private AdvertisementListViewModel viewModel;
    private FragmentGiverAdvrsBinding binding;
    private WeakReference<FragmentGiverAdvrsBinding> mBinding;
    private DefineUser defineUser;
    private AdvertsRepository advertsRepository;
    private MapRepository mapRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationCore app = (ApplicationCore) requireActivity().getApplication();
        advertsRepository = app.getAppComponent().getAdvertsRepository();
        mapRepository = app.getAppComponent().getMapRepository();
        defineUser = app.getHelpersComponent().getDefineUser();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGiverAdvrsBinding.inflate(inflater);
        mBinding = new WeakReference<>(binding);

        RecyclerView recyclerView = binding.giverListAdvert;

        GiverAdvertListAdapter giverAdvertListAdapter = new GiverAdvertListAdapter(getContext());
        recyclerView.setAdapter(giverAdvertListAdapter);
        recyclerView.setVerticalScrollBarEnabled(true);

        viewModel = new ViewModelProvider(requireActivity(),
                new GiverAdvrsFactory(requireActivity().getApplication(), advertsRepository, mapRepository))
                .get(AdvertisementListViewModel.class);

        viewModel.getAllAdverts(defineUser.getUser().getX5_Id()).observe(requireActivity(), giverAdvertListAdapter::updateAdverts);
        viewModel.getLoaderStatus().observe(requireActivity(), this::renderStatus);

        binding.giverAdvertSwiper.setOnRefreshListener(() -> {
            viewModel.getAllAdverts(defineUser.getUser().getX5_Id()).observe(requireActivity(), giverAdvertListAdapter::updateAdverts);
            binding.giverAdvertSwiper.setRefreshing(false);
        });

        binding.giverAdvertFaq.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_giverAdvrsF_to_faqFragment));

        initFilter();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding.clear();
    }

    private void renderStatus(LoaderStatus loaderStatus) {
        switch (loaderStatus.getStatus()) {
            case LOADING -> {
                binding.giverListAdvert.setVisibility(View.INVISIBLE);
                binding.giverLoader.setVisibility(View.VISIBLE);
            }
            case LOADED -> {
                binding.giverListAdvert.setVisibility(View.VISIBLE);
                binding.giverLoader.setVisibility(View.INVISIBLE);
                binding.giverAdvertSwiper.setRefreshing(false);
            }
            case FAILURE -> {
                binding.giverListAdvert.setVisibility(View.INVISIBLE);
                binding.giverLoader.setVisibility(View.INVISIBLE);
                binding.giverAdvertSwiper.setRefreshing(false);
            }
        }
    }

    private void initFilter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.market_item, viewModel.getFullListMarkets());
        adapter.setDropDownViewResource(R.layout.map_dropdown_text);
        binding.giverAdvertFilter.setAdapter(adapter);
        viewModel.getMarket().observe(requireActivity(), s -> {
            int pos = viewModel.getActiveMarketPosition();
            if (pos != -1) binding.giverAdvertFilter.setSelection(pos);
        });

        binding.giverAdvertFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.setActiveMarket(position);
                binding.giverAdvertFilter.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}