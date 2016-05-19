package com.tlabs.android.jeeves.model.data.social.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

/*

    //message distribution
    private static final String CREATE_MESSAGES = "CREATE TABLE IF NOT EXISTS " + TABLE_MESSAGES + " (message_id INTEGER NOT NULL," + "mailbox_id INTEGER NOT NULL,"
            + "owner_id INTEGER NOT NULL," + "read INTEGER DEFAULT 0," + "UNIQUE (owner_id, message_id, mailbox_id)," + "FOREIGN KEY(mailbox_id) REFERENCES " + TABLE_LIST
            + "(_id)," + "FOREIGN KEY(message_id) REFERENCES " + TABLE_MESSAGE + "(_id))";

*/
@Entity(name = "messages")
public class MessageListEntity {

    @Column(name = "message_id", nullable = false)
    private long messageID;

    @Column(name = "mailbox_id", nullable =  false)
    private long mailboxID;

    @Column(name = "owner_id", nullable = false)
    private long ownerID;

    public long getMessageID() {
        return messageID;
    }

    public void setMessageID(long messageID) {
        this.messageID = messageID;
    }

    public long getMailboxID() {
        return mailboxID;
    }

    public void setMailboxID(long mailboxID) {
        this.mailboxID = mailboxID;
    }

    public long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(long ownerID) {
        this.ownerID = ownerID;
    }

}
