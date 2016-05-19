package com.tlabs.android.jeeves.service.actions;

import android.content.Context;

import com.tlabs.eve.api.SovereigntyRequest;
import com.tlabs.eve.api.StationsRequest;

public final class ServerInformationAction extends EveAction {

    public ServerInformationAction(final Context context) {
        super(
                context,
                new StationsRequest(),
                new SovereigntyRequest());
    }

}
