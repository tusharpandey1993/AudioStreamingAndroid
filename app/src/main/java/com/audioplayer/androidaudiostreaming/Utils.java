package com.audioplayer.androidaudiostreaming;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {
    private static Utils utilities;
    public static Utils getInstance() {
        if (utilities == null) {
            utilities = new Utils();
        }
        return utilities;
    }

    public boolean isNetworkAvailable(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        if (context!=null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        haveConnectedWifi = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        haveConnectedMobile = true;
            }
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
