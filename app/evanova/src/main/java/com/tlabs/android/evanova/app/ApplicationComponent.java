package com.tlabs.android.evanova.app;

import android.content.Context;

import com.tlabs.android.evanova.content.ContentFacade;
import com.tlabs.android.evanova.content.ContentModule;
import com.tlabs.android.evanova.preferences.PreferencesModule;
import com.tlabs.android.evanova.preferences.SavedPreferences;
import com.tlabs.android.evanova.preferences.UserPreferences;
import com.tlabs.android.evanova.content.ContentPublisher;
import com.tlabs.android.jeeves.model.data.cache.CacheFacade;
import com.tlabs.android.jeeves.model.data.evanova.EvanovaFacade;
import com.tlabs.android.jeeves.model.data.fitting.FittingFacade;
import com.tlabs.android.jeeves.model.data.sde.EveFacade;
import com.tlabs.android.jeeves.model.data.social.MailFacade;
import com.tlabs.android.jeeves.modules.EveAPIModule;
import com.tlabs.android.jeeves.modules.EveContentModule;
import com.tlabs.android.jeeves.modules.EveDotlanModule;
import com.tlabs.android.jeeves.service.EveAPIServicePreferences;
import com.tlabs.android.jeeves.notifications.EveNotificationPreferences;
import com.tlabs.eve.EveNetwork;

import org.devfleet.dotlan.DotlanService;

import dagger.Component;

@Component(
        modules = {
            ApplicationModule.class,
            EveContentModule.class,
            EveAPIModule.class,
            EveDotlanModule.class,
            ContentModule.class,
            PreferencesModule.class})
public interface ApplicationComponent {

    Context applicationContext();

    ContentFacade contentFacade();
    ContentPublisher contentPublisher();

    EveFacade eveFacade();
    EvanovaFacade evanovaFacade();
    CacheFacade cacheFacade();
    MailFacade mailFacade();
    FittingFacade fittingFacade();

    EveNetwork eveAPI();
    DotlanService dotlanService();

    UserPreferences userPreferences();
    SavedPreferences savedPreferences();
    EveNotificationPreferences notificationPreferences();
    EveAPIServicePreferences apiPreferences();


}

