package com.tlabs.android.jeeves.model.data.evanova.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "accounts")
public class AccountEntity {

    @Id
    @GeneratedValue
    @Column
    private long id;

    //internal value; 0 = char; 1 = corp; 2 = account; -1 = unknown; -n: whatever other source
    @Column
    private int type;

    @Column
    private String name;

    @Column
    private long ownerID;

    @Column
    private long keyID;

    @Column
    private String keyValue;

    @Column
    private String accessToken;


    @Column
    private String refreshToken;

    @Column
    private long mask;

    @Column
    private int status;

    @Column
    private String statusMessage;

    @Column
    private long expires;

    @Column
    private long created;

    @Column
    private long paidUntil;

    @Column
    private long logonCount;

    @Column
    private long logonMinutes;

    @Column
    private int sortRank;

    public long getKeyID() {
        return keyID;
    }

    public void setKeyID(long keyID) {
        this.keyID = keyID;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public long getMask() {
        return mask;
    }

    public void setMask(long mask) {
        this.mask = mask;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getPaidUntil() {
        return paidUntil;
    }

    public void setPaidUntil(long paidUntil) {
        this.paidUntil = paidUntil;
    }

    public long getLogonCount() {
        return logonCount;
    }

    public void setLogonCount(long logonCount) {
        this.logonCount = logonCount;
    }

    public long getLogonMinutes() {
        return logonMinutes;
    }

    public void setLogonMinutes(long logonMinutes) {
        this.logonMinutes = logonMinutes;
    }

    public int getSortRank() {
        return sortRank;
    }

    public void setSortRank(int sortRank) {
        this.sortRank = sortRank;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(long ownerID) {
        this.ownerID = ownerID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
