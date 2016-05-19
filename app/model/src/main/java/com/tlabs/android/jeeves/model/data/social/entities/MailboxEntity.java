package com.tlabs.android.jeeves.model.data.social.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

/*
    private static final String CREATE_LIST = "CREATE TABLE IF NOT EXISTS " + TABLE_LIST + "(_id integer INTEGER NOT NULL, " + //eve id or internal one
            "owner_id INTEGER NOT NULL," + //char id | corp id
            "name TEXT," + "visible INTEGER DEFAULT 1," + //0=invis; 1 = vis
            "UNIQUE (owner_id, _id))";
*/
@Entity(name = "list")
public class MailboxEntity {

    @Column(name = "mailbox_id", nullable = false)
    private long mailboxID;

    @Column(name = "owner_id", nullable = false)
    private long ownerID;

    @Column
    private String name;

    @Column
    private int visible = 1;

    @Transient
    private long readCount;

    @Transient
    private long totalCount;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
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
}
