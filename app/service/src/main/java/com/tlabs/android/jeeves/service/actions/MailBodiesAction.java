package com.tlabs.android.jeeves.service.actions;

import android.content.Context;

import com.tlabs.eve.api.mail.MailBodiesRequest;
import com.tlabs.eve.api.mail.MailBodiesResponse;
import com.tlabs.eve.api.mail.MailMessage;

public final class MailBodiesAction extends EveSingleAction<MailBodiesRequest, MailBodiesResponse> {
    private final long ownerID;

    public MailBodiesAction(final Context context, final long ownerID, final long[] messageIds) {
        super(context, new MailBodiesRequest(Long.toString(ownerID), messageIds));        
        this.ownerID = ownerID;
    }

    @Override
    protected EveAction onActionCompleted(MailBodiesRequest request, MailBodiesResponse response) {
        if (response.getCached()) {
            return null;
        }

        for (MailMessage m: response.getMessages()) {
            mail.updateMail(ownerID, m);
        }
        return null;
    }
}
