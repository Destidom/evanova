package com.tlabs.android.jeeves.modules;


import android.content.Context;

import com.tlabs.android.jeeves.model.data.cache.CacheFacade;
import com.tlabs.android.jeeves.model.data.evanova.EvanovaFacade;
import com.tlabs.android.jeeves.network.Authentication;
import com.tlabs.android.jeeves.network.CachedNetwork;
import com.tlabs.android.jeeves.network.DisabledNetwork;
import com.tlabs.android.jeeves.network.HttpNetwork;
import com.tlabs.android.jeeves.service.EveAPIServicePreferences;
import com.tlabs.android.util.Environment;
import com.tlabs.eve.EveNetwork;
import com.tlabs.eve.EveRequest;
import com.tlabs.eve.EveResponse;

import dagger.Module;
import dagger.Provides;

@Module
public class EveAPIModule {

    private final EveAPIServicePreferences preferences;
    private final Context context;

    public EveAPIModule(final Context context) {
        this.preferences = new EveAPIServicePreferences(context);
        this.context = context;
    }

    @Provides
    public EveNetwork provideEveNetwork(final EvanovaFacade evanova, final CacheFacade cache) {
        return createNetwork(evanova, cache);
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
                final boolean spareNetwork =  preferences.getCachingSpareNetwork();
                if (Environment.isNetworkAvailable(context, spareNetwork)) {
                    return cached.execute(eveRequest);
                }
                return sticky.execute(eveRequest);
            }
        };
    }

}
