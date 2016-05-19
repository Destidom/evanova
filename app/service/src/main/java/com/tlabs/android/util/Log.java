package com.tlabs.android.util;

public final class Log {
    //ProGuard relies on those constants at build time.
    public static final boolean D = true;
    public static final boolean W = true;

    private Log() {
    }

    public static void d(String tag, String m, Throwable t) {
        if (!D) {
            return;
        }
        android.util.Log.d(tag, null == m ? "null" : m, t);
    }

    public static void d(String tag, Throwable t) {
        if (!D) {
            return;
        }
        android.util.Log.d(tag, "", t);
    }

    public static void d(String tag, String m) {
        if (!D) {
            return;
        }
        android.util.Log.d(tag, null == m ? "null" : m);
    }

    public static void w(String tag, String m, Throwable t) {
        if (!W) {
            return;
        }
        android.util.Log.w(tag, null == m ? "null" : m, t);
    }

    public static void w(String tag, String m) {
        if (!W) {
            return;
        }
        android.util.Log.w(tag, null == m ? "null" : m);
    }

    public static void w(String tag, Throwable t) {
        if (!W) {
            return;
        }
        android.util.Log.w(tag, t);
    }

    public static void e(String tag, String m, Throwable t) {
        android.util.Log.e(tag, null == m ? "null" : m, t);
    }

    public static void e(String tag, String m) {
        android.util.Log.e(tag, null == m ? "null" : m);
    }

    public static void i(String tag, String m) {
        android.util.Log.i(tag, null == m ? "null" : m);
    }
}
