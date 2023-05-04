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
import com.example.nofoodsharingproject.utils.LoaderStatus;
import com.example.nofoodsharingproject.view_models.AdvertisementList_ViewModel;

import java.util.List;

public class SetterAdvrs_Fragment extends Fragment {
    AdvertisementList_ViewModel viewModel;
    private FragmentSetterAdvrsBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_setter_advrs, container, false);
        binding = FragmentSetterAdvrsBinding.inflate(inflater);
        RecyclerView recyclerView = binding.setterListAdvert;
        SetterAdvertListAdapter setterAdvertListAdapter = new SetterAdvertListAdapter(getContext());
        recyclerView.setAdapter(setterAdvertListAdapter);
        recyclerView.setVerticalScrollBarEnabled(true);

        viewModel = new ViewModelProvider(getActivity(),
                (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()))
                .get(AdvertisementList_ViewModel.class);

        viewModel.getAllAdverts().observe(getActivity(), new Observer<List<Advertisement>>() {
            @Override
            public void onChanged(List<Advertisement> advertisements) {
                setterAdvertListAdapter.updateAdverts(advertisements);
            }
        });
        return binding.getRoot();
    }

    private void renderStatus(LoaderStatus loaderStatus) {
        // доделать
        switch (loaderStatus) {
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