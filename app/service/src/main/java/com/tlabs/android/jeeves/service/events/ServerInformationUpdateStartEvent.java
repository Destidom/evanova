package com.tlabs.android.jeeves.service.events;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(value = Parcel.Serialization.BEAN)
public final class ServerInformationUpdateStartEvent extends EveApiEvent implements LongRunningStartEvent {

    @ParcelConstructor
    public ServerInformationUpdateStartEvent() {}

    @Override
    public int hashCode() {
        return new HashCodeBuilder(91, 27).
                toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        return obj.getClass() == getClass();
    }
    
}
