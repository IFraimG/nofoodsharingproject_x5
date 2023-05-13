package com.example.nofoodsharingproject.fragments.setter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nofoodsharingproject.adapters.SetterAdvertListAdapter;
import com.example.nofoodsharingproject.databinding.FragmentSetterAdvrsBinding;
import com.example.nofoodsharingproject.models.Advertisement;
import com.example.nofoodsharingproject.models.LoaderStatus;
import com.example.nofoodsharingproject.view_models.AdvertisementList_ViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SetterAdvrs_Fragment extends Fragment {
    private AdvertisementList_ViewModel viewModel;
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
                .get(AdvertisementList_ViewModel.class);

        viewModel.getAllAdverts().observe(requireActivity(), setterAdvertListAdapter::updateAdverts);
        viewModel.getLoaderStatus().observe(requireActivity(), this::renderStatus);

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
                break;
            case FAILURE:
                binding.setterListAdvert.setVisibility(View.INVISIBLE);
                binding.setterLoader.setVisibility(View.INVISIBLE);
                break;
        }
    }
}