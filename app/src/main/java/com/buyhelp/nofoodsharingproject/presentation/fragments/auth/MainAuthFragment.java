/**
 * Класс {@code MainAuthFragment} - фрагмент общей страницы авторизации пользователя
 * @author Кулагин Александр
 */

package com.buyhelp.nofoodsharingproject.presentation.fragments.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.databinding.FragmentMainAuthBinding;
import com.buyhelp.nofoodsharingproject.presentation.fragments.SecretFragment;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class MainAuthFragment extends Fragment {
    private FragmentMainAuthBinding binding;
    private WeakReference<FragmentMainAuthBinding> mBinding;
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
        mBinding = new WeakReference<>(binding);

        binding.secretClickableImage.setOnClickListener(View -> secretClickerHandler());

        binding.mainAuthBtnGiver.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_mainAuthF_to_giverAuthF));
        binding.mainAuthBtnNeedy.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_mainAuthF_to_needyAuthF));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mBinding.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (future != null) {
            future.cancel(true);
            future = null;
        }

        if (executor != null) {
            executor.shutdown();
            executor = null;
        }

        if (toast != null) {
            toast.cancel();
            toast = null;
        }
    }

    private void secretClickerHandler() {
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