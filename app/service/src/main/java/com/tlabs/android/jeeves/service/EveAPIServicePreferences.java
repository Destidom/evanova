package com.tlabs.android.jeeves.service;

import android.content.Context;

import com.tlabs.android.util.PreferenceSupport;

public final class EveAPIServicePreferences extends PreferenceSupport {

    public static final String KEY_SUPPORT_URL = "preferences.api.evesupport.url";
    public static final String URL_SUPPORT_DEFAULT = "https://community.eveonline.com/support/";
    public static final String URL_SUPPORT_TEST = "https://community.testeveonline.com/support/";

    public static final String KEY_API_URL = "preferences.api.eve.url";
    public static final String KEY_API_CHECK = "preferences.api.check";
    public static final String URL_API_DEFAULT = "https://api.eveonline.com";
    public static final String URL_API_TEST = "https://api.testeveonline.com";

    /*public static final String KEY_CREST_URL = "preferences.crest.eve.url";
    public static final String URL_CREST_DEFAULT = "https://crest-tq.eveonline.com";
    public static final String URL_CREST_TEST = "https:/crest-sisi.testeveonline.com";*/

    private static final String KEY_CREST_LOGIN = "preferences.crest.login.url";


    public static final String KEY_ZKILLBOARD_URL = "preferences.zkill.eve.url";
    public static final String URL_ZKILLBOARD_DEFAULT = "https://zkillboard.com";

    public static final String KEY_IMAGES_URL = "preferences.api.images.url";
    public static final String URL_IMAGES_DEFAULT = "https://image.eveonline.com";

    public static final String KEY_RSS_URL = "preferences.api.rss.url";
    public static final String URL_RSS_DEFAULT = "https://forums.eveonline.com";

    public static final String KEY_CACHE_SPARE = "preferences.cache.spare";

    public static final String KEY_CENTRAL_URL = "preferences.central.url";
    public static final String URL_CENTRAL_DEFAULT = "http://api.eve-central.com";

    public EveAPIServicePreferences(Context context) {
        super(context);
    }

    public void setDefaultOptions() {

        setEveURL(URL_API_DEFAULT);
        setEveSupportURL(URL_SUPPORT_DEFAULT);
        setEveImageURL(URL_IMAGES_DEFAULT);
        setEveRSSURL(URL_RSS_DEFAULT);
        setEveCentralURL(URL_CENTRAL_DEFAULT);
    }

    public String getApiKeyInstallUri() {
        return getEveSupportURL() + "/api-key/ActivateInstallLinks?activate=true";
    }

    public String getEveURL() {
        return getString(KEY_API_URL, URL_API_DEFAULT);
    }

    public void setEveURL(final String url) {
        setString(KEY_API_URL, url);
    }

    public String getEveSupportURL() {
        return getString(KEY_SUPPORT_URL, URL_SUPPORT_DEFAULT);
    }

    public void setEveSupportURL(final String url) {
        setString(KEY_SUPPORT_URL, url);
    }

    public String getEveImageURL() {
        return getString(KEY_IMAGES_URL, URL_IMAGES_DEFAULT).trim();
    }

    public void setEveImageURL(final String url) {
        setString(KEY_IMAGES_URL, url.trim());
    }

    public boolean getCachingSpareNetwork() {
        return getBoolean(KEY_CACHE_SPARE, false);
    }

    public String getEveCentralURL() {
        return getString(KEY_CENTRAL_URL, URL_CENTRAL_DEFAULT);
    }

    public String getZKillboardURL() {
        return getString(KEY_ZKILLBOARD_URL, URL_ZKILLBOARD_DEFAULT);
    }

    public void setEveCentralURL(final String url) {
        setString(KEY_CENTRAL_URL, url);
    }

    public String getEveRSSURL() {
        return getString(KEY_RSS_URL, URL_RSS_DEFAULT);
    }

    public void setEveRSSURL(final String url) {
        setString(KEY_RSS_URL, url);
    }

}
