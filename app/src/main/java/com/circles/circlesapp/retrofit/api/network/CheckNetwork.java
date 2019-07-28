package com.circles.circlesapp.retrofit.api.network;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckNetwork {

    public static boolean isConnected(Context context) {
        try {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        } catch (Exception e) {
            return false;
        }
    }

    public static void isConnected(Context context, OnCheckConnection OnCheckConnection) {
        if (isConnected(context)) {
            OnCheckConnection.ConnectionTrue();
        } else {
            OnCheckConnection.ConnectionError();
        }
    }
}
