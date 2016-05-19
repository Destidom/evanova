package com.tlabs.android.jeeves.service.events;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(value = Parcel.Serialization.BEAN, parcelsIndex = false)
public final class CharacterCalendarEndEvent extends CharacterUpdateEvent implements LongRunningEndEvent {

    @ParcelConstructor
    public CharacterCalendarEndEvent(final long charID) {
        super(charID);
    }
}