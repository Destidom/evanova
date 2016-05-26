package com.tlabs.android.jeeves.service.events;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(value = Parcel.Serialization.BEAN)
public final class CharacterUpdateEndEvent extends CharacterUpdateEvent implements LongRunningEndEvent, BroadcastedEvent {

    @ParcelConstructor
    public CharacterUpdateEndEvent(final long charID) {
        super(charID);
    }

}
