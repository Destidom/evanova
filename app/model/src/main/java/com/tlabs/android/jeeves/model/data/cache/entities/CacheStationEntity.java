package com.tlabs.android.jeeves.model.data.cache.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "stations")
public class CacheStationEntity {

    @Id
    @Column
    private long id;

    @Column
    private long solarSystemID;

    @Column
    private long stationType;

    @Column
    private String stationName;

    @Column
    private long ownerID;

    @Column
    private String ownerName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSolarSystemID() {
        return solarSystemID;
    }

    public void setSolarSystemID(long solarSystemID) {
        this.solarSystemID = solarSystemID;
    }

    public long getStationType() {
        return stationType;
    }

    public void setStationType(long stationType) {
        this.stationType = stationType;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(long ownerID) {
        this.ownerID = ownerID;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
