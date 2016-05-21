package com.tlabs.android.evanova.app.fitting;

import android.content.Context;

import com.tlabs.android.util.PreferenceSupport;

import org.apache.commons.lang.ArrayUtils;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public final class FittingPreferences extends PreferenceSupport {

	private static final String KEY_RECENT = "saved.fitting.modules.recent";//Array of long
    private static final int KEY_RECENT_LIMIT = 20;

    public FittingPreferences(Context context) {
        super(context);
    }

    public void addRecentItem(final long itemID) {
        List<Long> recent = new ArrayList<>();
        recent.addAll(Arrays.asList(ArrayUtils.toObject(getRecentItems())));
        if (!recent.contains(itemID)) {
            recent.add(itemID);
        }

        if (recent.size() > KEY_RECENT_LIMIT) {
            recent = recent.subList(recent.size() - KEY_RECENT_LIMIT, recent.size());
        }
        setLongArray(KEY_RECENT, ArrayUtils.toPrimitive(recent.toArray(new Long[recent.size()])));
    }

	public long[] getRecentItems() {
		return getLongArray(KEY_RECENT);
	}
}
