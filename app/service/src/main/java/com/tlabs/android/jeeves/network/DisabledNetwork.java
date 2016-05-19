package com.tlabs.android.jeeves.network;

import android.content.Context;
import android.content.ContextWrapper;

import com.tlabs.android.jeeves.model.data.cache.CacheFacade;
import com.tlabs.android.util.Log;
import com.tlabs.eve.EveNetwork;
import com.tlabs.eve.EveRequest;
import com.tlabs.eve.EveResponse;

public final class DisabledNetwork extends ContextWrapper implements EveNetwork {

    private static final String LOG = "StickyRequestHandler";

    private final CacheFacade cache;

    public DisabledNetwork(final Context context, final CacheFacade cache) {
        super(context);
        this.cache = cache;
    }
    
    @Override
    public <T extends EveResponse> T execute(EveRequest<T> request) {
        T response = cache.getCached(request);
        if (null == response) {
            if (Log.D) {
                Log.d(LOG, request.getClass().getSimpleName() + " no cached entry.");
            }
            response = request.createError(500, "No cache entry");
        }
        return response;
    }
}
