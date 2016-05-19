package com.tlabs.android.jeeves.service.actions;

import android.content.Context;

import com.tlabs.eve.EveRequest;
import com.tlabs.eve.EveResponse;
import com.tlabs.eve.api.corporation.CorporationContactListRequest;
import com.tlabs.eve.api.corporation.CorporationStandingsRequest;
import com.tlabs.eve.api.mail.Contact;
import com.tlabs.eve.api.mail.ContactListResponse;

public final class CorporationSocialAction extends EveAction {

    public CorporationSocialAction(final Context context, final long corpID) {
        super(
                context,
                new CorporationContactListRequest(Long.toString(corpID)),
                new CorporationStandingsRequest(Long.toString(corpID)));
    }

    @Override
    public EveAction onRequestCompleted(EveRequest<? extends EveResponse> request, EveResponse response) {
        if (response.getCached()) {
            return null;
        }
        if (request instanceof CorporationContactListRequest) {
            onActionCompleted((CorporationContactListRequest)request, (ContactListResponse)response);
            return null;
        }
        return null;
    }

    private void onActionCompleted(final CorporationContactListRequest request, final ContactListResponse response) {
        for (Contact.Group g: response.getContactGroups()) {
            mail.addContact(Long.parseLong(request.getCorporationID()), g);
        }
    }
}
