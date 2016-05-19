package com.tlabs.android.jeeves.service.events;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(value = Parcel.Serialization.BEAN, parcelsIndex = false)
public final class StarbaseStartEvent extends StarbaseEvent implements LongRunningEndEvent {

    @ParcelConstructor
    public StarbaseStartEvent(long corporationID, long starbaseID) {
        super(corporationID, starbaseID);
    }
}
