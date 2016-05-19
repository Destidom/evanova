package com.tlabs.android.jeeves.model.data.evanova.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "skills")
public class CharacterSkillEntity {

    @Id
    @GeneratedValue
    @Column
    private long id;

    @Column(nullable = false)
    private long ownerID;

    @Column(nullable = false)
    private long skillID;

    @Column
    private int skillLevel;

    @Column
    private long startPoints;

    @Column
    private long endPoints;

    @Column
    private long startTime;

    @Column
    private long endTime;

    @Column
    private int status;//SkillInTraining.TYPE

    @Column
    private int position;//when in queue or plan

    public long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(long ownerID) {
        this.ownerID = ownerID;
    }

    public long getSkillID() {
        return skillID;
    }

    public void setSkillID(long skillID) {
        this.skillID = skillID;
    }

    public int getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(int skillLevel) {
        this.skillLevel = skillLevel;
    }

    public long getStartPoints() {
        return startPoints;
    }

    public void setStartPoints(long startPoints) {
        this.startPoints = startPoints;
    }

    public long getEndPoints() {
        return endPoints;
    }

    public void setEndPoints(long endPoints) {
        this.endPoints = endPoints;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
