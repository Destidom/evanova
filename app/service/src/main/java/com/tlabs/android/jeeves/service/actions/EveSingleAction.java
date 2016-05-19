package com.tlabs.android.jeeves.service.actions;

import android.content.Context;

import com.tlabs.eve.EveRequest;
import com.tlabs.eve.EveResponse;

public class EveSingleAction<R extends EveRequest, T extends EveResponse> extends EveAction {

    protected EveSingleAction(final Context context, final R request) {
        super(context, request);
    }

    @Override
    public final EveAction onRequestCompleted(EveRequest<? extends EveResponse> request, EveResponse response) {
        return this.onActionCompleted((R)request, (T)response);
    }

    protected EveAction onActionCompleted(R request, T response) {
        return null;
    }

}
