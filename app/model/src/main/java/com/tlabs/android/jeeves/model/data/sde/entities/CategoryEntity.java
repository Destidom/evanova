package com.tlabs.android.jeeves.model.data.sde.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "invCategories")
public class CategoryEntity {

    @Id
    @Column(name = "categoryID")
    private long categoryID;

    @Column
    private String categoryName;

    @Column
    private int published;

    public long getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(long categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}

