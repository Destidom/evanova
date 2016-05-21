package com.tlabs.android.jeeves.model.data.evanova.entities;

import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "corps")
public class CorporationEntity {

    @Id
    @Column
    private long corporationID;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String url;

    @Column
    private String ticker;

    @Column
    private long ceoID;

    @Column
    private String ceoName;

    @Column
    private long stationID;

    @Column
    private String stationName;

    @Column
    private long allianceID;

    @Column
    private String allianceName;

    @Column
    private int membersCount;

    @Column
    private int membersLimit;

    @Column
    private double taxRate;

    @Column
    private double shares;

    @Column
    private int visible = 1;

    @Column
    private String hangarDivisions; //hangars csv "id1=name1;id2=name2

    @Column
    private String walletDivisions; //wallets csv "id1=name1;id2=name2

    @Column
    private double walletBalance;

    @Column
    private String walletBalances;//wallet balances csv "accountid=accountkey=balance;...*/

    @Column
    private int sortRank;

    public long getCorporationID() {
        return corporationID;
    }

    public void setCorporationID(long corporationID) {
        this.corporationID = corporationID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public long getCeoID() {
        return ceoID;
    }

    public void setCeoID(long ceoID) {
        this.ceoID = ceoID;
    }

    public String getCeoName() {
        return ceoName;
    }

    public void setCeoName(String ceoName) {
        this.ceoName = ceoName;
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

    public int getMembersCount() {
        return membersCount;
    }

    public void setMembersCount(int membersCount) {
        this.membersCount = membersCount;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public double getShares() {
        return shares;
    }

    public void setShares(double shares) {
        this.shares = shares;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public Map<Integer, String> getHangarDivisions() {
        return intMap(this.hangarDivisions);
    }

    public void setHangarDivisions(Map<Integer, String> hangarDivisions) {
        this.hangarDivisions = stringMap(hangarDivisions);
    }

    public Map<Integer, String> getWalletDivisions() {
        return intMap(walletDivisions);
    }

    public void setWalletDivisions(Map<Integer, String> walletDivisions) {
        this.walletDivisions = stringMap(walletDivisions);
    }

    public Map<Long, Double> getWalletBalances() {
        return doubleMap(this.walletBalances);
    }

    public void setWalletBalances(Map<Long, Double> walletBalances) {
        this.walletBalances = stringMap(walletBalances);
        this.walletBalance = 0d;
        for (Double b: walletBalances.values()) {
            this.walletBalance = this.walletBalance + b;
        }
    }

    public int getMembersLimit() {
        return membersLimit;
    }

    public void setMembersLimit(int membersLimit) {
        this.membersLimit = membersLimit;
    }

    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public int getSortRank() {
        return sortRank;
    }

    public void setSortRank(int sortRank) {
        this.sortRank = sortRank;
    }

    private static Map<Integer, String> intMap(final String value) {
        if (StringUtils.isBlank(value)) {
            return Collections.emptyMap();
        }
        final String[] split = StringUtils.split(value, ";");
        if (split.length == 0) {
            return Collections.emptyMap();
        }

        final Map<Integer, String> returned = new HashMap<>(split.length);
        for (String s: split) {
            String[] division = StringUtils.split(s, "=");
            returned.put(Integer.parseInt(division[0]), division[1]);
        }
        return returned;
    }

    private static Map<Long, Double> doubleMap(final String value) {
        if (StringUtils.isBlank(value)) {
            return Collections.emptyMap();
        }
        final String[] split = StringUtils.split(value, ";");
        if (split.length == 0) {
            return Collections.emptyMap();
        }

        final Map<Long, Double> returned = new HashMap<>(split.length);
        for (String s: split) {
            String[] division = StringUtils.split(s, "=");
            returned.put(Long.parseLong(division[0]), Double.parseDouble(division[1]));
        }
        return returned;
    }

    private static String stringMap(Map<?, ?> values) {
        final StringBuilder b = new StringBuilder();
        for (Map.Entry<?, ?> e: values.entrySet()) {
            b.append(e.getKey().toString());
            b.append("=");
            b.append(e.getValue().toString());
            b.append(";");
        }
        return StringUtils.removeEnd(b.toString(), ";");
    }
}
