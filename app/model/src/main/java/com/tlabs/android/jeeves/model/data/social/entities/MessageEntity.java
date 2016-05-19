package com.tlabs.android.jeeves.model.data.social.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

/*

    private static final String CREATE_MESSAGE = "CREATE TABLE IF NOT EXISTS " + TABLE_MESSAGE + " (_id integer primary key, " + //eve msg id
            "type_id INTEGER DEFAULT NULL," + //NULL when e-mail, type_id when notification
            "sender_id INTEGER NOT NULL," + "sender_name TEXT," + "sent_date DATETIME NOT NULL," + "title TEXT," + "body TEXT DEFAULT NULL)";


*/
@Entity(name = "message")
public class MessageEntity {
    @Id
    @Column(name = "_id")
    private long id;

    @Column(name = "type_id")
    private long typeID = -1;//0 when e-mail, type_id when notification

    @Column(name = "sender_id", nullable = false)
    private long senderID;

    @Column(name = "sender_name")
    private String senderName;

    @Column(name = "sent_date")
    private long date;

    @Column(name = "title")
    private String title;

    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    private byte[] body;

    @Column
    private int read;

    @Transient
    List<Long> mailboxes = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTypeID() {
        return typeID;
    }

    public void setTypeID(long typeID) {
        this.typeID = typeID;
    }

    public long getSenderID() {
        return senderID;
    }

    public void setSenderID(long senderID) {
        this.senderID = senderID;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return null == body ? "" : new String(body);
    }

    public void setBody(String body) {
        this.body = StringUtils.isEmpty(body) ? null : body.getBytes();
    }

    public List<Long> getMailboxes() {
        return mailboxes;
    }

    public void setMailboxes(List<Long> mailboxes) {
        this.mailboxes = mailboxes;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }
}
