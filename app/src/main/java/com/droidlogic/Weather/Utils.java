package com.droidlogic.Weather;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Utils {
    public static void startActivitySafe(Context Context, Intent paramIntent) {
        if (paramIntent != null && Context != null) {
            try {
                Context.startActivity(paramIntent);
            } catch (ActivityNotFoundException localActivityNotFoundException) {
                localActivityNotFoundException.printStackTrace();
                Log.e("mylog", "localActivityNotFoundException");
            } catch (SecurityException e) {
                Log.e("mylog", "localSecurityException");
            }
        }
    }

    public static boolean getNetworkConnState(Context context) {
        if (context != null) {
            NetworkInfo mNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
