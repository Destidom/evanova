package com.tlabs.android.jeeves.model.data.sde.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "invGroups")
public class GroupEntity {

    @Id
    @Column(name = "groupID")
    private long groupID;

    @Column
    private String groupName;

    @Column
    private long categoryID;

    private CategoryEntity category;

    @Column
    private int published;

    public long getGroupID() {
        return groupID;
    }

    public void setGroupID(long groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public long getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(long categoryID) {
        this.categoryID = categoryID;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public int getPublished() {
        return published;
    }
}

