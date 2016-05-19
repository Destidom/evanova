package com.tlabs.android.jeeves.service.actions;

import android.content.Context;

import com.tlabs.eve.EveRequest;
import com.tlabs.eve.EveResponse;
import com.tlabs.eve.api.character.CharacterContactListRequest;
import com.tlabs.eve.api.character.CharacterContactNotificationsRequest;
import com.tlabs.eve.api.mail.Contact;
import com.tlabs.eve.api.mail.ContactListResponse;
import com.tlabs.eve.api.mail.ContactNotificationsResponse;
import com.tlabs.eve.api.mail.MailMessage;
import com.tlabs.eve.api.mail.MailMessagesRequest;
import com.tlabs.eve.api.mail.MailMessagesResponse;
import com.tlabs.eve.api.mail.NotificationMessage;
import com.tlabs.eve.api.mail.NotificationsRequest;
import com.tlabs.eve.api.mail.NotificationsResponse;
import com.tlabs.eve.zkb.ZKillCharacterLogRequest;

import java.util.List;

public final class CharacterMailboxAction extends EveAction {
    
    private final long ownerID;

    public CharacterMailboxAction(final Context context, final long ownerID) {
        super(
                context,
                new MailMessagesRequest(Long.toString(ownerID)),
                new NotificationsRequest(Long.toString(ownerID)),
                new CharacterContactNotificationsRequest(Long.toString(ownerID)),
                new ZKillCharacterLogRequest(ownerID));
        this.ownerID = ownerID;
    }

    @Override
    public EveAction onRequestCompleted(EveRequest<? extends EveResponse> request, EveResponse response) {
        if (response.getCached()) {
            return null;
        }
        if (request instanceof MailMessagesRequest) {
            onActionCompleted((MailMessagesRequest)request, (MailMessagesResponse)response);
            return null;
        }
        if (request instanceof NotificationsRequest) {
            onActionCompleted((NotificationsRequest)request, (NotificationsResponse)response);
            return null;
        }

        if (request instanceof CharacterContactListRequest) {
            onActionCompleted((CharacterContactListRequest)request, (ContactListResponse)response);
            return null;
        }

        if (request instanceof CharacterContactNotificationsRequest) {
            onActionCompleted((CharacterContactNotificationsRequest)request, (ContactNotificationsResponse)response);
            return null;
        }
        return null;
    }
    
    private void onActionCompleted(final MailMessagesRequest request, final MailMessagesResponse response) {
        final List<MailMessage> messages = response.getMessages();
        if (messages.isEmpty()) {
            return;
        }
        for (MailMessage m : messages) {
            mail.addMail(ownerID, m);
        }
    }
    
    private void onActionCompleted(final NotificationsRequest request, final NotificationsResponse response) {
        for (NotificationMessage m: response.getMessages()) {
            mail.addNotification(ownerID, m);
        }
    }

    private void onActionCompleted(final CharacterContactListRequest request, final ContactListResponse response) {
        for (Contact.Group g: response.getContactGroups()) {
            mail.addContact(ownerID, g);
        }
    }

    private void onActionCompleted(final CharacterContactNotificationsRequest request, final ContactNotificationsResponse response) {
        for (NotificationMessage m: response.getMessages()) {
            mail.addNotification(ownerID, m);
        }
    }
}
