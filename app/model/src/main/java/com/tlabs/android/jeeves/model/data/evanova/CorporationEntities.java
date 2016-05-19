package com.tlabs.android.jeeves.model.data.evanova;

import com.tlabs.android.jeeves.model.data.evanova.entities.CorporationEntity;
import com.tlabs.eve.api.corporation.CorporationSheet;

public final class CorporationEntities {

    private CorporationEntities() {
    }

    public static CorporationSheet transform(final CorporationEntity entity) {
        if (null == entity) {
            return null;
        }

        final CorporationSheet sheet = new CorporationSheet();
        sheet.setAllianceID(entity.getAllianceID());
        sheet.setCorporationID(entity.getCorporationID());
        sheet.setCorporationName(entity.getName());
        sheet.setDescription(entity.getDescription());
        sheet.setAllianceName(entity.getAllianceName());
        sheet.setCeoID(entity.getCeoID());
        sheet.setCeoName(entity.getCeoName());
        //sheet.setFactionID(entity.getF);
        //sheet.setFactionName();
        sheet.setMemberCount(entity.getMembersCount());
        sheet.setMemberLimit(entity.getMembersLimit());
        sheet.setShares((float) entity.getShares());
        sheet.setStationID(entity.getStationID());
        sheet.setStationName(entity.getStationName());
        sheet.setTaxRate((float) entity.getTaxRate());
        sheet.setTicker(entity.getTicker());
        sheet.setUrl(entity.getUrl());
        sheet.setBalance(entity.getWalletBalance());
        return sheet;
    }

    public static CorporationEntity transform(final CorporationSheet sheet) {
        final CorporationEntity entity = new CorporationEntity();
        entity.setCeoName(sheet.getCeoName());
        entity.setCeoID(sheet.getCeoID());
        entity.setName(sheet.getCorporationName());
        entity.setAllianceID(sheet.getAllianceID());
        entity.setAllianceName(sheet.getAllianceName());
        entity.setCorporationID(sheet.getCorporationID());
        entity.setDescription(sheet.getDescription());
        entity.setHangarDivisions(sheet.getHangarDivisions());
        entity.setMembersCount(sheet.getMemberCount());
        entity.setMembersLimit(sheet.getMemberLimit());
        entity.setShares(sheet.getShares());
        entity.setStationID(sheet.getStationID());
        entity.setStationName(sheet.getStationName());
        entity.setTaxRate(sheet.getTaxRate());
        entity.setTicker(sheet.getTicker());
        entity.setUrl(sheet.getUrl());
        entity.setWalletBalance(sheet.getBalance());
        entity.setWalletDivisions(sheet.getWalletDivisions());
        return entity;
    }

}