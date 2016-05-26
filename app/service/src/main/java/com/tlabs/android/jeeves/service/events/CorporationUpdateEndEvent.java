package com.tlabs.android.jeeves.service.events;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(value = Parcel.Serialization.BEAN)
public final class CorporationUpdateEndEvent extends CorporationUpdateEvent implements LongRunningEndEvent, BroadcastedEvent {

    @ParcelConstructor
    public CorporationUpdateEndEvent(final long corporationID) {
        super(corporationID);
    }

}
