package com.tlabs.android.jeeves.model.data.sde.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity(name = "jeevesItems")
public class ItemEntity {

    @Id
    @Column
    private long typeID;

    @Column
    private String typeName;

    @Column
    private long categoryID;

    @Column
    private String categoryName;

    @Column
    private long groupID;

    @Column
    private String groupName;

    @Column
    private String description;

    @Column
    private double mass;

    @Column
    private double volume;

    @Column
    private double capacity;

    @Column
    private int portionSize;

    @Column
    private int raceID;

    @Column
    private double basePrice;

    @Column
    private int published;

    @Column
    private long marketGroupID;

    @Column
    private String marketGroupName;

    @Column
    private String marketGroupDescription;

    @Column
    private long metaGroupID;

    @Column
    private String metaGroupName;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="typeID")
    private List<ItemTraitEntity> traits = new ArrayList<>();

    public long getTypeID() {
        return typeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public long getMetaGroupID() {
        return metaGroupID;
    }


    public String getDescription() {
        return description;
    }

    public double getMass() {
        return mass;
    }

    public double getVolume() {
        return volume;
    }

    public double getCapacity() {
        return capacity;
    }

    public int getPortionSize() {
        return portionSize;
    }

    public int getRaceID() {
        return raceID;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public long getMarketGroupID() {
        return marketGroupID;
    }

    public List<ItemTraitEntity> getTraits() {
        return traits;
    }

    public int getPublished() {
        return published;
    }

    public long getCategoryID() {
        return categoryID;
    }

    public long getGroupID() {
        return groupID;
    }
    public String getCategoryName() {
        return categoryName;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getMarketGroupName() {
        return marketGroupName;
    }

    public String getMarketGroupDescription() {
        return marketGroupDescription;
    }

    public String getMetaGroupName() {
        return metaGroupName;
    }
}
