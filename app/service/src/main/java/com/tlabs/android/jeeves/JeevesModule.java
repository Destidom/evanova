package com.tlabs.android.jeeves;


import android.content.Context;

import com.tlabs.android.jeeves.data.CacheDatabaseOpenHelper;
import com.tlabs.android.jeeves.data.EvanovaDatabaseOpenHelper;
import com.tlabs.android.jeeves.data.EveDatabaseOpenHelper;
import com.tlabs.android.jeeves.data.FittingDatabaseOpenHelper;
import com.tlabs.android.jeeves.data.MailDatabaseOpenHelper;
import com.tlabs.android.jeeves.model.data.cache.CacheFacade;
import com.tlabs.android.jeeves.model.data.cache.CacheFacadeImpl;
import com.tlabs.android.jeeves.model.data.evanova.EvanovaFacade;
import com.tlabs.android.jeeves.model.data.evanova.EvanovaFacadeImpl;
import com.tlabs.android.jeeves.model.data.fitting.FittingFacade;
import com.tlabs.android.jeeves.model.data.fitting.FittingFacadeImpl;
import com.tlabs.android.jeeves.model.data.sde.EveFacade;
import com.tlabs.android.jeeves.model.data.sde.EveFacadeImpl;
import com.tlabs.android.jeeves.model.data.social.MailFacade;
import com.tlabs.android.jeeves.model.data.social.MailFacadeImpl;
import com.tlabs.android.jeeves.network.Authentication;
import com.tlabs.android.jeeves.network.CachedNetwork;
import com.tlabs.android.jeeves.network.DisabledNetwork;
import com.tlabs.android.jeeves.network.HttpNetwork;
import com.tlabs.android.jeeves.notifications.EveNotificationPreferences;
import com.tlabs.android.jeeves.service.EveAPIServicePreferences;
import com.tlabs.android.util.Environment;
import com.tlabs.eve.EveNetwork;
import com.tlabs.eve.EveRequest;
import com.tlabs.eve.EveResponse;

import dagger.Module;
import dagger.Provides;

@Module
public class JeevesModule {

    private final EveAPIServicePreferences apiPreferences;
    private final EveNotificationPreferences notificationPreferences;

    private Context context;

    public JeevesModule(Context context) {
        this.context = context.getApplicationContext();
        this.apiPreferences = new EveAPIServicePreferences(context.getApplicationContext());
        this.notificationPreferences = new EveNotificationPreferences(context.getApplicationContext());
    }

    @Provides
    public EveAPIServicePreferences provideApiPreferences() {
        return this.apiPreferences;
    }

    @Provides
    public EveNotificationPreferences provideNotificationPreferences() {
        return this.notificationPreferences;
    }

    @Provides
    public EveNetwork provideEveNetwork(final EvanovaFacade evanova, final CacheFacade cache) {
        return createNetwork(evanova, cache);
    }

    @Provides
    public EveFacade provideEveFacade() {
        return new EveFacadeImpl(EveDatabaseOpenHelper.from(this.context).getDatabase());
    }

    @Provides
    public MailFacade provideMailFacade() {
        return new MailFacadeImpl(
                MailDatabaseOpenHelper.from(this.context).getDatabase(),
                EvanovaDatabaseOpenHelper.from(this.context).getDatabase());
    }

    @Provides
    public EvanovaFacade provideEvanovaFacade(final EveFacade eve) {
        return new EvanovaFacadeImpl(
                EvanovaDatabaseOpenHelper.from(this.context).getDatabase(),
                eve);
    }

    @Provides
    public FittingFacade provideFittingFacade() {
        return new FittingFacadeImpl(
                FittingDatabaseOpenHelper.from(this.context).getDatabase(),
                EveDatabaseOpenHelper.from(this.context).getFitting());
    }

    @Provides
    public CacheFacade provideCacheFacade() {
        return new CacheFacadeImpl(CacheDatabaseOpenHelper.from(this.context).getDatabase());
    }

    private EveNetwork createNetwork(
            final EvanovaFacade evanova,
            final CacheFacade cache) {
        final EveNetwork network = new EveNetwork() {
            final EveNetwork http = new HttpNetwork(context);
            final Authentication authentication = Authentication.from(context, evanova);
            @Override
            public <T extends EveResponse> T execute(EveRequest<T> eveRequest) {
                if (!authentication.authenticate(eveRequest)) {
                    return eveRequest.createError(403, "Stored Eve API credentials don't allow " + eveRequest.getPage());
                }
                return http.execute(eveRequest);
            }
        };

        final EveNetwork sticky = new DisabledNetwork(context, cache);
        final EveNetwork cached = new CachedNetwork(context, cache, network, false);
        return  new EveNetwork() {
            @Override
            public <T extends EveResponse> T execute(EveRequest<T> eveRequest) {
                final boolean spareNetwork =  apiPreferences.getCachingSpareNetwork();
                if (Environment.isNetworkAvailable(context, spareNetwork)) {
                    return cached.execute(eveRequest);
                }
                return sticky.execute(eveRequest);
            }
        };
    }
}
