package com.tlabs.android.jeeves.model.data.social;

import com.tlabs.android.jeeves.model.data.social.entities.ContactEntity;
import com.tlabs.android.jeeves.model.data.social.entities.MailboxEntity;
import com.tlabs.android.jeeves.model.data.social.entities.MessageEntity;
import com.tlabs.eve.api.mail.Contact;
import com.tlabs.eve.api.mail.MailMessage;
import com.tlabs.eve.api.mail.MailingList;
import com.tlabs.eve.api.mail.Message;
import com.tlabs.eve.api.mail.NotificationMessage;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class MailEntities {

    private MailEntities() {}

    public static MessageEntity transform(final MailMessage message, final long ownerID, final long ownerCorp) {
        final MessageEntity entity = transformImpl(message);
        entity.setId(message.getMessageID());
        entity.setMailboxes(getMailboxes(message, ownerID, ownerCorp));
        return entity;
    }

    public static MessageEntity transform(final NotificationMessage message, final long ownerID, final long ownerCorp) {
        final MessageEntity entity = transformImpl(message);
        entity.setId(message.getNotificationID());
        entity.setTypeID(message.getTypeID());

        final NotificationResource.Title title = NotificationResource.getTitle(message.getTypeID());
        if (null == title) {
            entity.setTitle("Notification Type " + message.getTypeID());
            entity.setMailboxes(Collections.singletonList(-885l));
        }
        else {
            entity.setTitle(title.getText());
            entity.setMailboxes(Collections.singletonList(title.getGroup()));
        }

        message.setTitle(entity.getTitle());
        return entity;
    }

    public static List<ContactEntity> transform(final long ownerID, final Contact.Group group) {
        final List<ContactEntity> entities = new ArrayList<>(group.getContacts().size());
        for (Contact c: group.getContacts()) {
            entities.add(transform(ownerID, group, c));
        }
        return entities;
    }

    public static ContactEntity transform(final long ownerID, final Contact.Group group, final Contact contact) {
        final ContactEntity entity = new ContactEntity();
        entity.setContactID(contact.getContactID());
        entity.setContactGroup(group.getName());
        entity.setContactName(contact.getContactName());
        entity.setStanding(contact.getStanding());
        entity.setWatched(contact.getInWatchlist() ? 1 : 0);
        entity.setOwnerID(ownerID);
        return entity;
    }

    public static Contact transform(final ContactEntity entity) {
        final Contact contact = new Contact();
        contact.setContactID(entity.getContactID());
        contact.setContactName(entity.getContactName());
        contact.setContactTypeID(entity.getContactType());
        contact.setInWatchlist(entity.getWatched() == 1);
        contact.setStanding(entity.getStanding());
        return contact;
    }

    public static MailboxEntity transform(final long ownerID, final MailingList list) {
        final MailboxEntity entity = new MailboxEntity();
        entity.setMailboxID(list.getListID());
        entity.setName(list.getDisplayName());
        entity.setOwnerID(ownerID);
        entity.setVisible(1);
        return entity;
    }

    public static List<MailFacade.Mailbox> transform(final List<MailboxEntity> entities) {
        final List<MailFacade.Mailbox> mailboxes = new ArrayList<>();
        for (MailboxEntity e: entities) {
            mailboxes.add(transform(e));
        }
        return mailboxes;
    }

    public static MailFacade.Mailbox transform(final MailboxEntity entity) {
        final MailFacade.Mailbox mailbox = new MailFacade.Mailbox();
        mailbox.setTotalCount(entity.getTotalCount());
        mailbox.setReadCount(entity.getReadCount());
        mailbox.setName(entity.getName());
        mailbox.setId(entity.getMailboxID());
        return mailbox;
    }


    public static MailMessage transformMail(final MessageEntity entity) {
        if (null == entity) {
            return null;
        }
        final MailMessage message = new MailMessage();
        message.setMessageID(entity.getId());
        return transformImpl(entity, message);
    }

    public static NotificationMessage transformNotification(final MessageEntity entity) {
        if (null == entity) {
            return null;
        }
        final NotificationMessage message = new NotificationMessage();
        message.setNotificationID(entity.getId());
        message.setTypeID((int)entity.getTypeID());
        return transformImpl(entity, message);
    }

    private static MessageEntity transformImpl(final Message message) {
        final MessageEntity entity = new MessageEntity();
        entity.setBody(message.getBody());
        entity.setDate(message.getSentDate());
        entity.setSenderID(message.getSenderID());
        entity.setSenderName(message.getSenderName());
        entity.setTitle(message.getTitle());
        return entity;
    }

    public static <T extends Message> T transformImpl(final MessageEntity entity, final T message) {
        if (null == entity) {
            return null;
        }
        message.setSenderName(entity.getSenderName());
        message.setBody(entity.getBody());
        message.setSentDate(entity.getDate());
        message.setSenderID(entity.getSenderID());
        message.setTitle(entity.getTitle());
        message.setRead(entity.getRead() == 1);
        return message;
    }

    static final long INBOX_ID = -999;
    static final long OUTBOX_ID = -998;
    static final long CORP_ID = -997;
    static final long ALLIANCE_ID = -996;
    static final long OTHERS_ID = -995;

    private static List<Long> getMailboxes(final MailMessage message, final long ownerID, final long ownerCorp) {
        final List<Long> boxes = new ArrayList<>();
        if (message.getSenderID() == ownerID) {
            boxes.add(OUTBOX_ID);
        }
        if (message.getToListID() > 0) {
            boxes.add(message.getToListID());
        }

        for (String s: StringUtils.split(message.getToCharacterIDs(), ",")) {
            if (s.equals(Long.toString(ownerID))) {
                boxes.add(INBOX_ID);
                break;
            }
        }
        if (message.getToCorpOrAllianceID() > 0) {
            if (message.getSenderID() == ownerCorp || message.getToCorpOrAllianceID() == ownerCorp) {
                boxes.add(CORP_ID);
            }
            else {
                boxes.add(ALLIANCE_ID);
            }
        }
        if (boxes.isEmpty()) {
            boxes.add(OTHERS_ID);
        }
        return boxes;
    }
}
