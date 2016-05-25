package com.tlabs.android.evanova.preferences;

import android.content.ContentResolver;
import android.content.Context;

import com.tlabs.android.evanova.R;
import com.tlabs.android.jeeves.views.EveImages;
import com.tlabs.android.util.PreferenceSupport;

public final class UserPreferences extends PreferenceSupport {

	public static final String KEY_BACKGROUND = "preferences.background_moon.key";
    public static final String KEY_DISPLAY = "preferences.display.key";

    //Applies to char views and corp views only
    public static final int OPT_BACKGROUND_PORTRAIT = 0;
    public static final int OPT_BACKGROUND_SHIP = 1;
    public static final int OPT_BACKGROUND_APP = 2;
    public static final int OPT_BACKGROUND_NONE = 3;

    private static final String KEY_BACKGROUND_URI = "preferences.background_moon.uri";
    private static final String KEY_BACKGROUND_OPT = "preferences.background_moon.option";

	private static final String KEY_MAIN_CAPSULEER = "saved.user.capsuleer";//char ID
	private static final String KEY_MAIN_REGION_ID = "saved.user.region.id";
    private static final String KEY_MAIN_REGION_NAME = "saved.user.region.name";

    public UserPreferences(Context context) {
        super(context);
    }

	public void setDefaultOptions() {
        remove(KEY_BACKGROUND);
        remove(KEY_DISPLAY);

        saveBackgroundResource(R.drawable.background_planet);
        setInt(KEY_BACKGROUND_OPT, OPT_BACKGROUND_PORTRAIT);
	}

	public void setPreferredCharacter(final long charID) {
        setLong(KEY_MAIN_CAPSULEER, charID);
	}

	public long getPreferredCharacter() {
		return getLong(KEY_MAIN_CAPSULEER, -1L);
	}
	
	public void setPreferredRegion(final long regionID, final String regionName) {
        setLong(KEY_MAIN_REGION_ID, regionID);
        setString(KEY_MAIN_REGION_NAME, regionName);
	}

	public long getPreferredRegionId() {
        return getLong(KEY_MAIN_REGION_ID, 10000002L);
	}

    public String getPreferredRegionName() {
        return getString(KEY_MAIN_REGION_NAME, "The Forge");
    }

    public void setDisplayOption(final int option) {
        setInt(KEY_BACKGROUND_OPT, option);
    }

    public String getBackgroundDefault() {
        return getString(KEY_BACKGROUND_URI, null);
    }

    public int getBackgroundOption() {
        return getInt(KEY_BACKGROUND_OPT, OPT_BACKGROUND_PORTRAIT);
    }

    public void saveBackgroundImage(final String imageURI) {
        setString(KEY_BACKGROUND_URI, imageURI);
    }

    public void saveBackgroundResource(final int resId) {
        saveBackgroundImage(resourceToUri(resId));
    }

    public void saveBackgroundCharacter(final long charID) {
        saveBackgroundImage(EveImages.getCharacterImageURL(getContext(), charID));
    }

    public void saveBackgroundCorporation(final long corpID) {
        saveBackgroundImage(EveImages.getCorporationImageURL(getContext(), corpID));
    }

    //"android.resource://[package]/[res id]"
    private String resourceToUri (int resID) {
        return new StringBuilder()
                .append(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .append("://")
                .append(getContext().getResources().getResourcePackageName(resID))
                .append("/")
                .append(resID)
                .toString();

    }
}
