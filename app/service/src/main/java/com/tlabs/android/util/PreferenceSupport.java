package com.tlabs.android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class PreferenceSupport {

    protected static final String LOG = "PreferenceSupport";

    private final SharedPreferences preferences;
    private final Context context;

    protected PreferenceSupport(Context context) {
        this.context = context.getApplicationContext();
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    protected Context getContext() {
        return context;
    }

    public void register(final OnSharedPreferenceChangeListener l) {
        getPreferences().registerOnSharedPreferenceChangeListener(l);
    }

    public void unregister(final OnSharedPreferenceChangeListener l) {
        getPreferences().unregisterOnSharedPreferenceChangeListener(l);
    }

    protected SharedPreferences getPreferences() {
        return this.preferences;
    }

    protected void setBoolean(final String key, final boolean value) {
        getPreferences().edit().putBoolean(key, value).commit();
    }

    protected void setInt(final String key, final int value) {
        getPreferences().edit().putInt(key, value).commit();
    }

    protected void setLong(final String key, final long value) {
        getPreferences().edit().putLong(key, value).commit();
    }

    protected void setString(final String key, final String value) {
        if (StringUtils.isBlank(value)) {
            getPreferences().edit().remove(key).apply();
        }
        else {
            getPreferences().edit().putString(key, value).apply();
        }
    }

    protected void remove(final String key) {
        getPreferences().edit().remove(key).commit();
    }

    protected String getString(final String key, final String defaultValue) {
        return getString(getPreferences(), key, defaultValue);
    }

    protected static String getString(final SharedPreferences prefs, final String key, final String defaultValue) {
        try {
            return prefs.getString(key, defaultValue);
        }
        catch (ClassCastException e) {
            if (Log.D)
                Log.d(LOG, "Preferences:getString(" + key + "):" + e.getMessage());
            return defaultValue;
        }
    }

    protected boolean getBoolean(final String key, final boolean defaultValue) {
        return getBoolean(getPreferences(), key, defaultValue);
    }

    protected boolean getBoolean(final SharedPreferences prefs, final String key, final boolean defaultValue) {
        try {
            return prefs.getBoolean(key, defaultValue);
        }
        catch (ClassCastException e) {
            if (Log.D)
                Log.d(LOG, "getBoolean(" + key + "):" + e.getMessage());
            return defaultValue;
        }
    }

    protected int getInt(final String key, final int defaultValue) {
        return getInt(getPreferences(), key, defaultValue);
    }

    protected int getInt(final SharedPreferences prefs, final String key, final int defaultValue) {
        try {
            return prefs.getInt(key, defaultValue);
        }
        catch (ClassCastException e) {
            if (Log.D)
                Log.d(LOG, "Preferences:getInt(" + key + "):" + e.getMessage());
            //DONT return
        }
        try {
            return Integer.parseInt(prefs.getString(key, "" + defaultValue));
        }
        catch (NumberFormatException e) {
            if (Log.D)
                Log.d(LOG, "Preferences:getInt(" + key + "):" + e.getMessage());
            throw e;
        }
    }

    protected long getLong(final String key, final long defaultValue) {
        return getPreferences().getLong(key, defaultValue);
    }

    protected void setLongArray(final String key, final long[] array) {
        if (ArrayUtils.isEmpty(array)) {
            getPreferences().edit().remove(key).commit();
            return;
        }

        final StringBuilder b = new StringBuilder();
        for (long l: array) {
            b.append(Long.toString(l));
            b.append(",");
        }
        getPreferences().edit().putString(key, StringUtils.removeEnd(b.toString(), ",")).apply();
    }

    protected long[] getLongArray(final String key) {
        final String value = getPreferences().getString(key, null);
        if (StringUtils.isBlank(value)) {
            return new long[0];
        }

        final String[] split = StringUtils.split(value, ",");
        if (ArrayUtils.isEmpty(split)) {
            return new long[0];
        }
        try {
            final long[] returned = new long[split.length];
            for (int i = 0; i < split.length; i++) {
                returned[i] = Long.parseLong(split[i]);
            }
            return returned;
        }
        catch (NumberFormatException e) {
            Log.w(LOG, e.getLocalizedMessage(), e);
            Log.e(LOG, e.getLocalizedMessage());
            return new long[0];
        }
    }

    protected Resources getResources() {
        return context.getResources();
    }
}
