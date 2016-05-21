package com.tlabs.android.jeeves.model.data.evanova.entities;



import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "clones")
public class CharacterCloneEntity {

    @Id
    @GeneratedValue
    @Column
    private long id;

    @Column(nullable = false)
    private long ownerID;

    @Column(nullable = false)
    private long cloneID;

    @Column
    private String cloneName;

    @Column
    private long locationID;

    @Column
    private String implants;//typeid; typeid...

    public long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(long ownerID) {
        this.ownerID = ownerID;
    }

    public long getCloneID() {
        return cloneID;
    }

    public void setCloneID(long cloneID) {
        this.cloneID = cloneID;
    }

    public String getCloneName() {
        return cloneName;
    }

    public void setCloneName(String cloneName) {
        this.cloneName = cloneName;
    }

    public long getLocationID() {
        return locationID;
    }

    public void setLocationID(long locationID) {
        this.locationID = locationID;
    }

    public void setImplants(List<Long> implants) {
        if ((null == implants) || implants.isEmpty()) {
            this.implants = null;
            return;
        }
        final StringBuilder b = new StringBuilder();
        for (Long l: implants) {
            b.append(Long.toString(l));
            b.append(";");
        }
        this.implants = StringUtils.removeEnd(b.toString(), ";");
    }

    public List<Long> getImplants() {
        final String[] ids = StringUtils.split(this.implants, ";");
        if (ArrayUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        final List<Long> implants = new ArrayList<>(ids.length);
        for (String id: ids) {
            implants.add(Long.parseLong(id));
        }
        return implants;
    }
}
