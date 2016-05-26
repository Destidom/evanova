package com.tlabs.android.jeeves.service.events;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.ParcelProperty;

@Parcel(value = Parcel.Serialization.BEAN)
public final class CharacterUpdateStartEvent extends CharacterUpdateEvent implements LongRunningStartEvent {

    @ParcelProperty("force")
    boolean force = false;

    @ParcelConstructor
    public CharacterUpdateStartEvent(final long charID, final boolean force) {
        super(charID);
        this.force = force;
    }

    public boolean getForce() {
        return force;
    }

}