package com.example.nofoodsharingproject.fragments.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.databinding.FragmentMainAuthBinding;

import org.jetbrains.annotations.NotNull;


public class MainAuthFragment extends Fragment {

    private FragmentMainAuthBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainAuthBinding.inflate(inflater);

        Button btnGetter = binding.mainAuthBtnGetter;
        Button btnSetter = binding.mainAuthBtnSetter;

        btnSetter.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_mainAuthF_to_setterAuthF));
        btnGetter.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_mainAuthF_to_getterAuthF));

        return binding.getRoot();
    }


}