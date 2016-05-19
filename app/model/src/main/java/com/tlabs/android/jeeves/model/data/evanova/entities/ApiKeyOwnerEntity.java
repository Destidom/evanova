package com.tlabs.android.jeeves.model.data.evanova.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

@Deprecated
@Entity(name = "key_owners")
public class ApiKeyOwnerEntity {

    @Column(nullable = false)
    private long ownerID;

    @Column(nullable = false)
    private long keyID;

    public long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(long ownerID) {
        this.ownerID = ownerID;
    }

    public long getKeyID() {
        return keyID;
    }

    public void setKeyID(long keyID) {
        this.keyID = keyID;
    }
}
