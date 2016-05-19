package com.tlabs.android.jeeves.service;

import android.content.Context;

import com.tlabs.android.jeeves.model.data.cache.CacheFacade;
import com.tlabs.android.jeeves.network.CachedNetwork;
import com.tlabs.android.jeeves.network.Authentication;
import com.tlabs.android.jeeves.network.HttpNetwork;
import com.tlabs.android.jeeves.network.DisabledNetwork;
import com.tlabs.android.jeeves.service.actions.EveAction;
import com.tlabs.android.util.Environment;
import com.tlabs.android.util.Log;
import com.tlabs.eve.EveNetwork;
import com.tlabs.eve.EveRequest;

import rx.Observable;
import rx.schedulers.Schedulers;

final class EveApiActionHelper {

    private static final String LOG = "EveApiActionHelper";

    private final Context context;

    private final EveAPIServicePreferences preferences;
    private final Authentication authentication;

    private final EveNetwork stickyNetwork;
    private final EveNetwork httpNetwork;

    private final CacheFacade cache;

    public EveApiActionHelper(
            final Context context,
            final CacheFacade cache,
            final Authentication authentication) {
        this.authentication = authentication;

        this.context = context;
        this.preferences = new EveAPIServicePreferences(context);

        this.stickyNetwork = new DisabledNetwork(context, cache);
        this.httpNetwork = new HttpNetwork(context);
        this.cache = cache;
    }

    public void executeActions(final boolean force, final EveAction... actions) {

        EveNetwork handler = this.stickyNetwork;

        final boolean spareNetwork = preferences.getCachingSpareNetwork();
        if (Environment.isNetworkAvailable(context, spareNetwork)) {
            handler = new CachedNetwork(context, cache, httpNetwork, force);
        }

        for (EveAction action : actions) {
            executeActionImpl(action, handler);
        }
    }

    private void executeActionImpl(final EveAction action, final EveNetwork handler) {
        if (Log.D) {
            Log.d(LOG, "executeAction start " + action.getClass().getSimpleName());
        }
        if (action.getSynchronous()) {
            for (EveRequest<?> r : action.getRequests()) {
                executeRequest(action, r, handler);
            }
        }
        else {
            Observable.from(action.getRequests())
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(r -> executeRequest(action, r, handler));
        }
    }

    private void executeRequest(final EveAction action, final EveRequest<?> r, final EveNetwork handler) {
        EveAction next;
        if (this.authentication.authenticate(r)) {
            next = action.onRequestCompleted(r, handler.execute(r));
            if (Log.D) {
                Log.d(LOG, "onRequestCompleted 200 " + r.getClass().getSimpleName());
            }
        }
        else {
            next = action.onRequestCompleted(r, r.createError(403, "API key doesn't allow " + r.getPage()));
            if (Log.D) {
                Log.d(LOG, "onRequestCompleted 403 (not allowed by API key) " + r.getClass().getSimpleName());
            }
        }
        if (null == next) {
            return;
        }

        executeActionImpl(next, handler);
    }
}