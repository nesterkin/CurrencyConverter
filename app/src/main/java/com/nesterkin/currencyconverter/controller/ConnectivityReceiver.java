package com.nesterkin.currencyconverter.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.lang.ref.WeakReference;

public class ConnectivityReceiver extends BroadcastReceiver {

    private static WeakReference<ConnectivityReceiverListener> connectivityReceiverListener;

    public ConnectivityReceiver() {
        super();
    }

    public static void setListener (ConnectivityReceiverListener listener) {
        connectivityReceiverListener = new WeakReference<>(listener);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if (connectivityReceiverListener != null) {
            ConnectivityReceiverListener listener = connectivityReceiverListener.get();
            if (listener != null) {
                listener.onNetworkConnectionChanged(isConnected);
            }
        }
    }

    public static boolean isConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) CurrencyApp.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
