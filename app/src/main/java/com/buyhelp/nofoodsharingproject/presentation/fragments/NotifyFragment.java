package com.buyhelp.nofoodsharingproject.presentation.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buyhelp.nofoodsharingproject.data.api.notifications.NotificationRepository;
import com.buyhelp.nofoodsharingproject.presentation.ApplicationCore;
import com.buyhelp.nofoodsharingproject.presentation.adapters.GetterNotificationsAdapter;
import com.buyhelp.nofoodsharingproject.databinding.FragmentGetterNotifyBinding;
import com.buyhelp.nofoodsharingproject.data.models.LoaderStatus;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.buyhelp.nofoodsharingproject.presentation.factories.NotificationFactory;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.NotificationsViewModel;
import org.jetbrains.annotations.NotNull;

public class NotifyFragment extends Fragment {
    private NotificationsViewModel viewModel;
    private FragmentGetterNotifyBinding binding;
    private DefineUser defineUser;
    private NotificationRepository notificationRepository;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationCore app = (ApplicationCore) requireActivity().getApplication();
        notificationRepository = app.getAppComponent().getNotificationRepository();

        defineUser = new DefineUser<>(requireActivity());
    }
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGetterNotifyBinding.inflate(inflater);

        GetterNotificationsAdapter getterNotificationsAdapter = new GetterNotificationsAdapter(getContext());
        binding.notifyRecycler.setAdapter(getterNotificationsAdapter);

        viewModel = new ViewModelProvider(
                requireActivity(),
                new NotificationFactory(requireActivity().getApplication(), notificationRepository))
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