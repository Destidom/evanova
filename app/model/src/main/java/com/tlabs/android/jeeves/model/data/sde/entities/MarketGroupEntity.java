package com.tlabs.android.jeeves.model.data.sde.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "invMarketGroups")
public class MarketGroupEntity {

    @Id
    @Column
    private long marketGroupID;

    @Column(nullable = true)
    private Long parentGroupID;

    @Column
    private String marketGroupName;

    @Column
    private String description;

    @Column
    private long iconID;

    @Column
    private boolean hasTypes;

    public long getMarketGroupID() {
        return marketGroupID;
    }

    public void setMarketGroupID(long marketGroupID) {
        this.marketGroupID = marketGroupID;
    }

    public Long getParentGroupID() {
        return parentGroupID;
    }

    public void setParentGroupID(Long parentGroupID) {
        this.parentGroupID = parentGroupID;
    }

    public String getMarketGroupName() {
        return marketGroupName;
    }

    public void setMarketGroupName(String marketGroupName) {
        this.marketGroupName = marketGroupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getIconID() {
        return iconID;
    }

    public void setIconID(long iconID) {
        this.iconID = iconID;
    }

    public boolean isHasTypes() {
        return hasTypes;
    }

    public void setHasTypes(boolean hasTypes) {
        this.hasTypes = hasTypes;
    }
}
