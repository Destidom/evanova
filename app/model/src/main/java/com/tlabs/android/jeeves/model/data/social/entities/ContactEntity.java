package com.tlabs.android.jeeves.model.data.social.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/*
private static final String CREATE_CONTACT = "CREATE TABLE IF NOT EXISTS " + TABLE_CONTACT +
        "(_id integer primary key autoincrement," +
        "owner_id INTEGER NOT NULL," +
        "contact_id INTEGER NOT NULL," +
        "contact_type INTEGER NOT NULL," +
        "contact_name TEXT DEFAULT NULL," +
        "contact_group TEXT DEFAULT NULL," +
        "watched INTEGER DEFAULT 0," +
        "standing REAL DEFAULT 0," +
        "UNIQUE (owner_id, contact_id))";
*/
@Entity(name = "contact")
public class ContactEntity {

    @Id
    @GeneratedValue
    @Column(name = "_id")
    private long id;

    @Column(name = "owner_id")
    private long ownerID;

    @Column(name = "contact_id")
    private long contactID;

    @Column(name = "contact_type")
    private long contactType;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "contact_group")
    private String contactGroup;

    @Column(name = "watched")
    private int watched;

    @Column(name = "standing")
    private float standing;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(long ownerID) {
        this.ownerID = ownerID;
    }

    public long getContactID() {
        return contactID;
    }

    public void setContactID(long contactID) {
        this.contactID = contactID;
    }

    public long getContactType() {
        return contactType;
    }

    public void setContactType(long contactType) {
        this.contactType = contactType;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactGroup() {
        return contactGroup;
    }

    public void setContactGroup(String contactGroup) {
        this.contactGroup = contactGroup;
    }

    public int getWatched() {
        return watched;
    }

    public void setWatched(int watched) {
        this.watched = watched;
    }

    public float getStanding() {
        return standing;
    }

    public void setStanding(float standing) {
        this.standing = standing;
    }
}
