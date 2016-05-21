package com.tlabs.android.evanova.app.launcher.presenter;

import android.app.Activity;
import android.content.Intent;
import android.content.UriMatcher;
import android.net.Uri;

import com.tlabs.android.evanova.app.accounts.ui.AccountActivity;
import com.tlabs.android.evanova.app.character.ui.CharacterActivity;
import com.tlabs.android.evanova.app.corporation.ui.CorporationActivity;
import com.tlabs.android.jeeves.notifications.Notifications;
import com.tlabs.android.jeeves.views.Snacks;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

final class LauncherSupport {

    private static final Logger LOG = LoggerFactory.getLogger(LauncherSupport.class);

    private static final int CHARACTER = 1;
    private static final int CORPORATION = 2;
    private static final int ACCOUNT = 3;

    private static final int API = 20;
    private static final int SSO = 21;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Notifications.CHARACTER.getAuthority(), "char/#", CHARACTER);//#=char ID
        uriMatcher.addURI(Notifications.CORPORATION.getAuthority(), "corp/#", CORPORATION);//#=corp ID
        uriMatcher.addURI(Notifications.KEYS.getAuthority(), "key/#", ACCOUNT);//#=key ID
        uriMatcher.addURI("api.eveonline.com", "", API);//should deprecate this in favor of the following
        uriMatcher.addURI("api.eveonline.com", "installKey", API);//api.eveonline.com - install button

        uriMatcher.addURI("crest.eveonline.com", "*", SSO);
    }

    private LauncherSupport() {}

    public static Intent getLaunchIntent(final Activity activity) {
        Uri data = activity.getIntent().getData();
        if (null == data) {
            return null;
        }

        LOG.debug(".getLaunchIntent: {}", activity.getIntent());

        Intent intent = null;
        switch (uriMatcher.match(data)) {
            case CHARACTER: {
                final Long charId = extractID(data, 1);
                if (null != charId) {
                    intent = new Intent(activity, CharacterActivity.class);
                 //   intent.putExtra(com.tlabs.android.evanova.activity.character.CharacterViewActivity.EXTRA_CHAR_ID, charId);
                }
                break;
            }
            case CORPORATION: {
                final Long corpId = extractID(data, 1);
                if (null != corpId) {

                    intent = new Intent(activity, CorporationActivity.class);
                   // intent.putExtra(com.tlabs.android.evanova.activity.corporation.CorporationViewActivity.EXTRA_CORP_ID, corpId);
                }
                break;
            }
            case ACCOUNT: {
                final Long keyId = extractID(data, 1);
                if (null != keyId) {
                    intent = new Intent(activity, AccountActivity.class);
                    //FIXME    intent.putExtra(com.tlabs.android.evanova.activity.api.ApiAccessActivity.EXTRA_KEY_ID, keyId);
                }
                break;
            }
            case API: {
                intent = handleAPIImport(activity, data);
                if (null == intent) {
                    Snacks.show(activity, "Unable to import this API key link.");
                }
                break;
            }
            case SSO: {
                intent = handleSSO(activity, data);
            }
            break;

            default: {
                LOG.warn("Uri not handled {}", data);
                //FIXME can't get the uri matcher to match
                if (null != data.getQueryParameter("code")) {
                    intent = handleSSO(activity, data);
                }
                break;
            }
        }
        if (null == intent) {
            LOG.warn("LaunchURLActivity: no matching authority {} ({})", data.getAuthority(), data);
        }
        else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
        return intent;
    }

    //I18N
    private static Intent handleAPIImport(final Activity activity, final Uri data) {
        Long id = null;
        try {
            final String keyID = data.getQueryParameter("keyID");
            if (StringUtils.isNotBlank(keyID)) {
                id = Long.parseLong(keyID);
            }
        }
        catch (NumberFormatException e) {
        }

        if (null == id) {
            LOG.debug("handleImport: no KeyID");
            Snacks.show(activity, "Invalid API key.");
            return null;
        }

        final String keyValue = data.getQueryParameter("vCode");
        if (StringUtils.isBlank(keyValue)) {
            LOG.debug("handleImport: no vCode");
            Snacks.show(activity, "Invalid API key.");
            return null;
        }

        Snacks.show(activity, "This key will be updated in Evanova.");
        final String keyName = data.getQueryParameter("name");

        Intent intent = new Intent(activity, AccountActivity.class);

        intent.putExtra(AccountActivity.EXTRA_KEY_ID, id);
        intent.putExtra(AccountActivity.EXTRA_KEY_CODE, keyValue);
        intent.putExtra(AccountActivity.EXTRA_KEY_NAME, keyName);

        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return intent;
    }

    private static Intent handleSSO(final Activity activity, final Uri data) {
        final String code = data.getQueryParameter("code");
        if (StringUtils.isBlank(code)) {
            LOG.debug("handleSSO: no auth code.");
            return null;
        }

        Intent intent = new Intent(activity, AccountActivity.class);
        intent.putExtra(AccountActivity.EXTRA_AUTH_CODE, code);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return intent;
    }

    private static Long extractID(Uri uri, int pathSegment) {
        List<String> segments = uri.getPathSegments();
        if (segments.size() < pathSegment + 1) {
            return null;
        }

        try {
            return Long.parseLong(segments.get(pathSegment));
        }
        catch (NumberFormatException e) {
            LOG.warn("Malformed URI: {} {}", uri, e.getLocalizedMessage());
            return null;
        }
    }

}
