package com.example.nofoodsharingproject.fragments.setter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.adapters.SetterNotificationsAdapter;
import com.example.nofoodsharingproject.models.Notification;
import com.example.nofoodsharingproject.view_models.Notifications_ViewModel;

import java.util.List;

public class SetterNotify_Fragment extends Fragment {
    Notifications_ViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setter_notify, container, false);

        SetterNotificationsAdapter setterNotificationsAdapter = new SetterNotificationsAdapter(getContext());
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.setter_list_notify);
        recyclerView.setAdapter(setterNotificationsAdapter);

        viewModel = new ViewModelProvider(
                getActivity(),
                (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()))
                .get(Notifications_ViewModel.class);
        // для сохранения уведомлений придется sqllite использовать

        viewModel.getAllNotifications().observe(getActivity(), new Observer<List<Notification>>() {
            @Override
            public void onChanged(List<Notification> notifications) {
                setterNotificationsAdapter.updateNotifications(notifications);
            }
        });

        return view;
    }
}