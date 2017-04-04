package com.app.localbig.view.widget.youtube;

import android.support.design.BuildConfig;
import android.util.Log;

/**
 * Created by jakubszczygiel on 03/01/15.
 */
public class LogHelper {
    public static final String LOG_TAG = "YtOverlay";

    public static void logMessage(String message) {
        if(BuildConfig.DEBUG) {
            Log.i(LOG_TAG, message);
        }
    }
}
