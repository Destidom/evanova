package com.tlabs.android.jeeves.service.actions;

import android.content.Context;

import com.tlabs.eve.api.NamesRequest;

import java.util.ArrayList;
import java.util.List;

public final class NamesAction extends EveAction {

    //The following is all about chopping missing name ids in chunks of MAX_LENGTH
    //since the Eve API doesn't allow more than 255 ids at the same time (actually a lot less apparently now).
    private static final int MAX_LENGTH = 100;

    public NamesAction(final Context context, final List<Long> ids) {
        super(context, fill(ids));
    }

    private static NamesRequest[] fill(final List<Long> ids) {
        if (ids.isEmpty()) {
            return new NamesRequest[0];
        }
        
        final List<NamesRequest> requests = new ArrayList<>();
        add(ids, requests);
        return requests.toArray(new NamesRequest[requests.size()]);
    }

    private static void add(final List<Long> ids, final List<NamesRequest> requests) {
        if (ids.isEmpty()) {
            return;
        }

        final List<Long> added = ids.subList(0, Math.min(ids.size(), MAX_LENGTH));
        requests.add(new NamesRequest(added.toArray(new Long[added.size()])));
        if (ids.size() > MAX_LENGTH) {
            add(ids.subList(MAX_LENGTH, ids.size()), requests);
        }
    }
}
