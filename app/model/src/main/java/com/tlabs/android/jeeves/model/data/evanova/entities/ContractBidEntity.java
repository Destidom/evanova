package com.tlabs.android.jeeves.model.data.evanova.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "bids")
public class ContractBidEntity {

    @Id
    @Column
    private long bidID;

    @Column(nullable = false)
    private long ownerID;

    @Column(nullable = false)
    private long contractID;

    @Column(nullable = false)
    private long bidderID;

    @Column(nullable = false)
    private double bidAmount;

    @Column
    private long bidDate;

    public long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(long ownerID) {
        this.ownerID = ownerID;
    }

    public long getBidID() {
        return bidID;
    }

    public void setBidID(long bidID) {
        this.bidID = bidID;
    }

    public long getContractID() {
        return contractID;
    }

    public void setContractID(long contractID) {
        this.contractID = contractID;
    }

    public long getBidderID() {
        return bidderID;
    }

    public void setBidderID(long bidderID) {
        this.bidderID = bidderID;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(double bidAmount) {
        this.bidAmount = bidAmount;
    }

    public long getBidDate() {
        return bidDate;
    }

    public void setBidDate(long bidDate) {
        this.bidDate = bidDate;
    }
}
