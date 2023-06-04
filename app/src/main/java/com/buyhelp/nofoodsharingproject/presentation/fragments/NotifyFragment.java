package com.buyhelp.nofoodsharingproject.presentation.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buyhelp.nofoodsharingproject.data.api.notifications.NotificationRepository;
import com.buyhelp.nofoodsharingproject.databinding.FragmentNeedyNotifyBinding;
import com.buyhelp.nofoodsharingproject.presentation.ApplicationCore;
import com.buyhelp.nofoodsharingproject.presentation.adapters.NeedyNotificationsAdapter;
import com.buyhelp.nofoodsharingproject.data.models.LoaderStatus;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.buyhelp.nofoodsharingproject.presentation.factories.NotificationFactory;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.NotificationsViewModel;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

public class NotifyFragment extends Fragment {
    private NotificationsViewModel viewModel;
    private FragmentNeedyNotifyBinding binding;
    private WeakReference<FragmentNeedyNotifyBinding> mBinding;
    private DefineUser defineUser;
    private NotificationRepository notificationRepository;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationCore app = (ApplicationCore) requireActivity().getApplication();
        notificationRepository = app.getAppComponent().getNotificationRepository();
        defineUser = app.getHelpersComponent().getDefineUser();
    }
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNeedyNotifyBinding.inflate(inflater);
        mBinding = new WeakReference<>(binding);

        NeedyNotificationsAdapter needyNotificationsAdapter = new NeedyNotificationsAdapter(getContext());
        binding.notifyRecycler.setAdapter(needyNotificationsAdapter);

        viewModel = new ViewModelProvider(
                requireActivity(),
                new NotificationFactory(requireActivity().getApplication(), notificationRepository))
                .get(NotificationsViewModel.class);

        Pair<String, Boolean> userType = defineUser.getTypeUser();

        viewModel.getAllNotifications(userType.first, userType.second ? "needy" : "giver").observe(requireActivity(), needyNotificationsAdapter::updateNotifications);
        viewModel.getLoaderStatus().observe(requireActivity(), this::renderStatus);

        binding.needyNotifySwiper.setOnRefreshListener(() -> {
            viewModel.getAllNotifications(userType.first, userType.second ? "needy" : "giver").observe(requireActivity(), needyNotificationsAdapter::updateNotifications);
            viewModel.getLoaderStatus().observe(requireActivity(), this::renderStatus);
        });

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
                binding.notifyRecycler.setVisibility(View.INVISIBLE);
                binding.needyNotifyLoader.setVisibility(View.VISIBLE);
                break;
            case LOADED:
                binding.notifyRecycler.setVisibility(View.VISIBLE);
                binding.needyNotifyLoader.setVisibility(View.INVISIBLE);
                binding.needyNotifySwiper.setRefreshing(false);
                break;
            case FAILURE:
                binding.notifyRecycler.setVisibility(View.INVISIBLE);
                binding.needyNotifyLoader.setVisibility(View.INVISIBLE);
                binding.needyNotifySwiper.setRefreshing(false);
                break;
        }
    }

}