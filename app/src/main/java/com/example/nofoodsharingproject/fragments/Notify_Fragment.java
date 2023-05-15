package com.example.nofoodsharingproject.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.adapters.GetterNotificationsAdapter;
import com.example.nofoodsharingproject.databinding.FragmentGetterNotifyBinding;
import com.example.nofoodsharingproject.models.LoaderStatus;
import com.example.nofoodsharingproject.view_models.Notifications_ViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class Notify_Fragment extends Fragment {
    private Notifications_ViewModel viewModel;
    private FragmentGetterNotifyBinding binding;
    private ProgressBar loader;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGetterNotifyBinding.inflate(inflater);

        recyclerView = binding.notifyRecycler;
        loader = binding.getterNotifyLoader;
        swipeRefreshLayout = binding.getterNotifySwiper;

        GetterNotificationsAdapter getterNotificationsAdapter = new GetterNotificationsAdapter(getContext());
        recyclerView.setAdapter(getterNotificationsAdapter);


        viewModel = new ViewModelProvider(
                requireActivity(),
                (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(Notifications_ViewModel.class);

        viewModel.getAllNotifications(defineTypeUser().first, defineTypeUser().second ? "getter" : "setter").observe(requireActivity(), getterNotificationsAdapter::updateNotifications);
        viewModel.getLoaderStatus().observe(requireActivity(), this::renderStatus);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.getAllNotifications(defineTypeUser().first, defineTypeUser().second ? "getter" : "setter").observe(requireActivity(), getterNotificationsAdapter::updateNotifications);
            viewModel.getLoaderStatus().observe(requireActivity(), this::renderStatus);
        });

        return binding.getRoot();
    }

    private Pair<String, Boolean> defineTypeUser() {
        try {
            MasterKey masterKey = new MasterKey.Builder(requireActivity().getApplicationContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(requireActivity().getApplicationContext(), "user", masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

            String userID = sharedPreferences.getString("X5_id", "");
            boolean isUser = sharedPreferences.getBoolean("isGetter", false);
            return new Pair<>(userID, isUser);
        } catch (GeneralSecurityException | IOException err) {
            Toast.makeText(getContext(), R.string.unvisinle_error, Toast.LENGTH_SHORT).show();
            Log.e("esp_error", err.toString());
        }
        return new Pair<>("", false);
    }

    private void renderStatus(LoaderStatus loaderStatus) {
        switch (loaderStatus.getStatus()) {
            case LOADING:
                recyclerView.setVisibility(View.INVISIBLE);
                loader.setVisibility(View.VISIBLE);
                break;
            case LOADED:
                recyclerView.setVisibility(View.VISIBLE);
                loader.setVisibility(View.INVISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                break;
            case FAILURE:
                recyclerView.setVisibility(View.INVISIBLE);
                loader.setVisibility(View.INVISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                break;
        }
    }

}