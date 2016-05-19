package com.tlabs.android.jeeves.model.data.sde.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "jeevesLocations")
public class LocationEntity {

    @Id
    @Column(name = "_id")
    private long id;

    @Column
    private String name;

    @Column
    private long typeID;

    @Column
    private long solarSystemID;

    @Column
    private float security;

    @Column
    private long regionID;

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

    public long getSolarSystemID() {
        return solarSystemID;
    }

    public void setSolarSystemID(long solarSystemID) {
        this.solarSystemID = solarSystemID;
    }

    public float getSecurity() {
        return security;
    }

    public void setSecurity(float security) {
        this.security = security;
    }

    public long getRegionID() {
        return regionID;
    }

    public void setRegionID(long regionID) {
        this.regionID = regionID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
