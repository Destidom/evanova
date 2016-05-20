package com.tlabs.android.evanova.app;

import android.content.Context;

import com.tlabs.android.evanova.mvp.ActivityPresenter;
import com.tlabs.android.evanova.mvp.ActivityView;
import com.tlabs.android.evanova.preferences.SavedPreferences;
import com.tlabs.android.evanova.preferences.UserPreferences;
import com.tlabs.android.jeeves.model.EveCharacter;
import com.tlabs.android.jeeves.views.EveImages;

public class EvanovaActivityPresenter<T extends ActivityView> extends ActivityPresenter<T> {

    private final UserPreferences userPreferences;
    private final SavedPreferences savedPreferences;

    public EvanovaActivityPresenter(Context context) {
        super(context);
        this.userPreferences = new UserPreferences(context.getApplicationContext());
        this.savedPreferences = new SavedPreferences(context.getApplicationContext());
    }

    protected final UserPreferences userPreferences() {
        return userPreferences;
    }

    protected final SavedPreferences savedPreferences() {
        return savedPreferences;
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
                    setBackground(EveImages.getCharacterImageURL(getContext(), character.getShipTypeID()));
                }
                break;
            case UserPreferences.OPT_BACKGROUND_APP:
            default:
                setBackground(userPreferences.getBackgroundDefault());
                break;
        }
    }
}
