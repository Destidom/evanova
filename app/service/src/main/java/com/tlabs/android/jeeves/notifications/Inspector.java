package com.tlabs.android.jeeves.notifications;


import android.content.Context;
import android.content.ContextWrapper;

import java.util.List;

abstract class Inspector extends ContextWrapper {

	private final EveNotificationPreferences preferences;

	public Inspector(Context context) {
		super(context);
		this.preferences = new EveNotificationPreferences(context);
	}

	abstract List<Notification> inspect();

    protected final EveNotificationPreferences getPreferences() {
        return this.preferences;
    }
}
