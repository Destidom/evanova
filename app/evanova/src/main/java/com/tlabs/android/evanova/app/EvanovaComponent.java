package com.tlabs.android.evanova.app;

import android.content.Context;

import com.tlabs.android.evanova.content.ContentFacade;
import com.tlabs.android.evanova.content.ContentPublisher;
import com.tlabs.android.evanova.preferences.SavedPreferences;
import com.tlabs.android.evanova.preferences.UserPreferences;
import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.android.jeeves.model.data.cache.CacheFacade;
import com.tlabs.android.jeeves.model.data.evanova.EvanovaFacade;
import com.tlabs.android.jeeves.model.data.fitting.FittingFacade;
import com.tlabs.android.jeeves.model.data.sde.EveFacade;
import com.tlabs.android.jeeves.model.data.social.MailFacade;
import com.tlabs.android.jeeves.modules.EveAccountModule;
import com.tlabs.android.jeeves.modules.EveCrestModule;
import com.tlabs.android.jeeves.notifications.EveNotificationPreferences;
import com.tlabs.android.jeeves.service.EveAPIServicePreferences;
import com.tlabs.eve.EveNetwork;

import org.devfleet.crest.CrestService;
import org.devfleet.dotlan.DotlanService;

import dagger.Component;
import dagger.Lazy;

@Component(
        dependencies = {ApplicationComponent.class},
        modules = {
            EveAccountModule.class,
            EveCrestModule.class})
public interface EvanovaComponent {

    EveAccount eveAccount();

    CrestService crestService();

    Context context();

    ContentFacade contentFacade();
    ContentPublisher apiEvents();

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
