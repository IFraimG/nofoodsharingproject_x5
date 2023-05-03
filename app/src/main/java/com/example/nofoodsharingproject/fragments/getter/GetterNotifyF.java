package com.example.nofoodsharingproject.fragments.getter;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.adapters.GetterNotificationsAdapter;
import com.example.nofoodsharingproject.adapters.SetterNotificationsAdapter;
import com.example.nofoodsharingproject.models.Notification;
import com.example.nofoodsharingproject.view_models.NotificationsViewModel;

import java.util.List;

public class GetterNotifyF extends Fragment {
    NotificationsViewModel viewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_getter_notify, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler);


        GetterNotificationsAdapter getterNotificationsAdapter = new GetterNotificationsAdapter(getContext());
        recyclerView.setAdapter(getterNotificationsAdapter);

        viewModel = new ViewModelProvider(
                getActivity(),
                (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()))
                .get(NotificationsViewModel.class);
        // для сохранения уведомлений придется sqllite использовать

        viewModel.getAllNotifications().observe(getActivity(), new Observer<List<Notification>>() {
            @Override
            public void onChanged(List<Notification> notifications) {
                getterNotificationsAdapter.updateNotifications(notifications);
            }
        });

        return view;
    }
}