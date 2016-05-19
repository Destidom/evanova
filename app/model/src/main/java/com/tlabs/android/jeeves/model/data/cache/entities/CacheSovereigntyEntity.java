package com.tlabs.android.jeeves.model.data.cache.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "sovereignty")
public class CacheSovereigntyEntity {

    @Id
    @Column
    private long id;//solar_system_id

    @Column
    private long corpID = -1;

    @Column
    private long allianceID = -1;

    @Column
    private long factionID = -1;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCorpID() {
        return corpID;
    }

    public void setCorpID(long corpID) {
        this.corpID = corpID;
    }

    public long getAllianceID() {
        return allianceID;
    }

    public void setAllianceID(long allianceID) {
        this.allianceID = allianceID;
    }

    public long getFactionID() {
        return factionID;
    }

    public void setFactionID(long factionID) {
        this.factionID = factionID;
    }
}
