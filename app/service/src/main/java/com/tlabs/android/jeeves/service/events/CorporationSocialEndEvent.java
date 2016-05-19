package com.tlabs.android.jeeves.service.events;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(value = Parcel.Serialization.BEAN, parcelsIndex = false)
public final class CorporationSocialEndEvent extends CorporationUpdateEvent implements LongRunningEndEvent {

    @ParcelConstructor
    public CorporationSocialEndEvent(final long corporationID) {
        super(corporationID);
    }

}
