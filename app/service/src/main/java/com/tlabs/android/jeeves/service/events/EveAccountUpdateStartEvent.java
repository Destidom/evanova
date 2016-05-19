package com.tlabs.android.jeeves.service.events;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.ParcelProperty;

@Parcel(value = Parcel.Serialization.BEAN, parcelsIndex = false)
public final class EveAccountUpdateStartEvent extends EveAccountEvent {

    @ParcelProperty("force")
    boolean force = false;

    @ParcelConstructor
    public EveAccountUpdateStartEvent(final long accountID, final boolean force) {
        super(accountID);
        this.force = force;
    }

    public boolean getForce() {
        return force;
    }
}
