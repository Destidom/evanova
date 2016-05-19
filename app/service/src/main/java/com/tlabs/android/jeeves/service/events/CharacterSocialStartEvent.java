package com.tlabs.android.jeeves.service.events;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(value = Parcel.Serialization.BEAN, parcelsIndex = false)
public final class CharacterSocialStartEvent extends CharacterUpdateEvent implements LongRunningStartEvent {

    @ParcelConstructor
    public CharacterSocialStartEvent(final long charID) {
        super(charID);
    }

}