package com.tlabs.android.jeeves.service.actions;

import android.content.Context;

import com.tlabs.eve.zkb.ZKillCorporationLogRequest;

public final class CorporationMailboxAction extends EveAction {
    public CorporationMailboxAction(final Context context, final long ownerID) {
        super(context, new ZKillCorporationLogRequest(ownerID));
    }
}
