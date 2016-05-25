package com.tlabs.android.jeeves.model;

import com.tlabs.eve.api.EveAPI;
import com.tlabs.eve.api.EveAPI.CharacterAccess;
import com.tlabs.eve.api.character.CharacterInfo;
import com.tlabs.eve.api.character.CharacterSheet;
import com.tlabs.eve.api.character.CharacterSheet.Implant;
import com.tlabs.eve.api.character.CharacterSheet.JumpClone;
import com.tlabs.eve.api.character.SkillInTraining;
import com.tlabs.eve.api.corporation.CorporationRole;
import com.tlabs.eve.api.corporation.CorporationTitle;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EveCharacter {

    public static final class Attribute {

        private final String name;
        private final int baseValue;
        private int enhancerValue = 0;

        private Attribute(String name, int baseValue) {
            this.name = name;
            this.baseValue = baseValue;
        }

        public int getBaseValue() {
            return baseValue;
        }

        public int getEnhancerValue() {
            return enhancerValue;
        }

        public void setEnhancerValue(int enhancerValue) {
            this.enhancerValue = enhancerValue;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return this.baseValue + this.enhancerValue;
        }
    }

    public static class Location {


        private long locationID;
        private String locationName;

        private long constellationID;

        private long stationID;
        private String stationName;

        private double securityStatus;

        private long sovereigntyID;
        private String sovereignty;

        public long getLocationID() {
            return locationID;
        }

        public void setLocationID(long locationID) {
            this.locationID = locationID;
        }

        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        public long getConstellationID() {
            return constellationID;
        }

        public void setConstellationID(long constellationID) {
            this.constellationID = constellationID;
        }

        public long getStationID() {
            return stationID;
        }

        public void setStationID(long stationID) {
            this.stationID = stationID;
        }

        public String getStationName() {
            return stationName;
        }

        public void setStationName(String stationName) {
            this.stationName = stationName;
        }

        public double getSecurityStatus() {
            return securityStatus;
        }

        public void setSecurityStatus(double securityStatus) {
            this.securityStatus = securityStatus;
        }

        public long getSovereigntyID() {
            return sovereigntyID;
        }

        public void setSovereigntyID(long sovereigntyID) {
            this.sovereigntyID = sovereigntyID;
        }

        public String getSovereignty() {
            return sovereignty;
        }

        public void setSovereignty(String sovereignty) {
            this.sovereignty = sovereignty;
        }
    }

	private long corporationJoinedOn = -1;
	private String corporationTitles;
	private String corporationRoles;

    private String bloodline;
    private final CharacterSheet sheet;
    private final CharacterInfo info;

    private final EveAccount account;
    private final EveTraining training;

    private final Map<String, Attribute> attributes;
    private Location location;

	public EveCharacter(
            final CharacterSheet sheet,
            final CharacterInfo info,
            final EveAccount account) {
		super();

        this.sheet = sheet;
        this.info = info;
        this.account = account;

        this.attributes = new HashMap<>();
        this.attributes.put(EveAPI.ATTR_CHARISMA, new Attribute(EveAPI.ATTR_CHARISMA, sheet.getCharisma()));
        this.attributes.put(EveAPI.ATTR_MEMORY, new Attribute(EveAPI.ATTR_MEMORY, sheet.getMemory()));
        this.attributes.put(EveAPI.ATTR_INTELLIGENCE, new Attribute(EveAPI.ATTR_INTELLIGENCE, sheet.getIntelligence()));
        this.attributes.put(EveAPI.ATTR_WILLPOWER, new Attribute(EveAPI.ATTR_WILLPOWER, sheet.getWillpower()));
        this.attributes.put(EveAPI.ATTR_PERCEPTION, new Attribute(EveAPI.ATTR_PERCEPTION, sheet.getPerception()));
        this.training = new EveTraining(sheet, info, this.attributes);
        this.location = new Location();
	}

    public void setTraining(final List<SkillInTraining> training) {
        this.training.setTraining(training);
    }

    public EveTraining getTraining() {
        return training;
    }

    public Map<String, Attribute> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public long getCorporationJoinedOn() {
        return corporationJoinedOn;
    }

    public void setCorporationJoinedOn(long corporationJoinedOn) {
        this.corporationJoinedOn = corporationJoinedOn;
    }

    public final String getName() {
        return this.sheet.getCharacterName();
    }

    public final long getID() {
        return this.sheet.getCharacterID();
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

    public String getShipName() {
        return this.info.getShipName();
    }

    public String getShipTypeName() {
        return this.info.getShipTypeName();
    }

    public long getShipTypeID() {
        return this.info.getShipTypeID();
    }

    public String getBloodline() {
        return bloodline;
    }

    public void setBloodline(String bloodline) {
        this.bloodline = bloodline;
    }

    public long getRemoteStationDate() {
        return sheet.getRemoteStationDate();
    }

    public int getMemory() {
        return getMemory(false);
    }

    public int getMemory(final boolean enhanced) {
        return getAttributeValue(EveAPI.ATTR_MEMORY, enhanced);
    }

    public long getJumpActivation() {
        return sheet.getJumpActivation();
    }

    public int getIntelligence() {
        return getIntelligence(false);
    }

    public int getIntelligence(final boolean enhanced) {
        return getAttributeValue(EveAPI.ATTR_INTELLIGENCE, enhanced);
    }

    public long getCloneJumpDate() {
        return sheet.getCloneJumpDate();
    }

    public String getFactionName() {
        return sheet.getFactionName();
    }

    public long getHomeStationID() {
        return sheet.getHomeStationID();
    }

    public String getGender() {
        return sheet.getGender();
    }

    public long getJumpFatigue() {
        return sheet.getJumpFatigue();
    }

    public String getRace() {
        return sheet.getRace();
    }

    public List<JumpClone> getJumpClones() {
        return sheet.getJumpClones();
    }

    public String getAllianceName() {
        return sheet.getAllianceName();
    }

    public List<Long> getCertificates() {
        return sheet.getCertificates();
    }

    public String getAncestry() {
        return sheet.getAncestry();
    }

    public long getLastTimedRespec() {
        return sheet.getLastTimedRespec();
    }

    public long getFactionID() {
        return sheet.getFactionID();
    }

    public int getFreeRespecs() {
        return sheet.getFreeRespecs();
    }

    public long getCorporationID() {
        return sheet.getCorporationID();
    }

    public List<CorporationRole> getRoles() {
        return sheet.getRoles();
    }

    public int getCharisma() {
        return getCharisma(false);
    }

    public int getCharisma(final boolean enhanced) {
        return getAttributeValue(EveAPI.ATTR_CHARISMA, enhanced);
    }

    public double getBalance() {
        return sheet.getBalance();
    }

    public long getBirthdate() {
        return sheet.getBirthdate();
    }

    public String getBloodLine() {
        return sheet.getBloodLine();
    }

    public long getAllianceID() {
        return sheet.getAllianceID();
    }

    public int getWillpower() {
        return getWillpower(false);
    }

    public int getWillpower(final boolean enhanced) {
        return getAttributeValue(EveAPI.ATTR_WILLPOWER, enhanced);
    }

    public String getCorporationName() {
        return sheet.getCorporationName();
    }

    public long getLastRespecDate() {
        return sheet.getLastRespecDate();
    }

    public long getJumpLastUpdate() {
        return sheet.getJumpLastUpdate();
    }

    public int getPerception() {
        return getPerception(false);
    }

    public int getPerception(final boolean enhanced) {
        return getAttributeValue(EveAPI.ATTR_PERCEPTION, enhanced);
    }

    public List<Implant> getImplants() {
        return sheet.getImplants();
    }

    public List<CorporationTitle> getTitles() {
        return sheet.getTitles();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public final float getSecurityStatus() {
        return this.info.getSecurityStatus();
    }

    public final List<CharacterInfo.History> getHistory() {
        return this.info.getHistory();
    }

    public final void addEnhancerValue(final String attributeName, final int value) {
        Attribute attr = this.attributes.get(attributeName);
        attr.setEnhancerValue(attr.getEnhancerValue() + value);
    }

    public final boolean hasCrest() {
        return this.account.hasCharacterScope();
    }

    public final boolean hasApiKey() {
        return this.account.hasApiKey();
    }

    public final boolean hasAnyAccess(CharacterAccess... access) {
        return EveAccessHelper.hasAnyAccess(this.account.getAccessMask(), access);
    }

    public final boolean hasAllAccess(CharacterAccess... access) {
        return EveAccessHelper.hasAllAccess(this.account.getAccessMask(), access);
    }

    public final void addJumpClone(final JumpClone clone) {
        this.sheet.addJumpClone(clone);
    }

    private int getAttributeValue(final String attrName, final boolean enhanced) {
        final Attribute attr = this.attributes.get(attrName);
        int value = attr.getBaseValue();
        if (enhanced) {
            value = value + attr.getEnhancerValue();
        }
        return value;
    }
}
