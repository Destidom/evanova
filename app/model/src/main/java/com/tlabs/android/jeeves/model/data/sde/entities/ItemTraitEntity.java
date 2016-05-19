package com.tlabs.android.jeeves.model.data.sde.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity(name = "yamlTraits")
public class ItemTraitEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "_id")
    private long id;

    @Column
    private int rank;

    @Column
    private float bonus;

    @Column
    private String text;

    @Column
    private long typeID;

    @Column(name = "groupID")
    private long nameID;//name type = 0

    @Column
    private long unitID;

    @Transient
    private NameEntity name;

    @Transient
    private UnitEntity unit;

    public long getId() {
        return id;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public float getBonus() {
        return bonus;
    }

    public void setBonus(float bonus) {
        this.bonus = bonus;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getUnitID() {
        return unitID;
    }

    public void setUnitID(long unitID) {
        this.unitID = unitID;
    }

    public long getTypeID() {
        return typeID;
    }

    public void setTypeID(long typeID) {
        this.typeID = typeID;
    }

    public long getNameID() {
        return nameID;
    }

    public void setNameID(long nameID) {
        this.nameID = nameID;
    }

    public NameEntity getName() {
        return name;
    }

    public void setName(NameEntity name) {
        this.name = name;
    }

    public UnitEntity getUnit() {
        return unit;
    }

    public void setUnit(UnitEntity unit) {
        this.unit = unit;
    }
}
