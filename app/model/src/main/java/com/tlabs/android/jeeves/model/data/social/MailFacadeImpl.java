package com.tlabs.android.jeeves.model.data.social;

import com.tlabs.android.jeeves.model.data.evanova.EvanovaDatabase;
import com.tlabs.android.jeeves.model.data.social.entities.ContactEntity;
import com.tlabs.android.jeeves.model.data.social.entities.MessageEntity;
import com.tlabs.eve.api.mail.Contact;
import com.tlabs.eve.api.mail.MailMessage;
import com.tlabs.eve.api.mail.MailingList;
import com.tlabs.eve.api.mail.NotificationMessage;

import java.util.ArrayList;
import java.util.List;

public class MailFacadeImpl implements MailFacade {


    private final MailDatabase mail;
    private final EvanovaDatabase evanova;

    public MailFacadeImpl(
            final MailDatabase mail,
            final EvanovaDatabase evanova) {
        this.mail = mail;
        this.evanova = evanova;
    }

    @Override
    public void setup(long ownerID, List<MailingList> mailing) {
        for (MailingList l: defaultMailing()) {
            this.mail.setupMailbox(MailEntities.transform(ownerID, l));
        }
        for (MailingList l: mailing) {
            this.mail.setupMailbox(MailEntities.transform(ownerID, l));
        }
    }

    @Override
    public List<NotificationMessage> getNotifications(long ownerID, long mailboxID) {
        final List<MessageEntity> entities = this.mail.listMessages(ownerID, mailboxID);
        final List<NotificationMessage> messages = new ArrayList<>(entities.size());
        for (MessageEntity e: entities) {
            messages.add(MailEntities.transformNotification(e));
        }
        return messages;
    }

    @Override
    public List<NotificationMessage> getNotificationsSince(long ownerID, long since) {
        final List<MessageEntity> entities = this.mail.listNotificationsSince(ownerID, since);
        final List<NotificationMessage> messages = new ArrayList<>(entities.size());
        for (MessageEntity e: entities) {
            messages.add(MailEntities.transformNotification(e));
        }
        return messages;
    }

    @Override
    public void addNotification(long ownerID, NotificationMessage m) {
        MessageEntity entity = MailEntities.transform(m, ownerID, evanova.getCorporationId(ownerID));
        this.mail.saveMessage(ownerID, entity);
        m.setTitle(entity.getTitle());
    }

    @Override
    public void updateNotification(long ownerID, NotificationMessage m) {
        this.mail.updateBody(MailEntities.transform(m, ownerID, evanova.getCorporationId(ownerID)));
    }

    @Override
    public void addMail(long ownerID, MailMessage m) {
        this.mail.saveMessage(ownerID, MailEntities.transform(m, ownerID, evanova.getCorporationId(ownerID)));
    }

    @Override
    public void updateMail(long ownerID, MailMessage m) {
        this.mail.updateBody(MailEntities.transform(m, ownerID, evanova.getCorporationId(ownerID)));
    }

    @Override
    public List<MailMessage> getMails(long ownerID, long mailboxID) {
        final List<MessageEntity> entities = this.mail.listMessages(ownerID, mailboxID);
        final List<MailMessage> messages = new ArrayList<>(entities.size());
        for (MessageEntity e: entities) {
            messages.add(MailEntities.transformMail(e));
        }
        return messages;
    }

    @Override
    public List<MailMessage> getMailsSince(long ownerID, long since) {
        final List<MessageEntity> entities = this.mail.listMailsSince(ownerID, since);
        final List<MailMessage> messages = new ArrayList<>(entities.size());
        for (MessageEntity e: entities) {
            messages.add(MailEntities.transformMail(e));
        }
        return messages;
    }

    @Override
    public void addContact(long ownerID, Contact.Group group) {
        for (ContactEntity c: MailEntities.transform(ownerID, group)) {
            this.mail.save(c);
        }
    }

    @Override
    public void setRead(List<Long> messages, boolean read) {
        this.mail.markAsRead(messages, read);
    }

    @Override
    public void deleteNotification(long ownerID, long messageID) {
        this.mail.delete(ownerID, messageID);
    }

    @Override
    public NotificationMessage getNotification(long messageID) {
        return MailEntities.transformNotification(this.mail.get(messageID));
    }

    @Override
    public void deleteMail(long ownerID, long messageID) {
        this.mail.delete(ownerID, messageID);
    }

    @Override
    public MailMessage getMail(long messageID) {
        return MailEntities.transformMail(this.mail.get(messageID));
    }

    @Override
    public List<String> getContactTypes(long ownerID) {
        return this.mail.listContactTypes(ownerID);
    }

    @Override
    public List<Mailbox> getMailBoxes(long ownerID) {
        final List<Mailbox> returned = new ArrayList<>();
        returned.addAll(MailEntities.transform(this.mail.listMailBoxes(ownerID)));
        returned.addAll(MailEntities.transform(this.mail.listMailingListBoxes(ownerID)));
        return returned;
    }

    @Override
    public List<Mailbox> getNotificationBoxes(long ownerID) {
        return MailEntities.transform(this.mail.listNotificationBoxes(ownerID));
    }

    @Override
    public List<Contact> getContacts(long ownerID, String contactGroup) {
        final List<ContactEntity> contacts = this.mail.getContacts(ownerID, contactGroup);
        final List<Contact> returned = new ArrayList<>(contacts.size());
        for (ContactEntity e: contacts) {
            returned.add(MailEntities.transform(e));
        }
        return returned;
    }

    private List<MailingList> defaultMailing() {
        List<MailingList> lists = new ArrayList<>();
        for (NotificationResource.Group g: NotificationResource.getGroups()) {
            final MailingList ml = new MailingList();
            ml.setDisplayName(g.getText());
            ml.setListID(g.getId());
            lists.add(ml);
        }

        return lists;
    }
}
