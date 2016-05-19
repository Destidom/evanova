package com.tlabs.android.jeeves.model.data.sde.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "jeevesAttributes") //VIEW
public class AttributeEntity {

    @Column
    @Id
    private long attributeID;

    @Column
    private String attributeName;

    @Column
    private String description;

    @Column
    private float defaultValue;

    @Column(nullable = true)
    private Float valueFloat;

    @Column(nullable = true)
    private Integer valueInt;

    @Column
    private int published;

    @Column
    private String displayName;

    @Column
    private long unitID;

    @Column
    private int stackable;

    @Column
    private int highIsGood;

    @Column
    private long categoryID;

    @Column
    private String categoryName;

    @Column
    private long typeID;

    public long getAttributeID() {
        return attributeID;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public String getDescription() {
        return description;
    }

    public float getDefaultValue() {
        return defaultValue;
    }

    public Float getValueFloat() {
        return valueFloat;
    }

    public Integer getValueInt() {
        return valueInt;
    }

    public int getPublished() {
        return published;
    }

    public String getDisplayName() {
        return displayName;
    }

    public long getUnitID() {
        return unitID;
    }

    public int getStackable() {
        return stackable;
    }

    public int getHighIsGood() {
        return highIsGood;
    }

    public long getCategoryID() {
        return categoryID;
    }

    public long getTypeID() {
        return typeID;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
