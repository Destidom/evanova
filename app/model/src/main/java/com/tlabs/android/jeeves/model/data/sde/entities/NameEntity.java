package com.tlabs.android.jeeves.model.data.sde.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "jeevesNames")
public class NameEntity {
    public static final int TYPE_ITEM = 0;//type_id
    public static final int TYPE_CATEGORY = 1;//category_id

    @Column(name = "name_type")
    private long nameType;

    @Column(name = "name_id")
    private long nameID;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private long published;

    public long getNameType() {
        return nameType;
    }

    public void setNameType(long nameType) {
        this.nameType = nameType;
    }

    public long getNameID() {
        return nameID;
    }

    public void setNameID(long nameID) {
        this.nameID = nameID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPublished() {
        return published;
    }

    public void setPublished(long published) {
        this.published = published;
    }
}
