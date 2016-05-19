package com.tlabs.android.jeeves.model.data.cache;

import com.tlabs.eve.EveRequest;
import com.tlabs.eve.EveResponse;

import java.util.List;

public interface CacheFacade {

    String getName(final long id);

    List<Long> filterExistingNames(final List<Long> from);

    <T extends EveResponse> T getCached(final EveRequest<T> request);

    <T extends EveResponse> T findCached(final EveRequest<T> request);

    <T extends EveResponse> void cache(final EveRequest<T> request, T response);

    void clear();
}
