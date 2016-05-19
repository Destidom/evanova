package com.tlabs.android.jeeves.model.data.evanova.entities;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "chars")
public class CharacterEntity {
    @Id
    @Column
    private long characterID;

    @Column
    private String name;

    @Column
    private long corporationID;

    @Column
    private String corporationName;

    @Column
    private long corporationJoined;

    @Column
    private String corporationTitles;

    @Column
    private String corporationRoles;

    @Column
    private long allianceID;

    @Column
    private String allianceName;

    @Column
    private long allianceJoined;

    @Column
    private double balance;

    @Column
    private String race;

    @Column
    private String gender;

    @Column
    private String bloodLine;

    //computed from skills when added to a Character, or from Char Info if enough API key rights
    @Column
    private long skillPoints;

    @Column
    private int intelligence;

    @Column
    private int memory;

    @Column
    private int perception;

    @Column
    private int charisma;

    @Column
    private int willpower;

    @Column
    private String certificates;

    @Column
    private String location;

    @Column
    private long locationID;

    @Column
    private float security;

    @Column
    private String shipName;

    @Column
    private String shipTypeName;

    @Column
    private long shipTypeID;

    @Column
    private String ancestry;

    @Column
    private long logonDateTime;

    @Column
    private long logoffDateTime;

    @Column
    private long cloneJumpDate;

    @Column
    private long lastRespecDate;

    @Column
    private long lastTimedRespec;

    @Column
    private long jumpLastUpdate;

    @Column
    private long jumpActivation;

    @Column
    private long jumpFatigue;

    @Column
    private long remoteStationDate;

    @Column
    private long birth;

    @Column
    private int grantableRoles;

    @Column
    private int visible;

    @Column
    private int freeRespecs;

    @Column
    private long freeSkillPoints;

    @Column
    private String implants;//typeid;

    @Column
    private String history;//corpid:date

    @Column
    private String skills;//skillid:level

    @Column
    private int sortRank;

    public long getCharacterID() {
        return characterID;
    }

    public void setCharacterID(long characterID) {
        this.characterID = characterID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCorporationID() {
        return corporationID;
    }

    public void setCorporationID(long corporationID) {
        this.corporationID = corporationID;
    }

    public String getCorporationName() {
        return corporationName;
    }

    public void setCorporationName(String corporationName) {
        this.corporationName = corporationName;
    }

    public long getCorporationJoined() {
        return corporationJoined;
    }

    public void setCorporationJoined(long corporationJoined) {
        this.corporationJoined = corporationJoined;
    }

    public String getCorporationTitles() {
        return corporationTitles;
    }

    public void setCorporationTitles(String corporationTitles) {
        this.corporationTitles = corporationTitles;
    }

    public String getCorporationRoles() {
        return corporationRoles;
    }

    public void setCorporationRoles(String corporationRoles) {
        this.corporationRoles = corporationRoles;
    }

    public long getAllianceID() {
        return allianceID;
    }

    public void setAllianceID(long allianceID) {
        this.allianceID = allianceID;
    }

    public String getAllianceName() {
        return allianceName;
    }

    public void setAllianceName(String allianceName) {
        this.allianceName = allianceName;
    }

    public long getAllianceJoined() {
        return allianceJoined;
    }

    public void setAllianceJoined(long allianceJoined) {
        this.allianceJoined = allianceJoined;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodLine() {
        return bloodLine;
    }

    public void setBloodLine(String bloodLine) {
        this.bloodLine = bloodLine;
    }

    public long getSkillPoints() {
        return skillPoints;
    }

    public void setSkillPoints(long skillPoints) {
        this.skillPoints = skillPoints;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getPerception() {
        return perception;
    }

    public void setPerception(int perception) {
        this.perception = perception;
    }

    public int getCharisma() {
        return charisma;
    }

    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }

    public int getWillpower() {
        return willpower;
    }

    public void setWillpower(int willpower) {
        this.willpower = willpower;
    }

    public String getCertificates() {
        return certificates;
    }

    public void setCertificates(String certificates) {
        this.certificates = certificates;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getLocationID() {
        return locationID;
    }

    public void setLocationID(long locationID) {
        this.locationID = locationID;
    }

    public float getSecurity() {
        return security;
    }

    public void setSecurity(float security) {
        this.security = security;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipTypeName() {
        return shipTypeName;
    }

    public void setShipTypeName(String shipTypeName) {
        this.shipTypeName = shipTypeName;
    }

    public long getShipTypeID() {
        return shipTypeID;
    }

    public void setShipTypeID(long shipTypeID) {
        this.shipTypeID = shipTypeID;
    }

    public String getAncestry() {
        return ancestry;
    }

    public void setAncestry(String ancestry) {
        this.ancestry = ancestry;
    }

    public long getLogonDateTime() {
        return logonDateTime;
    }

    public void setLogonDateTime(long logonDateTime) {
        this.logonDateTime = logonDateTime;
    }

    public long getLogoffDateTime() {
        return logoffDateTime;
    }

    public void setLogoffDateTime(long logoffDateTime) {
        this.logoffDateTime = logoffDateTime;
    }

    public long getCloneJumpDate() {
        return cloneJumpDate;
    }

    public void setCloneJumpDate(long cloneJumpDate) {
        this.cloneJumpDate = cloneJumpDate;
    }

    public long getLastRespecDate() {
        return lastRespecDate;
    }

    public void setLastRespecDate(long lastRespecDate) {
        this.lastRespecDate = lastRespecDate;
    }

    public long getLastTimedRespec() {
        return lastTimedRespec;
    }

    public void setLastTimedRespec(long lastTimedRespec) {
        this.lastTimedRespec = lastTimedRespec;
    }

    public long getJumpLastUpdate() {
        return jumpLastUpdate;
    }

    public void setJumpLastUpdate(long jumpLastUpdate) {
        this.jumpLastUpdate = jumpLastUpdate;
    }

    public long getJumpActivation() {
        return jumpActivation;
    }

    public void setJumpActivation(long jumpActivation) {
        this.jumpActivation = jumpActivation;
    }

    public long getJumpFatigue() {
        return jumpFatigue;
    }

    public void setJumpFatigue(long jumpFatigue) {
        this.jumpFatigue = jumpFatigue;
    }

    public long getRemoteStationDate() {
        return remoteStationDate;
    }

    public void setRemoteStationDate(long remoteStationDate) {
        this.remoteStationDate = remoteStationDate;
    }

    public long getBirth() {
        return birth;
    }

    public void setBirth(long birth) {
        this.birth = birth;
    }

    public int getGrantableRoles() {
        return grantableRoles;
    }

    public void setGrantableRoles(int grantableRoles) {
        this.grantableRoles = grantableRoles;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public int getFreeRespecs() {
        return freeRespecs;
    }

    public void setFreeRespecs(int freeRespecs) {
        this.freeRespecs = freeRespecs;
    }

    public long getFreeSkillPoints() {
        return freeSkillPoints;
    }

    public void setFreeSkillPoints(long freeSkillPoints) {
        this.freeSkillPoints = freeSkillPoints;
    }

    public List<Long> getImplants() {
        if (StringUtils.isBlank(this.implants)) {
            return Collections.emptyList();
        }
        final String[] split = StringUtils.split(this.implants, ";");
        final List<Long> ids = new ArrayList<>(split.length);
        for (String s: split) {
            ids.add(Long.parseLong(s));
        }
        return ids;
    }

    public void setImplants(List<Long> implants) {
        if (CollectionUtils.isEmpty(implants)) {
            this.implants = null;
            return;
        }
        final StringBuilder b = new StringBuilder();
        for (Long id: implants) {
            b.append(Long.toString(id));
            b.append(";");
        }

        this.implants = StringUtils.removeEnd(b.toString(), ";");
    }

    public List<CharacterSkillEntity> getSkills() {
        if (StringUtils.isBlank(this.skills)) {
            return Collections.emptyList();
        }
        final List<CharacterSkillEntity> returned = new ArrayList<>();
        final String[] split = StringUtils.split(this.skills, ";");
        for (String s: split) {
            final String[] entry = StringUtils.split(s, ":");
            if (entry.length == 3) {
                CharacterSkillEntity e = new CharacterSkillEntity();
                e.setSkillID(Long.parseLong(entry[0]));
                e.setSkillLevel(Integer.parseInt(entry[1]));
                e.setEndPoints(Long.parseLong(entry[2]));
                returned.add(e);
            }
        }
        return returned;
    }

    public void setSkills(List<CharacterSkillEntity> skills) {
        final StringBuilder b = new StringBuilder();
        for (CharacterSkillEntity s: skills) {
            b.append(s.getSkillID());
            b.append(":");
            b.append(s.getSkillLevel());
            b.append(":");
            b.append(s.getEndPoints());
            b.append(";");
        }
        this.skills = StringUtils.removeEnd(b.toString(), ";");
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public int getSortRank() {
        return sortRank;
    }

    public void setSortRank(int sortRank) {
        this.sortRank = sortRank;
    }
}
