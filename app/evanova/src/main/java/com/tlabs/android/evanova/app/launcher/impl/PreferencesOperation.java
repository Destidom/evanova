package com.tlabs.android.evanova.app.launcher.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.tlabs.android.evanova.app.launcher.LauncherUseCase;
import com.tlabs.android.evanova.preferences.UserPreferences;
import com.tlabs.android.jeeves.notifications.EveNotificationPreferences;

import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.Set;

final class PreferencesOperation implements LauncherUseCase.Operation {

    private static final String PREFS30 = "com.tlabs.android.evanova.preferences.30.3";

    @Override
    public void execute(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences.getBoolean(PREFS30, false)) {
            return;
        }
        migrate30(preferences, context);
        preferences.
                edit().
                putBoolean(PREFS30, true).
                apply();
    }

    @Override
    public boolean runOnce() {
        return false;
    }

    private static void migrate30(final SharedPreferences preferences, final Context context) {

        migrateNotifications30(preferences, context);

        UserPreferences userPreferences = new UserPreferences(context);
        userPreferences.setDefaultOptions();

        EveNotificationPreferences notificationPreferences = new EveNotificationPreferences(context);
        notificationPreferences.setDefaultOptions();
    }

    private static void migrateNotifications30(final SharedPreferences preferences, final Context context) {

        final SharedPreferences.Editor editor = preferences.edit();
        for (Map.Entry<String, ?> a: preferences.getAll().entrySet()) {
            if (!a.getKey().startsWith("jeeves.preferences.notifications")) {
                continue;
            }
            final String newKey = StringUtils.removeStart(a.getKey(), "jeeves.");
            if (a.getValue() instanceof String) {
                editor.putString(newKey, (String)a.getValue());
            }
            else if (a.getValue() instanceof Set) {
                editor.putStringSet(newKey, (Set<String>)a.getValue());
            }
            else if (a.getValue() instanceof Boolean) {
                editor.putBoolean(newKey, (Boolean)a.getValue());
            }
            else if (a.getValue() instanceof Integer) {
                editor.putInt(newKey, (Integer)a.getValue());
            }
            else if (a.getValue() instanceof Long) {
                editor.putLong(newKey, (Long)a.getValue());
            }
            else if (a.getValue() instanceof Float) {
                editor.putFloat(newKey, (Float) a.getValue());
            }
            editor.remove(a.getKey());
        }
        editor.apply();
    }

}
