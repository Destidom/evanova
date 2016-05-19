package com.tlabs.android.jeeves.model.data.evanova.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Deprecated
@Entity(name = "token_owners")
public class ApiTokenEntity {

    @Id
    @Column(nullable = false)
    private long ownerID;

    @Column(nullable = false)
    private String token;

    public long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(long ownerID) {
        this.ownerID = ownerID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
