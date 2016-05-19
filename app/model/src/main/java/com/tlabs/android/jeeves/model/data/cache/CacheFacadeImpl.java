package com.tlabs.android.jeeves.model.data.cache;

import com.tlabs.android.jeeves.model.data.cache.entities.CacheRequestEntity;
import com.tlabs.android.jeeves.model.data.cache.entities.CacheSovereigntyEntity;
import com.tlabs.android.jeeves.model.data.cache.entities.CacheStationEntity;
import com.tlabs.eve.EveRequest;
import com.tlabs.eve.EveResponse;
import com.tlabs.eve.api.NamesResponse;
import com.tlabs.eve.api.Sovereignty;
import com.tlabs.eve.api.SovereigntyResponse;
import com.tlabs.eve.api.Station;
import com.tlabs.eve.api.StationsResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CacheFacadeImpl implements CacheFacade {
    private static final Logger LOG = LoggerFactory.getLogger(CacheFacadeImpl.class);

    private final CacheDatabase cache;

    public CacheFacadeImpl(final CacheDatabase cache) {
        this.cache = cache;
    }

    @Override
    public String getName(long id) {
        String name = this.cache.getName(id);
        if (null == name) {
            name = this.cache.getStationName(id);
        }
        return name;
    }

    @Override
    public <T extends EveResponse> T getCached(EveRequest<T> request) {
        LOG.debug("Jeeves.CacheFacadeImpl:getCached {}", request.getClass().getSimpleName());
        return CacheEntities.transform(request, this.cache.getCached(CacheEntities.md5(request), true));
    }

    @Override
    public <T extends EveResponse> T findCached(EveRequest<T> request) {
        LOG.debug("Jeeves.CacheFacadeImpl:findCached {}", request.getClass().getSimpleName());
        return CacheEntities.transform(request, this.cache.getCached(CacheEntities.md5(request), false));
    }

    @Override
    public <T extends EveResponse> void cache(EveRequest<T> request, T response) {
        final CacheRequestEntity cacheEntity = CacheEntities.transform(request, response);
        this.cache.cache(cacheEntity);

        if (response instanceof StationsResponse) {
            this.cache.cacheStations(CollectionUtils.collect(
                    ((StationsResponse) response).getStations(),
                    new Transformer<Station, CacheStationEntity>() {
                        @Override
                        public CacheStationEntity transform(Station input) {
                            return CacheEntities.transform(input);
                        }
                    }));
         }
        else if (response instanceof SovereigntyResponse) {
            this.cache.cacheSovereignty(CollectionUtils.collect(
                    ((SovereigntyResponse) response).getSovereignty(),
                    new Transformer<Sovereignty, CacheSovereigntyEntity>() {
                        @Override
                        public CacheSovereigntyEntity transform(Sovereignty input) {
                            return CacheEntities.transform(input);
                        }
                    }));
        }
        else if (response instanceof NamesResponse) {
            this.cache.cacheNames(((NamesResponse) response).getNames());
        }
    }

    @Override
    public List<Long> filterExistingNames(List<Long> from) {
        return this.cache.filterExistingNames(from);
    }

    @Override
    public void clear() {
        this.cache.clear();
    }
}
