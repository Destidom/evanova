package com.tlabs.android.jeeves.service.events;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(value = Parcel.Serialization.BEAN)
public final class ContractItemsEndEvent extends ContractItemsEvent implements LongRunningEndEvent {

    @ParcelConstructor
    public ContractItemsEndEvent(final long ownerID, final long contractID) {
        super(ownerID, contractID);
    }

}
