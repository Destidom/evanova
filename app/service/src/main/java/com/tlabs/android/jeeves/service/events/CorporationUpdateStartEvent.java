package com.tlabs.android.jeeves.service.events;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.ParcelProperty;

@Parcel(value = Parcel.Serialization.BEAN, parcelsIndex = false)
public final class CorporationUpdateStartEvent extends CorporationUpdateEvent implements LongRunningStartEvent {

    @ParcelProperty("force")
    boolean force = false;

    @ParcelConstructor
    public CorporationUpdateStartEvent(final long corporationID, final boolean force) {
        super(corporationID);
        this.force = force;
    }

    public boolean getForce() {
        return force;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).
                append(getCorporationID()).
                toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        CorporationUpdateStartEvent rhs = (CorporationUpdateStartEvent) obj;
        return rhs.getCorporationID() == this.getCorporationID();
    }
}
