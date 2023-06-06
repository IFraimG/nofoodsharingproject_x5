/**
 * Класс {@code GiverHelpFinishFragment} - фрагмент итоговой страницы при покупки продуктов
 * @author Кулагин Александр
 */

package com.buyhelp.nofoodsharingproject.presentation.fragments.giver;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.buyhelp.nofoodsharingproject.databinding.FragmentGiverFinishHelpBinding;
import com.buyhelp.nofoodsharingproject.presentation.ApplicationCore;
import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.presentation.activities.GiverActivity;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.lang.ref.WeakReference;

import io.socket.client.Socket;


public class GiverHelpFinishFragment extends Fragment {
    private WeakReference<FragmentGiverFinishHelpBinding> mBinding;
    private Socket socket;
    private DefineUser defineUser;
    private String needyID = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationCore app = (ApplicationCore) requireActivity().getApplication();
        socket = app.getSocket();
        socket.connect();

        defineUser = app.getHelpersComponent().getDefineUser();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentGiverFinishHelpBinding binding = FragmentGiverFinishHelpBinding.inflate(inflater);
        mBinding = new WeakReference<>(binding);

        if (getArguments() != null) needyID = getArguments().getString("needyID");
        String generateID = getArguments().getString("gettingProductID");

        if (generateID != null) binding.giverFinishCode.setText(generateID);

        binding.giverFinishGotovk.setOnClickListener(View -> vkLoad());
        binding.giverFinishReturn.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_giverHelpFinishFragment_to_giverAdvrsF));

        binding.giverFinishOpenChat.setOnClickListener(this::createChat);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof GiverActivity) {
            ((GiverActivity) requireActivity()).setBottomNavigationVisibility(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() instanceof GiverActivity) {
            ((GiverActivity) requireActivity()).setBottomNavigationVisibility(true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (socket != null) {
            socket.disconnect();
            socket.off("getCreatedChat");
        }
    }

    /**
     * Этот метод перенаправляет пользователя в сервис VK чэкбэк
     */
    private void vkLoad() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.vk_link)));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Snackbar.make(requireContext(), requireView(), getString(R.string.unvisinle_error), Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * Этот метод создает чат с пользователем и открывает страницу со списком чатов
     */
    private void createChat(View v) {
        try {
            JSONArray arr = new JSONArray(new String[]{defineUser.getTypeUser().first, needyID});
            socket.emit("create_chat", arr);
            Navigation.findNavController(v).navigate(R.id.action_giverHelpFinishFragment_to_chatsListFragment);
        } catch (JSONException err) {
            err.printStackTrace();
        }
    }
}
