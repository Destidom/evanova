package com.tlabs.android.jeeves.model.data.evanova.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "items")
public class ContractItemEntity {

    @Id
    @Column
    private long recordID;

    @Column
    private long ownerID;

    @Column
    private long contractID;

    @Column
    private long itemID;

    @Column
    private double quantity;

    @Column
    private double rawQuantity;

    //This attribute will only show up if the quantity is a negative number in the DB.
    //Negative quantities are in fact codes, -1 indicates that the item is a singleton (non-stackable).
    //If the item happens to be a Blueprint, -1 is an Original and -2 is a Blueprint Copy.
    @Column
    private int raw;

    @Column
    private int included;


    public long getRecordID() {
        return recordID;
    }

    public void setRecordID(long recordID) {
        this.recordID = recordID;
    }

    public long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(long ownerID) {
        this.ownerID = ownerID;
    }

    public long getContractID() {
        return contractID;
    }

    public void setContractID(long contractID) {
        this.contractID = contractID;
    }

    public long getItemID() {
        return itemID;
    }

    public void setItemID(long itemID) {
        this.itemID = itemID;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getRawQuantity() {
        return rawQuantity;
    }

    public void setRawQuantity(double rawQuantity) {
        this.rawQuantity = rawQuantity;
    }

    public int getRaw() {
        return raw;
    }

    public void setRaw(int raw) {
        this.raw = raw;
    }

    public int getIncluded() {
        return included;
    }

    public void setIncluded(int included) {
        this.included = included;
    }
}
