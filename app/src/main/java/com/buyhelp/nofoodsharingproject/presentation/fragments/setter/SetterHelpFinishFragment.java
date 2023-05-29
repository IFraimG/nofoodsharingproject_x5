package com.buyhelp.nofoodsharingproject.presentation.fragments.setter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.buyhelp.nofoodsharingproject.presentation.ApplicationCore;
import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.presentation.activities.SetterActivity;
import com.buyhelp.nofoodsharingproject.databinding.FragmentSetterFinishHelpBinding;
import com.buyhelp.nofoodsharingproject.data.models.Setter;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import io.socket.client.Socket;


public class SetterHelpFinishFragment extends Fragment {
    private FragmentSetterFinishHelpBinding binding;
    private Socket socket;
    private DefineUser<Setter> defineUser;
    private String getterID;
    private String generateID;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetterFinishHelpBinding.inflate(inflater);

        ApplicationCore app = (ApplicationCore) requireActivity().getApplication();
        socket = app.getSocket();
        socket.connect();

        getterID = getArguments().getString("getterID");
        generateID = getArguments().getString("gettingProductID");

        if (generateID != null) binding.setterFinishCode.setText(generateID);

        defineUser = new DefineUser<>(requireActivity());

        binding.setterFinishGotovk.setOnClickListener(View -> vkLoad());
        binding.setterFinishReturn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_setterHelpFinishFragment_to_setterAdvrsF);
        });

        binding.setterFinishOpenChat.setOnClickListener(this::createChat);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof SetterActivity) {
            ((SetterActivity) requireActivity()).setBottomNavigationVisibility(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() instanceof SetterActivity) {
            ((SetterActivity) requireActivity()).setBottomNavigationVisibility(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (socket != null) {
            socket.disconnect();
            socket.off("getCreatedChat");
        }
    }

    private void vkLoad() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.vk_link)));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(requireContext(), R.string.unvisinle_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void createChat(View v) {
        try {
            JSONArray arr = new JSONArray(new String[]{defineUser.getTypeUser().first, getterID});
            socket.emit("create_chat", arr);
            Navigation.findNavController(v).navigate(R.id.action_setterHelpFinishFragment_to_chatsListFragment);
        } catch (JSONException err) {
            err.printStackTrace();
        }
    }
}
