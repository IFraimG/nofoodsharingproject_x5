/**
 * Класс {@code MainAuthFragment} - фрагмент общей страницы авторизации пользователя
 * @author Кулагин Александр
 */

package com.buyhelp.nofoodsharingproject.presentation.fragments.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.databinding.FragmentMainAuthBinding;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;


public class MainAuthFragment extends Fragment {
    private WeakReference<FragmentMainAuthBinding> mBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentMainAuthBinding binding = FragmentMainAuthBinding.inflate(inflater);
        mBinding = new WeakReference<>(binding);

        binding.mainAuthBtnGiver.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_mainAuthF_to_giverAuthF));
        binding.mainAuthBtnNeedy.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_mainAuthF_to_needyAuthF));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mBinding.clear();
    }
}