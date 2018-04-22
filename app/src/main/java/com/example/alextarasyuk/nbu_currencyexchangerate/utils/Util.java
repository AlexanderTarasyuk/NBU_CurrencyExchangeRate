package com.example.alextarasyuk.nbu_currencyexchangerate.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Util {

    public static boolean isConnection(final Context context) {
        ConnectivityManager cvm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cvm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }

        return false;
    }

}
