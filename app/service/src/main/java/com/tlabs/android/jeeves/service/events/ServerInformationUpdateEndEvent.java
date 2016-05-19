package com.tlabs.android.jeeves.service.events;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(value = Parcel.Serialization.BEAN, parcelsIndex = false)
public final class ServerInformationUpdateEndEvent extends EveApiEvent implements LongRunningStartEvent {

    @ParcelConstructor
    public ServerInformationUpdateEndEvent() {}
}
