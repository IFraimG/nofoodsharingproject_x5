package com.example.nofoodsharingproject.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.data.RetrofitService;
import com.example.nofoodsharingproject.databinding.FragmentSecretBinding;
import com.example.nofoodsharingproject.utils.DefineUser;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

public class SecretFragment extends DialogFragment {
    private FragmentSecretBinding binding;
    private final ArrayList<String> list = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final EditText input = new EditText(requireContext());


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setPositiveButton("Подключить", (dialog, id) -> {
                    DefineUser defineUser = new DefineUser(requireActivity());
                    defineUser.initBaseRetrofitPath(input.getText().toString());
                    RetrofitService.changeBaseUrl(defineUser.getBaseForRetrofit());
                })
                .setNegativeButton("Отмена", (dialog, id) -> {
                    DefineUser defineUser = new DefineUser(requireActivity());
                    defineUser.setDefaultBasePathForRetrofit();
                    RetrofitService.changeBaseUrl(defineUser.getBaseForRetrofit());
                });


        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        input.setLayoutParams(lp);
        builder.setView(input);

        return builder.create();
    }

    private void getIpAddresses() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();

                if (networkInterface.isUp() && !networkInterface.isLoopback()) {
                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();

                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = inetAddresses.nextElement();

                        if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                            String ipAddress = inetAddress.getHostAddress();
                            list.add(ipAddress);
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private void getFreeAddresses() {
        adapter = new ArrayAdapter<>(requireContext(), R.layout.item_getter_product_name, list);
        String subnet = "192.168.0.";
        int timeout = 1000;

        for (int i = 1; i < 255; i++) {
            String host = subnet + i;
            try {
                if (InetAddress.getByName(host).isReachable(timeout)) {
                } else {
                    Log.d("msg", host);
                    list.add(host);
                    adapter.notifyDataSetChanged();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}