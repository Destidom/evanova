package com.tlabs.android.jeeves.service.events;

class StarbaseEvent extends CorporationUpdateEvent {

    private final long starbaseID;

    public StarbaseEvent(final long corpID, final long starbaseID) {
        super(corpID);
        this.starbaseID = starbaseID;
    }

    public final long getStarbaseID() {
        return starbaseID;
    }

}
