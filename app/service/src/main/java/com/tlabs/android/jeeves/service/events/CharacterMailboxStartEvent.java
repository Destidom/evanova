package com.tlabs.android.jeeves.service.events;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(value = Parcel.Serialization.BEAN, parcelsIndex = false)
public final class CharacterMailboxStartEvent extends CharacterUpdateEvent implements LongRunningStartEvent {

    @ParcelConstructor
    public CharacterMailboxStartEvent(final long charID) {
        super(charID);
    }
}