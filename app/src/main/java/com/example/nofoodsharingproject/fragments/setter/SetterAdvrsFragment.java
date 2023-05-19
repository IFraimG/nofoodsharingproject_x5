package com.example.nofoodsharingproject.fragments.setter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.activities.FaqActivity;
import com.example.nofoodsharingproject.adapters.SetterAdvertListAdapter;
import com.example.nofoodsharingproject.databinding.FragmentSetterAdvrsBinding;
import com.example.nofoodsharingproject.models.LoaderStatus;
import com.example.nofoodsharingproject.view_models.AdvertisementListViewModel;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class SetterAdvrsFragment extends Fragment {
    private AdvertisementListViewModel viewModel;
    private FragmentSetterAdvrsBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetterAdvrsBinding.inflate(inflater);
        RecyclerView recyclerView = binding.setterListAdvert;

        SetterAdvertListAdapter setterAdvertListAdapter = new SetterAdvertListAdapter(getContext());
        recyclerView.setAdapter(setterAdvertListAdapter);
        recyclerView.setVerticalScrollBarEnabled(true);

        viewModel = new ViewModelProvider(requireActivity(),
                (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(AdvertisementListViewModel.class);

        viewModel.getAllAdverts(getUserID()).observe(requireActivity(), setterAdvertListAdapter::updateAdverts);
        viewModel.getLoaderStatus().observe(requireActivity(), this::renderStatus);

        binding.setterAdvertSwiper.setOnRefreshListener(() -> {
            viewModel.getAllAdverts(getUserID()).observe(requireActivity(), setterAdvertListAdapter::updateAdverts);
            binding.setterAdvertSwiper.setRefreshing(false);
        });

        binding.setterAdvertFaq.setOnClickListener(View -> {
            Intent intent = new Intent(getContext(), FaqActivity.class);
            startActivity(intent);
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

    private String getUserID() {
        try {
            MasterKey masterKey = new MasterKey.Builder(requireActivity().getApplicationContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(requireActivity().getApplicationContext(), "user", masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

            return sharedPreferences.getString("X5_id", "");
        } catch (GeneralSecurityException | IOException err) {
            Toast.makeText(getContext(), R.string.unvisinle_error, Toast.LENGTH_SHORT).show();
            Log.e("esp_error", err.toString());
        }
        return "";
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