package com.example.nofoodsharingproject.fragments.setter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.adapters.SetterAdvertListAdapter;
import com.example.nofoodsharingproject.view_models.AdvertisementListViewModel;

public class SetterAdvrsF extends Fragment {
    AdvertisementListViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setter_advrs, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.setter_list_advert);
        SetterAdvertListAdapter setterAdvertListAdapter = new SetterAdvertListAdapter(getContext());
        recyclerView.setAdapter(setterAdvertListAdapter);
        recyclerView.setVerticalScrollBarEnabled(true);
//
//        viewModel = new ViewModelProvider(this,
//                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()))
//                .get(AdvertisementListViewModel.class);
        return view;
    }
}