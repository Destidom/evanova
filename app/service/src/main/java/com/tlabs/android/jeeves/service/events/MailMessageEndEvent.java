package com.tlabs.android.jeeves.service.events;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(value = Parcel.Serialization.BEAN, parcelsIndex = false)
public final class MailMessageEndEvent extends CharacterMessageEndEvent {

    @ParcelConstructor
    public MailMessageEndEvent(long charID, long[] mailIds) {
        super(charID, mailIds);

    }

}
