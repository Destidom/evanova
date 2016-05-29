package com.tlabs.android.evanova.app;

import android.content.Context;
import android.content.Intent;

import com.tlabs.android.evanova.mvp.ActivityPresenter;
import com.tlabs.android.evanova.mvp.ActivityView;
import com.tlabs.android.evanova.preferences.SavedPreferences;
import com.tlabs.android.evanova.preferences.UserPreferences;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.model.EveCorporation;
import com.tlabs.android.jeeves.service.EveAPIServicePreferences;
import com.tlabs.android.jeeves.views.EveImages;
import com.tlabs.android.util.Environment;

public class EvanovaActivityPresenter<T extends ActivityView> extends ActivityPresenter<T> {

    public static final String EXTRA_OWNER_ID = "evanova.ownerID";

    private final UserPreferences userPreferences;
    private final SavedPreferences savedPreferences;
    private final EveAPIServicePreferences apiPreferences;

    public EvanovaActivityPresenter(Context context) {
        super(context);

        this.userPreferences = new UserPreferences(context.getApplicationContext());
        this.savedPreferences = new SavedPreferences(context.getApplicationContext());
        this.apiPreferences = new EveAPIServicePreferences(context.getApplicationContext());
    }

    protected final UserPreferences userPreferences() {
        return userPreferences;
    }

    protected final SavedPreferences savedPreferences() {
        return savedPreferences;
    }

    protected final EveAPIServicePreferences apiPreferences() {
        return apiPreferences;
    }

    protected final void setBackgroundDefault() {
        setBackground(this.userPreferences.getBackgroundDefault());
    }

    protected final void setBackground(final EveCharacter character) {
        switch (userPreferences.getBackgroundOption()) {
            case UserPreferences.OPT_BACKGROUND_NONE:
                setBackground((String)null);
                break;

            case UserPreferences.OPT_BACKGROUND_PORTRAIT:
                if (null == character) {
                    setBackground(userPreferences.getBackgroundDefault());
                }
                else {
                    setBackground(EveImages.getCharacterImageURL(getContext(), character.getID()));
                }
                break;

            case UserPreferences.OPT_BACKGROUND_SHIP:
                if (null == character) {
                    setBackground(userPreferences.getBackgroundDefault());
                }
                else {
                    setBackground(EveImages.getItemImageURL(getContext(), character.getShipTypeID()));
                }
                break;

            case UserPreferences.OPT_BACKGROUND_APP:
            default:
                setBackground(userPreferences.getBackgroundDefault());
                break;
        }
    }

    protected final void setBackground(final EveCorporation corporation) {
        switch (userPreferences.getBackgroundOption()) {
            case UserPreferences.OPT_BACKGROUND_NONE:
                setBackground((String)null);
                break;

            case UserPreferences.OPT_BACKGROUND_PORTRAIT:
            case UserPreferences.OPT_BACKGROUND_SHIP:
                if (null == corporation) {
                    setBackground(userPreferences.getBackgroundDefault());
                }
                else {
                    setBackground(EveImages.getCorporationImageURL(getContext(), corporation.getID()));
                }
                break;

            case UserPreferences.OPT_BACKGROUND_APP:
            default:
                setBackground(userPreferences.getBackgroundDefault());
                break;
        }
    }

    protected final boolean isNetworkAvailable() {
        return Environment.isNetworkAvailable(getContext(), apiPreferences().getCachingSpareNetwork());
    }

    protected static final long ownerOf(final Intent intent) {
        return (null == intent) ? -1l : intent.getLongExtra(EXTRA_OWNER_ID, -1l);
    }
}
