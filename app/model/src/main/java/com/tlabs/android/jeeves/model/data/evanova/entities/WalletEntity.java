package com.tlabs.android.jeeves.model.data.evanova.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "wallet")
public class WalletEntity {

    @Id
    @GeneratedValue
    @Column
    private long id;

    @Column(nullable = false)
    private long ownerID;

    @Column
    private long division;

    @Column
    private double balance;

    @Column
    private long updated;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(long ownerID) {
        this.ownerID = ownerID;
    }

    public long getDivision() {
        return division;
    }

    public void setDivision(long division) {
        this.division = division;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }
}
