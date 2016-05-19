package com.tlabs.android.jeeves.service.events;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(value = Parcel.Serialization.BEAN, parcelsIndex = false)
public final class StarbaseEndEvent extends StarbaseEvent implements LongRunningEndEvent {

    @ParcelConstructor
    public StarbaseEndEvent(long corporationID, long starbaseID) {
        super(corporationID, starbaseID);
    }

}
