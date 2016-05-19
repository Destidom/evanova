package com.tlabs.android.evanova.preferences;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

public class CacheDialogPreference extends DialogPreference {

	public CacheDialogPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CacheDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	 @Override
    protected void onDialogClosed(boolean positiveResult) {
	    if (positiveResult) {
			SavedPreferences preferences = new SavedPreferences(getContext());
			preferences.setInvalidateCache();
	    }	   
        super.onDialogClosed(positiveResult);
    }
}
