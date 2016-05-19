package com.tlabs.android.jeeves.model;

import com.tlabs.eve.api.EveAPI.CorporationAccess;
import com.tlabs.eve.api.corporation.CorporationSheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public final class EveCorporation {

	private final List<Long> accessMasks;
	private final CorporationSheet corpInfo;
	
	private long createdDate;
	private String location;

	public EveCorporation(final CorporationSheet corpInfo) {
		super();

		this.corpInfo = corpInfo;
		this.accessMasks = new ArrayList<>();
	}

	public CorporationSheet getCorporationSheet() {
		return corpInfo;
	}

	public String getName() {return corpInfo.getCorporationName();}
	public long getID() {
        return corpInfo.getCorporationID();
    }

    public String getDescription() {
        return corpInfo.getDescription();
    }

    public String getUrl() {
        return corpInfo.getUrl();
    }

    public String getTicker() {
        return corpInfo.getTicker();
    }

    public long getCeoID() {
        return corpInfo.getCeoID();
    }

    public String getCeoName() {
        return corpInfo.getCeoName();
    }

    public long getStationID() {
        return corpInfo.getStationID();
    }

    public String getStationName() {
        return corpInfo.getStationName();
    }

    public long getAllianceID() {
        return corpInfo.getAllianceID();
    }

    public String getAllianceName() {
        return corpInfo.getAllianceName();
    }

    public int getMemberCount() {
        return corpInfo.getMemberCount();
    }

    public int getMemberLimit() {
        return corpInfo.getMemberLimit();
    }

    public float getTaxRate() {
        return corpInfo.getTaxRate();
    }

    public float getShares() {
        return corpInfo.getShares();
    }

    public final Map<Integer, String> getWalletDivisions() {
        return corpInfo.getWalletDivisions();
    }

    public final Map<Integer, String> getHangarDivisions() {
        return corpInfo.getHangarDivisions();
    }

	public final double getBalance() {
        return corpInfo.getBalance();
    }

    public long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public final boolean hasAnyAccess(CorporationAccess... access) {
		if (null == access || access.length == 0) {
			return true;
		}
		for (CorporationAccess a: access) {
			for (long m: this.accessMasks) {
				if ((a.getAccessMask() & m) == a.getAccessMask()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public final boolean hasAllAccess(CorporationAccess... access) {
		if (null == access || access.length == 0) {
			return true;
		}
		for (CorporationAccess a: access) {
			boolean hasAccess = false;
			for (long m: this.accessMasks) {
				if ((a.getAccessMask() & m) == a.getAccessMask()) {
					hasAccess = true;
					break;
				}
			}
			if (!hasAccess) {
				return false;
			}
		}
		return true;
	}
	public final void setAccessMasks(final List<Long> masks) {
		this.accessMasks.clear();
		this.accessMasks.addAll(masks);
	}
}
