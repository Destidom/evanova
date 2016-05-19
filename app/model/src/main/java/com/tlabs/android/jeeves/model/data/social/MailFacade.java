package com.tlabs.android.jeeves.model.data.social;

import com.tlabs.eve.api.mail.Contact;
import com.tlabs.eve.api.mail.MailMessage;
import com.tlabs.eve.api.mail.MailingList;
import com.tlabs.eve.api.mail.NotificationMessage;

import java.util.List;

public interface MailFacade {

    class Mailbox {

        private long id;

        private long readCount;
        private long totalCount;

        private String name;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getReadCount() {
            return readCount;
        }

        public void setReadCount(long readCount) {
            this.readCount = readCount;
        }

        public long getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(long totalCount) {
            this.totalCount = totalCount;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    void setup(long ownerID, List<MailingList> mailing);

    void setRead(final List<Long> messages, final boolean read);

    void addNotification(long ownerID, NotificationMessage m);
    void addMail(long ownerID, MailMessage m);
    void addContact(long ownerID, final Contact.Group group);

    void updateNotification(long ownerID, NotificationMessage m);//body
    void updateMail(long ownerID, MailMessage m);//body

    void deleteNotification(long ownerID, long messageID);
    void deleteMail(long ownerID, long messageID);

    NotificationMessage getNotification(final long messageID);
    MailMessage getMail(final long messageID);


    List<NotificationMessage> getNotifications(final long ownerID, final long mailboxID);
    List<MailMessage> getMails(final long ownerID, final long mailboxID);

    List<NotificationMessage> getNotificationsSince(final long ownerID, final long since);
    List<MailMessage> getMailsSince(final long ownerID, final long since);

    List<Contact> getContacts(final long ownerID, final String contactGroup);

    List<String> getContactTypes(long ownerID);
    List<Mailbox> getMailBoxes(long ownerID);
    List<Mailbox> getNotificationBoxes(long ownerID);
}
