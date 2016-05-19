package com.tlabs.android.jeeves.model.data.sde.entities;

import java.util.EnumSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "jeevesAgents")
public class AgentEntity {
    public enum Type {
        NON(1, "None"),
        BASIC(2, "Basic"),
        TUTORIAL(3, "Tutorial"),
        RESEARCH(4, "Research"),
        CONCORD(5, "CONCORD"),
        MISSION(6, "Generic Storyline"),
        STORY(7, "Storyline Mission"),
        EVENT(8, "Event MIssion"),
        FW(9, "Factional Warfare"),
        EPIC(10, "Epic Arc"),
        AURA(12, "Aura"),
        CAREER(12, "Career");

        private long type;
        private String typeName;

        Type(final long type, final String typeName) {
            this.type = type;
            this.typeName = typeName;
        }

        public static Type type(final long type) {
            for (Type t: EnumSet.allOf(Type.class)) {
                if (t.type == type) {
                    return t;
                }
            }
            return NON;
        }

        public static Type type(final String typeName) {
            for (Type t: EnumSet.allOf(Type.class)) {
                if (t.typeName.equals(typeName)) {
                    return t;
                }
            }
            return NON;
        }
    }

    @Id
    @Column(name = "_id")
    private long id;

    @Column
    private String name;

    @Column
    private long divisionID;

    @Column
    private long corporationID;

    @Column
    private long locationID;

    @Column
    private int level;

    @Column
    private int quality;

    @Column
    private long agentTypeID;

    @Column
    private boolean isLocator;

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

    public long getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(long divisionID) {
        this.divisionID = divisionID;
    }

    public long getCorporationID() {
        return corporationID;
    }

    public void setCorporationID(long corporationID) {
        this.corporationID = corporationID;
    }

    public long getLocationID() {
        return locationID;
    }

    public void setLocationID(long locationID) {
        this.locationID = locationID;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public long getAgentTypeID() {
        return agentTypeID;
    }

    public void setAgentTypeID(long agentTypeID) {
        this.agentTypeID = agentTypeID;
    }

    public boolean getLocator() {
        return isLocator;
    }

    public void setLocator(boolean isLocator) {
        this.isLocator = isLocator;
    }

    public String getAgentType() {
        return Type.type(this.agentTypeID).typeName;
    }

}

