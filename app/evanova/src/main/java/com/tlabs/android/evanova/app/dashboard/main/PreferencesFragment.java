package com.tlabs.android.evanova.app.dashboard.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.R.xml;
import com.tlabs.android.evanova.preferences.SavedPreferences;
import com.tlabs.android.evanova.preferences.UserPreferences;
import com.tlabs.android.jeeves.model.data.cache.CacheFacade;
import com.tlabs.android.jeeves.service.EveAPIServicePreferences;
import com.tlabs.android.jeeves.notifications.EveNotificationPreferences;
import com.tlabs.android.jeeves.service.EveNotificationService;
import com.tlabs.android.jeeves.views.Snacks;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class PreferencesFragment extends PreferenceFragmentCompat implements OnSharedPreferenceChangeListener {

    private static final Logger LOG = LoggerFactory.getLogger(PreferencesFragment.class);

    private static final int ACTIVITY_SELECT_IMAGE = 2610;
    private static final int ACTIVITY_SELECT_SOUND = 1921;

    @Inject
    UserPreferences userPreferences;

    @Inject
    EveNotificationPreferences notificationPreferences;

    @Inject
    EveAPIServicePreferences apiPreferences;

    @Inject
    CacheFacade cache;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
      //  EvanovaApplication.getAppComponent().inject(this);
        setPreferencesFromResource(xml.preferences, s);

        apiPreferences.register(this);
        notificationPreferences.register(this);
        userPreferences.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        apiPreferences.unregister(this);
        notificationPreferences.unregister(this);
        userPreferences.unregister(this);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (null == preference.getKey()) {
            return super.onPreferenceTreeClick(preference);
        }

        if (!EveNotificationPreferences.KEY_NOTIFICATION_SOUND.equals(preference.getKey())) {
            return super.onPreferenceTreeClick(preference);
        }

        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, Settings.System.DEFAULT_NOTIFICATION_URI);

        Uri ringtone = notificationPreferences.getNotificationSound();
        if (null == ringtone) {
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Settings.System.DEFAULT_NOTIFICATION_URI);
        }
        else {
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, ringtone);
        }

        startActivityForResult(intent, ACTIVITY_SELECT_SOUND);
        return true;
    }

    @Override
    public final void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        LOG.debug("onSharedPreferenceChanged: {}", key);

        if (key.startsWith(EveNotificationPreferences.KEY_FREQUENCY)) {
            onAlarmOptionChanged();
            return;
        }
        if (UserPreferences.KEY_BACKGROUND.equals(key)) {
            onBackgroundChanged(extractInt(sharedPreferences, UserPreferences.KEY_BACKGROUND, 1));
            return;
        }
        if (UserPreferences.KEY_DISPLAY.equals(key)) {
            onDisplayOptionChanged(extractInt(sharedPreferences, UserPreferences.KEY_DISPLAY, UserPreferences.OPT_BACKGROUND_PORTRAIT));
            return;
        }
        if (SavedPreferences.KEY_CACHE_INVALIDATE.equals(key)) {
            onInvalidateCacheChanged(sharedPreferences.getBoolean(SavedPreferences.KEY_CACHE_INVALIDATE, false));
            return;
        }
        if (EveAPIServicePreferences.KEY_API_CHECK.equals(key)) {
            onEveApiCheckChanged(sharedPreferences.getBoolean(EveAPIServicePreferences.KEY_API_CHECK, true));
            return;
        }
        if (EveAPIServicePreferences.KEY_API_URL.equals(key)) {
            onApiURLChanged();
            return;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == data) {
            return;
        }
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ACTIVITY_SELECT_IMAGE:
                userPreferences.saveBackgroundImage(null == data ? null : data.getData().toString());
                Snacks.show(getActivity(), R.string.toast_preferences_success);
                return;
            case ACTIVITY_SELECT_SOUND:
                final Uri ringtone = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                notificationPreferences.setNotificationSound(ringtone);
                return;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                return;
        }

    }

    private void onAlarmOptionChanged() {
        EveNotificationService.setAlarms(getActivity().getApplicationContext(), true);
    }

    private void onApiURLChanged() {
        String value = StringUtils.trim(apiPreferences.getEveURL());
        if (StringUtils.startsWith(value, "http://api.eveonline.com") || StringUtils.startsWith(value, "http://api.eve-online.com")) {
            Snacks.show(
                    getActivity(),
                    "Usage of unsecure HTTP for Eve Online API is deprecated. Please use HTTPS instead.",
                    true);
        }
        else if (StringUtils.startsWith(value, "http:")) {
            //I18N
            Snacks.show(
                    getActivity(),
                    "In order to avoid your API keys being sent in clear over the network, " +
                            "it is recommended to use HTTPS instead of HTTP.",
                    true);
        }
    }

    private void onDisplayOptionChanged(final int option) {
        userPreferences.setDisplayOption(option);
    }

    private void onBackgroundChanged(final int option) {
        switch (option) {
            case 0:
                userPreferences.saveBackgroundImage(null);
                Snacks.show(getActivity(), R.string.toast_preferences_success);
                break;
            case 1: {//Planet
                userPreferences.saveBackgroundResource(R.drawable.background_planet);
                Snacks.show(getActivity(), R.string.toast_preferences_success);
                break;
            }
            case 2: {//Moon
                userPreferences.saveBackgroundResource(R.drawable.background_moon);
                Snacks.show(getActivity(), R.string.toast_preferences_success);
                break;
            }
            case 3: {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select"), ACTIVITY_SELECT_IMAGE);
                break;
            }
            default:
                break;
        }
    }

    private void onEveApiCheckChanged(final boolean checked) {
        if (!checked) {
            apiPreferences.setEveURL(EveAPIServicePreferences.URL_API_DEFAULT);
            apiPreferences.setEveImageURL(EveAPIServicePreferences.URL_IMAGES_DEFAULT);
            apiPreferences.setEveCentralURL(EveAPIServicePreferences.URL_CENTRAL_DEFAULT);
            apiPreferences.setEveRSSURL(EveAPIServicePreferences.URL_RSS_DEFAULT);
        }
    }

    private void onInvalidateCacheChanged(boolean invalidate) {
        if (invalidate) {
            this.cache.clear();
        }
    }

    private static int extractInt(SharedPreferences sharedPreferences, final String key, int defaultValue) {
        int option = defaultValue;
        try {
            option = sharedPreferences.getInt(key, defaultValue);
        }
        catch (ClassCastException e) {
            try {
                LOG.warn("onBackgroundOptionChanged1 {}", e.getMessage());
                option = Integer.parseInt(sharedPreferences.getString(key, Integer.toString(defaultValue)));
            }
            catch (NumberFormatException e2) {
                LOG.warn("onBackgroundOptionChanged2 {}", e2.getMessage());
            }
            catch (ClassCastException e3) {
                LOG.warn("onBackgroundOptionChanged3 {}", e3.getMessage());
            }
        }

        return option;
    }

}
