package com.tlabs.android.jeeves.service.events;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(value = Parcel.Serialization.BEAN)
public final class CharacterMailboxEndEvent extends CharacterUpdateEvent implements LongRunningEndEvent {

    @ParcelConstructor
    public CharacterMailboxEndEvent(final long charID) {
        super(charID);
    }
}