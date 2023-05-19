package com.example.nofoodsharingproject.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.nofoodsharingproject.adapters.GetterNotificationsAdapter;
import com.example.nofoodsharingproject.databinding.FragmentGetterNotifyBinding;
import com.example.nofoodsharingproject.models.LoaderStatus;
import com.example.nofoodsharingproject.utils.DefineUser;
import com.example.nofoodsharingproject.view_models.NotificationsViewModel;
import org.jetbrains.annotations.NotNull;

public class NotifyFragment extends Fragment {
    private NotificationsViewModel viewModel;
    private FragmentGetterNotifyBinding binding;
    private DefineUser defineUser;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineUser = new DefineUser<>(requireActivity());
    }
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGetterNotifyBinding.inflate(inflater);


        GetterNotificationsAdapter getterNotificationsAdapter = new GetterNotificationsAdapter(getContext());
        binding.notifyRecycler.setAdapter(getterNotificationsAdapter);

        viewModel = new ViewModelProvider(
                requireActivity(),
                (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(NotificationsViewModel.class);

        Pair<String, Boolean> userType = defineUser.getTypeUser();

        viewModel.getAllNotifications(userType.first, userType.second ? "getter" : "setter").observe(requireActivity(), getterNotificationsAdapter::updateNotifications);
        viewModel.getLoaderStatus().observe(requireActivity(), this::renderStatus);

        binding.getterNotifySwiper.setOnRefreshListener(() -> {
            viewModel.getAllNotifications(userType.first, userType.second ? "getter" : "setter").observe(requireActivity(), getterNotificationsAdapter::updateNotifications);
            viewModel.getLoaderStatus().observe(requireActivity(), this::renderStatus);
        });

        return binding.getRoot();
    }

    private void renderStatus(LoaderStatus loaderStatus) {
        switch (loaderStatus.getStatus()) {
            case LOADING:
                binding.notifyRecycler.setVisibility(View.INVISIBLE);
                binding.getterNotifyLoader.setVisibility(View.VISIBLE);
                break;
            case LOADED:
                binding.notifyRecycler.setVisibility(View.VISIBLE);
                binding.getterNotifyLoader.setVisibility(View.INVISIBLE);
                binding.getterNotifySwiper.setRefreshing(false);
                break;
            case FAILURE:
                binding.notifyRecycler.setVisibility(View.INVISIBLE);
                binding.getterNotifyLoader.setVisibility(View.INVISIBLE);
                binding.getterNotifySwiper.setRefreshing(false);
                break;
        }
    }

}