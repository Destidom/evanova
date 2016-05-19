package com.tlabs.android.jeeves.service.events;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(value = Parcel.Serialization.BEAN, parcelsIndex = false)
public final class CharacterCalendarStartEvent extends CharacterUpdateEvent implements LongRunningStartEvent {

    @ParcelConstructor
    public CharacterCalendarStartEvent(final long charID) {
        super(charID);
    }

}