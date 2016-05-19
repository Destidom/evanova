package com.tlabs.android.evanova.preferences;

import android.content.Context;

import com.tlabs.android.jeeves.service.EveAPIServicePreferences;
import com.tlabs.android.jeeves.notifications.EveNotificationPreferences;

import dagger.Module;
import dagger.Provides;

@Module
public class PreferencesModule {

    private Context context;

    public PreferencesModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    public SavedPreferences provideSavedPreferences() {
        return new SavedPreferences(this.context);
    }

    @Provides
    public UserPreferences provideUserPreferences() {
        return new UserPreferences(this.context);
    }

    @Provides
    public EveAPIServicePreferences provideAPIPreferences() {
        return new EveAPIServicePreferences(this.context);
    }

    @Provides
    public EveNotificationPreferences provideNotificationPreferences() {
        return new EveNotificationPreferences(this.context);
    }
}
