package com.tlabs.android.jeeves.service.events;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(value = Parcel.Serialization.BEAN)
public final class CorporationSocialStartEvent extends CorporationUpdateEvent implements LongRunningStartEvent {

    @ParcelConstructor
    public CorporationSocialStartEvent(final long corporationID) {
        super(corporationID);
    }

}
