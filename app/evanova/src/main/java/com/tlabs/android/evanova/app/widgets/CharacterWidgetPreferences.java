package com.tlabs.android.evanova.app.widgets;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

final class CharacterWidgetPreferences {
    private static final String PREFS = CharacterWidgetConfigure.class.getName() + ".prefs";
    private static final String KEY_OWNER_ID = "ownerId.";

    private CharacterWidgetPreferences() {
    }

    static long getWidgetCharacterId(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, 0);
        return prefs.getLong(KEY_OWNER_ID + appWidgetId, -1);
    }

    static void saveWidgetPreferences(Context context, int appWidgetId, final long ownerId) {
        Editor prefs = context.getSharedPreferences(PREFS, 0).edit();
        prefs.putLong(KEY_OWNER_ID + appWidgetId, ownerId);        
        prefs.apply();
    }
    
    static void deleteWidgetPreferences(Context context, int[] appWidgetIds) {
        Editor prefs = context.getSharedPreferences(PREFS, 0).edit();
        for (int widgetId: appWidgetIds) {
            prefs.remove(KEY_OWNER_ID + widgetId);
        }
        prefs.apply();
    }
}
