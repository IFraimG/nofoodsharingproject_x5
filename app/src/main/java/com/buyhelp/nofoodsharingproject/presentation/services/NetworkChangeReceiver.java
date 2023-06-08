package com.buyhelp.nofoodsharingproject.presentation.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private final NetworkChangeListener listener;

    public NetworkChangeReceiver(NetworkChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isConnectedToInternet(context)) {
            listener.onNetworkConnected();
        } else {
            listener.onNetworkDisconnected();
        }
    }

    private boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            return networkCapabilities != null && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
//                Toast.makeText(getApplicationContext(), getString(R.string.no_wifi), Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public interface NetworkChangeListener {
        void onNetworkConnected();

        void onNetworkDisconnected();
    }
}