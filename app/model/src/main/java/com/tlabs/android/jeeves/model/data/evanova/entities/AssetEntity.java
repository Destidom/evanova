package com.tlabs.android.jeeves.model.data.evanova.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "assets")
public class AssetEntity {

    @Id
    @GeneratedValue
    @Column
    private long id;

    @Column
    private long ownerID;

    @Column
    private long typeID;

    @Column
    private long categoryID;

    @Column
    private long marketGroupID;

    @Column
    private long locationID;

    @Column
    private int inventoryFlag;

    @Column
    private double quantity;

    @Column
    private double rawQuantity;

    @Column
    private int packaged;

    @Column
    private long parentID;//assets._id in case of a contained item.

    private List<AssetEntity> assets;

    public long getId() {
        return id;
    }

    public long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(long ownerID) {
        this.ownerID = ownerID;
    }

    public long getTypeID() {
        return typeID;
    }

    public void setTypeID(long typeID) {
        this.typeID = typeID;
    }

    public long getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(long categoryID) {
        this.categoryID = categoryID;
    }

    public long getMarketGroupID() {
        return marketGroupID;
    }

    public void setMarketGroupID(long marketGroupID) {
        this.marketGroupID = marketGroupID;
    }

    public long getLocationID() {
        return locationID;
    }

    public void setLocationID(long locationID) {
        this.locationID = locationID;
    }

    public int getInventoryFlag() {
        return inventoryFlag;
    }

    public void setInventoryFlag(int inventoryFlag) {
        this.inventoryFlag = inventoryFlag;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getPackaged() {
        return packaged;
    }

    public void setPackaged(int packaged) {
        this.packaged = packaged;
    }

    public long getParentID() {
        return parentID;
    }

    public void setParentID(long parentID) {
        this.parentID = parentID;
    }

    public double getRawQuantity() {
        return rawQuantity;
    }

    public void setRawQuantity(double rawQuantity) {
        this.rawQuantity = rawQuantity;
    }

    public List<AssetEntity> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetEntity> assets) {
        this.assets = assets;
    }
}
