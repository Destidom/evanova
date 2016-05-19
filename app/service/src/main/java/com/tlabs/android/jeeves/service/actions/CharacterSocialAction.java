package com.tlabs.android.jeeves.service.actions;

import android.content.Context;

import com.tlabs.eve.EveRequest;
import com.tlabs.eve.EveResponse;
import com.tlabs.eve.api.character.CharacterContactListRequest;
import com.tlabs.eve.api.character.CharacterStandingsRequest;
import com.tlabs.eve.api.mail.Contact;
import com.tlabs.eve.api.mail.ContactListResponse;

public final class CharacterSocialAction extends EveAction {
    public CharacterSocialAction(final Context context, final long charID) {
        super(
                context,
                new CharacterContactListRequest(Long.toString(charID)),
                //new CharacterBookmarksRequest(Long.toString(charID)),
                new CharacterStandingsRequest(Long.toString(charID)));
    }

    @Override
    public EveAction onRequestCompleted(EveRequest<? extends EveResponse> request, EveResponse response) {
        if (response.getCached()) {
            return null;
        }
        if (request instanceof CharacterContactListRequest) {
            onActionCompleted((CharacterContactListRequest)request, (ContactListResponse)response);
            return null;
        }
        return null;
    }

    private void onActionCompleted(final CharacterContactListRequest request, final ContactListResponse response) {
        for (Contact.Group g: response.getContactGroups()) {
            mail.addContact(Long.parseLong(request.getCharacterID()), g);
        }
    }
}
