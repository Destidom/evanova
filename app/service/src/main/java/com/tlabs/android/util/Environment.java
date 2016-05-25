package com.tlabs.android.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class Environment {

    public static boolean isExternalStorageAvailable(boolean readWriteMode) {
        String externalState = android.os.Environment.getExternalStorageState();
        if (readWriteMode) {
            return android.os.Environment.MEDIA_MOUNTED.equals(externalState);
        }

        return android.os.Environment.MEDIA_MOUNTED.equals(externalState) || android.os.Environment.MEDIA_MOUNTED_READ_ONLY.equals(externalState);
    }

    public static File getCacheDirectory(final Context context) {
        if (VERSION.SDK_INT < 8) {
            return context.getCacheDir();
        }

        //Exceptions should not happen
        try {
            Method method = Context.class.getMethod("getExternalCacheDir");
            return (File) method.invoke(context);
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static File getExternalStorageDirectory() {
        return android.os.Environment.getExternalStorageDirectory();
    }

    public static File getLocalStorageDirectory(final Context context) {
        return context.getFilesDir();
    }

    public static boolean isNetworkAvailable(final Context context, final boolean excludeCostlyConnection) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == manager) {
            return false;
        }
        final NetworkInfo active = manager.getActiveNetworkInfo();
        if (null == active) {
            return false;
        }
        if (!active.isConnected()) {
            return false;
        }
        if (!excludeCostlyConnection) {
            return true;
        }

        switch (active.getType()) {
            case ConnectivityManager.TYPE_BLUETOOTH:
            case ConnectivityManager.TYPE_DUMMY:
            case ConnectivityManager.TYPE_ETHERNET:
            case ConnectivityManager.TYPE_WIFI:
            case ConnectivityManager.TYPE_WIMAX:
                return true;
            default:
                return false;
        }
    }

}
