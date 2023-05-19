package com.example.nofoodsharingproject.fragments.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.databinding.FragmentMainAuthBinding;
import com.example.nofoodsharingproject.fragments.SecretFragment;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class MainAuthFragment extends Fragment {
    private FragmentMainAuthBinding binding;
    private int countClick = 0;
    private ScheduledFuture<String> future;
    private ScheduledExecutorService executor;
    private Toast toast;
    private boolean isOpen = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainAuthBinding.inflate(inflater);

        Button btnGetter = binding.mainAuthBtnGetter;
        Button btnSetter = binding.mainAuthBtnSetter;
        ImageView imageSecret = binding.secretClickableImage;

        imageSecret.setOnClickListener(View -> {
            if (!isOpen) {
                countClick += 1;
                if (future != null && future.isDone() && executor != null) {
                    future.cancel(true);
                    executor.shutdown();
                    if (toast != null) toast.cancel();
                }

                if (countClick > 3) {
                    isOpen = true;
                    openWindow();
                } else {
                    toast = Toast.makeText(getContext(), countClick + " нажатия", Toast.LENGTH_SHORT);
                    toast.show();
                    startTimer();
                }
            } else openWindow();
        });

        btnSetter.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_mainAuthF_to_setterAuthF));
        btnGetter.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_mainAuthF_to_getterAuthF));

        return binding.getRoot();
    }

    private void startTimer() {
        executor = Executors.newScheduledThreadPool(1);
        Callable<String> task = () -> {
            if (countClick < 4) countClick = 0;
            return "";
        };

        future = executor.schedule(task, 1, TimeUnit.SECONDS);
        executor.shutdown();
    }

    private void openWindow() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        SecretFragment secretFragment = new SecretFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(secretFragment, "secret");
        fragmentTransaction.commit();
    }
}