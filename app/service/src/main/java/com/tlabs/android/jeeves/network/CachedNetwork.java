package com.tlabs.android.jeeves.network;

import android.content.Context;
import android.content.ContextWrapper;

import com.tlabs.android.jeeves.model.data.cache.CacheFacade;
import com.tlabs.eve.EveNetwork;
import com.tlabs.eve.EveRequest;
import com.tlabs.eve.EveResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CachedNetwork extends ContextWrapper implements EveNetwork {
    private static final Logger LOG = LoggerFactory.getLogger(CachedNetwork.class);

    private final EveNetwork delegate;
    private final boolean force;

    private final CacheFacade cache;

    public CachedNetwork(
            final Context context,
            final CacheFacade cache,
            final EveNetwork delegate,
            final boolean force) {
        super(context);
        this.delegate = delegate;
        this.force = force;
        this.cache = cache;
    }

    @Override
    public <T extends EveResponse> T execute(EveRequest<T> request) {
        if (this.force) {
            LOG.debug("forced bypass cache for {} ", request.getClass().getSimpleName());
            return executeAndCacheDelegate(request);
        }
        return executeCached(request);
    }

    private <T extends EveResponse> T executeAndCacheDelegate(final EveRequest<T> request) {
        final T t = delegate.execute(request);
        cache.cache(request, t);
        return t;
    }

    private <T extends EveResponse> T executeCached(final EveRequest<T> request) {
            final T response = cache.findCached(request);
            if (null == response) {
                LOG.debug("{}: no cache hit", request.getClass().getSimpleName());
                return executeAndCacheDelegate(request);
            }

            final long expired = response.getCachedUntil() - System.currentTimeMillis();
            if (expired > 0) {
                LOG.debug("{} cached for {}mn", request.getClass().getSimpleName(), expired / 1000);
                return response;
            }
            LOG.debug("{}  cache requires new", request.getClass().getSimpleName());
            return executeAndCacheDelegate(request);
    }
}
