package com.tlabs.android.jeeves.model.data.sde.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "eveUnits")
public class UnitEntity {

    public static final UnitEntity NULL = new UnitEntity();
    static {
        NULL.setDescription("");
        NULL.setDisplay("");
        NULL.setName("");
        NULL.setId(0);
    }

    @Id
    @Column(name = "unitID")
    private long id;

    @Column(name = "unitName")
    private String name;

    @Column(name = "displayName")
    private String display;

    @Column
    private String description;

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

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
