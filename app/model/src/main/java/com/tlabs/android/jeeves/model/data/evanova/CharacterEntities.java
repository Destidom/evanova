package com.tlabs.android.jeeves.model.data.evanova;

import com.tlabs.android.jeeves.model.data.evanova.entities.CharacterEntity;
import com.tlabs.eve.api.character.CharacterInfo;
import com.tlabs.eve.api.character.CharacterSheet;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public final class CharacterEntities {

    private CharacterEntities() {
    }

    public static CharacterSheet toCharacterSheet(final CharacterEntity entity) {
        if (null == entity) {
            return null;
        }

        final CharacterSheet sheet = new CharacterSheet();
        sheet.setAllianceID(entity.getAllianceID());
        sheet.setAllianceName(entity.getAllianceName());
        sheet.setAncestry(entity.getAncestry());
        sheet.setBalance(entity.getBalance());
        sheet.setBirthdate(entity.getBirth());
        sheet.setBloodLine(entity.getBloodLine());
        sheet.setCharacterID(entity.getCharacterID());
        sheet.setCharacterName(entity.getName());
        sheet.setCharisma(entity.getCharisma());
        sheet.setCloneJumpDate(entity.getCloneJumpDate());
        sheet.setCorporationID(entity.getCorporationID());
        sheet.setCorporationName(entity.getCorporationName());
        //sheet.setFactionID(entity.get);
        //sheet.setFactionName();
        sheet.setFreeRespecs(entity.getFreeRespecs());
        sheet.setFreeSkillPoints(entity.getFreeSkillPoints());
        sheet.setGender(entity.getGender());
        //sheet.setHomeStationID(entity.gets);
        sheet.setIntelligence(entity.getIntelligence());
        sheet.setJumpActivation(entity.getJumpActivation());
        sheet.setJumpFatigue(entity.getJumpFatigue());
        sheet.setJumpLastUpdate(entity.getJumpLastUpdate());
        sheet.setLastRespecDate(entity.getLastRespecDate());
        sheet.setLastTimedRespec(entity.getLastTimedRespec());
        sheet.setMemory(entity.getMemory());
        sheet.setPerception(entity.getPerception());
        sheet.setRace(entity.getRace());
        sheet.setRemoteStationDate(entity.getRemoteStationDate());
        sheet.setWillpower(entity.getWillpower());

        for (Long id: entity.getImplants()) {
            final CharacterSheet.Implant implant = new CharacterSheet.Implant();
            implant.setTypeID(id);
            sheet.getImplants().add(implant);
        }

        return sheet;
    }

    public static CharacterInfo toCharacterInfo(final CharacterEntity entity) {
        if (null == entity) {
            return null;
        }

        final CharacterInfo info = new CharacterInfo();
        info.setAllianceID(entity.getAllianceID());
        info.setAlliance(entity.getAllianceName());
        info.setAllianceDate(entity.getAllianceJoined());
        info.setCharacterID(entity.getCharacterID());
        info.setCorporationDate(entity.getCorporationJoined());
        info.setLastKnownLocation(entity.getLocation());
        info.setSecurityStatus(entity.getSecurity());
        info.setShipName(entity.getShipName());
        info.setShipTypeID(entity.getShipTypeID());
        info.setShipTypeName(entity.getShipTypeName());
        info.setSkillPoints(entity.getSkillPoints());

        final String[] split = StringUtils.split(entity.getHistory(), ";");
        if (ArrayUtils.isNotEmpty(split)) {
            for (String s: split) {
                final String[] history = StringUtils.split(s, ":");
                if (ArrayUtils.isNotEmpty(history) && history.length == 3) {
                    final CharacterInfo.History h = new CharacterInfo.History();
                    h.setCorporationID(Long.parseLong(history[0]));
                    h.setCorporationName(history[1]);
                    h.setStartDate(Long.parseLong(history[2]));
                    info.addHistory(h);
                }
            }
        }
        return info;
    }

    //CharacterSheet using CREST is broken - expect nulls
    public static CharacterEntity transform(final CharacterInfo info, final CharacterSheet sheet) {
        if (null == info && null == sheet) {
            return null;
        }

        final CharacterEntity entity = new CharacterEntity();
        if (null != info) {
            fill(entity, info);
        }
        if (null != sheet) {
            fill(entity, sheet);
        }
        return entity;
    }

    private static void fill(final CharacterEntity entity, final CharacterSheet sheet) {
        entity.setCharacterID(sheet.getCharacterID());
        entity.setAllianceID(sheet.getAllianceID());
        //entity.setAllianceJoined(sheet.get);
        entity.setAllianceName(sheet.getAllianceName());
        entity.setAncestry(sheet.getAncestry());
        entity.setBalance(sheet.getBalance());
        entity.setBirth(sheet.getBirthdate());
        entity.setBloodLine(sheet.getBloodLine());
        //entity.setCertificatePlan(sheet.getCertificates());
        //entity.setCertificates(sheet.getCertificates());
        entity.setCharisma(sheet.getCharisma());
        entity.setCloneJumpDate(sheet.getCloneJumpDate());
        entity.setCorporationID(sheet.getCorporationID());
        entity.setCorporationName(sheet.getCorporationName());

        //entity.setCorporationJoined(sheet.getC);
        //entity.setCorporationRoles(sheet.getRoles());
        //entity.setCorporationTitles(sheet.getTitles());
        //entity.setExpires;
        entity.setFreeRespecs(sheet.getFreeRespecs());
        entity.setFreeSkillPoints(sheet.getFreeSkillPoints());
        entity.setGender(sheet.getGender());
        //entity.setGrantableRoles(s);
        //entity.setHistory(sheet.get);
        entity.setIntelligence(sheet.getIntelligence());
        //entity.setAllianceJoined(sheet.g);
        //entity.setCorporationJoined(sheet.ge);
        entity.setJumpActivation(sheet.getJumpActivation());
        entity.setJumpFatigue(sheet.getJumpFatigue());
        entity.setLastRespecDate(sheet.getLastRespecDate());
        entity.setLastTimedRespec(sheet.getLastTimedRespec());
        //entity.setLocation(sheet.getL);
        // entity.setLocationID(sheet.getl);
        //entity.setLogoffDateTime(sheet.g);
        //entity.setLogonDateTime(sheet.getL);
        entity.setJumpLastUpdate(sheet.getJumpLastUpdate());
        entity.setMemory(sheet.getMemory());
        entity.setName(sheet.getCharacterName());
        entity.setPerception(sheet.getPerception());
        entity.setRace(sheet.getRace());
        entity.setRemoteStationDate(sheet.getRemoteStationDate());
        //entity.setSkillPlan(sheet.get);
        entity.setSkillPoints(sheet.getSkillPoints());

        entity.setWillpower(sheet.getWillpower());

        final List<Long> implants = new ArrayList<>(sheet.getImplants().size());
        for (CharacterSheet.Implant i: sheet.getImplants()) {
            implants.add(i.getTypeID());
        }
        entity.setImplants(implants);
    }

    private static void fill(final CharacterEntity entity, final CharacterInfo info) {
        entity.setCharacterID(info.getCharacterID());
        entity.setAllianceID(info.getAllianceID());
        entity.setAllianceJoined(info.getAllianceDate());
        entity.setAllianceName(info.getAlliance());

        entity.setCorporationJoined(info.getCorporationDate());

        entity.setSkillPoints(info.getSkillPoints());
        entity.setShipTypeID(info.getShipTypeID());
        entity.setShipTypeName(info.getShipTypeName());
        entity.setShipName(info.getShipName());
        entity.setLocation(info.getLastKnownLocation());
        entity.setSecurity(info.getSecurityStatus());

        final StringBuilder history = new StringBuilder();
        for (CharacterInfo.History h: info.getHistory()) {
            history.append(h.getCorporationID());
            history.append(":");
            history.append(h.getCorporationName());
            history.append(":");
            history.append(h.getStartDate());
            history.append(";");
        }
        entity.setHistory(StringUtils.removeEnd(history.toString(), ";"));
    }

}