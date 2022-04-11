package com.geotec.mujersegura.others;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Internet {

    private static ConnectivityManager conector;
    private static NetworkInfo info;

    private static void ini(Context context) {
        conector = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        info = conector.getActiveNetworkInfo();
    }

    public static boolean isConnected(Context context) {
        ini(context);
        if (info != null && info.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static String getTypeConnection(Context context) {
        if (isConnected(context)) {
            return info.getTypeName();
        } else {
            return "No existe conección a Internet";
        }
    }
}
