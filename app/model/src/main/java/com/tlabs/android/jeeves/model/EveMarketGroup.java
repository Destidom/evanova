package com.tlabs.android.jeeves.model;

public class EveMarketGroup {

    private long marketGroupID;
    private String marketGroupName;

    private long parentGroupID;
    private long iconID;

    private long childCount;

    public long getMarketGroupID() {
        return marketGroupID;
    }

    public void setMarketGroupID(long marketGroupID) {
        this.marketGroupID = marketGroupID;
    }

    public String getMarketGroupName() {
        return marketGroupName;
    }

    public void setMarketGroupName(String marketGroupName) {
        this.marketGroupName = marketGroupName;
    }

    public long getParentGroupID() {
        return parentGroupID;
    }

    public void setParentGroupID(long parentGroupID) {
        this.parentGroupID = parentGroupID;
    }

    public long getIconID() {
        return iconID;
    }

    public void setIconID(long iconID) {
        this.iconID = iconID;
    }

    public long getChildCount() {
        return childCount;
    }

    public void setChildCount(long childCount) {
        this.childCount = childCount;
    }
}
