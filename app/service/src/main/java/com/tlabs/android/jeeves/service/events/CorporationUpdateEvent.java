package com.tlabs.android.jeeves.service.events;

class CorporationUpdateEvent extends EveApiEvent {

    long corporationID;

    protected CorporationUpdateEvent(final long corporationID) {
        this.corporationID = corporationID;
    }

    public final long getCorporationID() {
        return corporationID;
    }
}
