package com.example.nofoodsharingproject.fragments.setter;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nofoodsharingproject.activities.Faq_Activity;
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

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView linkFAQ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetterAdvrsBinding.inflate(inflater);
        RecyclerView recyclerView = binding.setterListAdvert;
        swipeRefreshLayout = binding.setterAdvertSwiper;
        linkFAQ = binding.setterAdvertFaq;

        SetterAdvertListAdapter setterAdvertListAdapter = new SetterAdvertListAdapter(getContext());
        recyclerView.setAdapter(setterAdvertListAdapter);
        recyclerView.setVerticalScrollBarEnabled(true);

        viewModel = new ViewModelProvider(requireActivity(),
                (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(AdvertisementList_ViewModel.class);

        viewModel.getAllAdverts().observe(requireActivity(), setterAdvertListAdapter::updateAdverts);
        viewModel.getLoaderStatus().observe(requireActivity(), this::renderStatus);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.getAllAdverts().observe(requireActivity(), setterAdvertListAdapter::updateAdverts);
            swipeRefreshLayout.setRefreshing(false);
        });

        linkFAQ.setOnClickListener(View -> {
            Intent intent = new Intent(getContext(), Faq_Activity.class);
            startActivity(intent);
        });

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
                swipeRefreshLayout.setRefreshing(false);
                break;
            case FAILURE:
                binding.setterListAdvert.setVisibility(View.INVISIBLE);
                binding.setterLoader.setVisibility(View.INVISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                break;
        }
    }
}