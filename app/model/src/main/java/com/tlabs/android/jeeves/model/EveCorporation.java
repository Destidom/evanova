package com.tlabs.android.jeeves.model;

import com.tlabs.eve.api.EveAPI;
import com.tlabs.eve.api.corporation.CorporationSheet;

import java.util.Map;


public final class EveCorporation {

	private final CorporationSheet corpInfo;
	private final EveAccount account;
	
	private long createdDate;
	private String location;

	public EveCorporation(final CorporationSheet corpInfo, final EveAccount account) {
		super();

		this.corpInfo = corpInfo;
		this.account = account;
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

	public final boolean hasCrest() {
		return this.account.hasCorporationScope();
	}

	public final boolean hasApiKey() {
		return this.account.hasApiKey();
	}

	public final boolean hasAnyAccess(EveAPI.CorporationAccess... access) {
		return EveAccessHelper.hasAnyAccess(this.account.getAccessMask(), access);
	}

	public final boolean hasAllAccess(EveAPI.CorporationAccess... access) {
		return EveAccessHelper.hasAllAccess(this.account.getAccessMask(), access);
	}

}
