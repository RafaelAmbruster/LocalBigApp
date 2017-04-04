package com.app.localbig;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.multidex.MultiDexApplication;
import android.util.Base64;
import com.app.localbig.data.AppDatabaseManager;
import com.app.localbig.helper.log.LogManager;
import com.orhanobut.logger.Logger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LocalBigApplication extends MultiDexApplication {

    private static LocalBigApplication instance;
    public static String TAG = "LocalBigApp";

    public LocalBigApplication() {
        instance = this;
    }

    public static synchronized LocalBigApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Logger.init("LocalBigApp").hideThreadInfo().setMethodCount(3).setMethodOffset(2);
        AppDatabaseManager.init(getApplicationContext());
        LogManager.init();
        //printKeyHash();
    }

    public void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.app.localbig", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                LogManager.getInstance().info("SHA: ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static Context getContext() {
        return instance;
    }
}
