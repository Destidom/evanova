package com.tlabs.android.jeeves.model.data.evanova.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "contracts")
public class ContractEntity {

    @Id
    @GeneratedValue
    @Column
    private long id;

    @Column(nullable = false)
    private long contractID;

    @Column(nullable = false)
    private long ownerID;

    @Column
    private int type;

    @Column
    private int status;

    @Column
    private String title;

    @Column
    private String availability;

    @Column
    private long dateIssued;

    @Column
    private long dateExpired;

    @Column
    private long dateAccepted;

    @Column
    private long dateCompleted;

    @Column
    private int numDays;

    @Column
    private double price;

    @Column
    private double reward;

    @Column
    private double collateral;

    @Column
    private double buyout;

    @Column
    private double volume;

    @Column
    private long issuerID;

    @Column
    private long issuerCorpID;

    @Column
    private long assigneeID;

    @Column
    private long acceptorID;

    @Column
    private long startStationID;

    @Column
    private long endStationID;

    @Column
    private long forCorpID;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getContractID() {
        return contractID;
    }

    public void setContractID(long contractID) {
        this.contractID = contractID;
    }

    public long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(long ownerID) {
        this.ownerID = ownerID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public long getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(long dateIssued) {
        this.dateIssued = dateIssued;
    }

    public long getDateExpired() {
        return dateExpired;
    }

    public void setDateExpired(long dateExpired) {
        this.dateExpired = dateExpired;
    }

    public long getDateAccepted() {
        return dateAccepted;
    }

    public void setDateAccepted(long dateAccepted) {
        this.dateAccepted = dateAccepted;
    }

    public long getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(long dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public int getNumDays() {
        return numDays;
    }

    public void setNumDays(int numDays) {
        this.numDays = numDays;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public double getCollateral() {
        return collateral;
    }

    public void setCollateral(double collateral) {
        this.collateral = collateral;
    }

    public double getBuyout() {
        return buyout;
    }

    public void setBuyout(double buyout) {
        this.buyout = buyout;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public long getIssuerID() {
        return issuerID;
    }

    public void setIssuerID(long issuerID) {
        this.issuerID = issuerID;
    }

    public long getIssuerCorpID() {
        return issuerCorpID;
    }

    public void setIssuerCorpID(long issuerCorpID) {
        this.issuerCorpID = issuerCorpID;
    }

    public long getAssigneeID() {
        return assigneeID;
    }

    public void setAssigneeID(long assigneeID) {
        this.assigneeID = assigneeID;
    }

    public long getAcceptorID() {
        return acceptorID;
    }

    public void setAcceptorID(long acceptorID) {
        this.acceptorID = acceptorID;
    }

    public long getStartStationID() {
        return startStationID;
    }

    public void setStartStationID(long startStationID) {
        this.startStationID = startStationID;
    }

    public long getEndStationID() {
        return endStationID;
    }

    public void setEndStationID(long endStationID) {
        this.endStationID = endStationID;
    }

    public long getForCorpID() {
        return forCorpID;
    }

    public void setForCorpID(long forCorpID) {
        this.forCorpID = forCorpID;
    }

}
