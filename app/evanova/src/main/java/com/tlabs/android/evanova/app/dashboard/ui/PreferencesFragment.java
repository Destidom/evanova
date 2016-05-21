package com.tlabs.android.evanova.app.dashboard.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.RingtonePreference;
import android.provider.MediaStore;

import com.tlabs.android.evanova.R;
import com.tlabs.android.evanova.R.xml;
import com.tlabs.android.evanova.preferences.SavedPreferences;
import com.tlabs.android.evanova.preferences.UserPreferences;
import com.tlabs.android.jeeves.model.data.cache.CacheFacade;
import com.tlabs.android.jeeves.service.EveAPIServicePreferences;
import com.tlabs.android.jeeves.notifications.EveNotificationPreferences;
import com.tlabs.android.jeeves.service.EveNotificationService;
import com.tlabs.android.jeeves.views.Snacks;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class PreferencesFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

    private static final Logger LOG = LoggerFactory.getLogger(PreferencesFragment.class);

    private static final int ACTIVITY_SELECT_IMAGE = 1231412;

    @Inject
    UserPreferences userPreferences;

    @Inject
    EveNotificationPreferences notificationPreferences;

    @Inject
    EveAPIServicePreferences apiPreferences;

    @Inject
    CacheFacade cache;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  EvanovaApplication.getAppComponent().inject(this);
        addPreferencesFromResource(xml.preferences);

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

        if (requestCode == ACTIVITY_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                userPreferences.saveBackgroundImage(null == data ? null : data.getData().toString());
                Snacks.show(getActivity(), R.string.toast_preferences_success);
            }
            return;
        }

        //Workaround for https://bitbucket.org/evanova/evanova/issue/49/
        //The RingtonePreference does not receive its onActivityResult for some reason, so it doesn't fire events when a selection is ok-ed.
        //Calling its onActivityResult() has to be done here and we don't even know what the request code should be.
        //Experimentally requestCode seems to be consistently 100 for mail and 101 for training. This assumption is very dodgy.

        RingtonePreference pref = (RingtonePreference) findPreference(EveNotificationPreferences.KEY_NOTIFICATION_SOUND);
        pref.onActivityResult(requestCode, resultCode, data);
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
                userPreferences.saveBackgroundResource(R.drawable.background2);
                Snacks.show(getActivity(), R.string.toast_preferences_success);
                break;
            }
            case 2: {//Moon
                userPreferences.saveBackgroundResource(R.drawable.background);
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
