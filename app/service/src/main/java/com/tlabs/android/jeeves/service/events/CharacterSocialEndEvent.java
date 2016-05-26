package com.tlabs.android.jeeves.service.events;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(value = Parcel.Serialization.BEAN)
public final class CharacterSocialEndEvent extends CharacterUpdateEvent implements LongRunningEndEvent {

    @ParcelConstructor
    public CharacterSocialEndEvent(final long charID) {
        super(charID);
    }
}