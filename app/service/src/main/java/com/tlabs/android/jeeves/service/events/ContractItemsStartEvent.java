package com.tlabs.android.jeeves.service.events;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(value = Parcel.Serialization.BEAN, parcelsIndex = false)
public final class ContractItemsStartEvent extends ContractItemsEvent implements LongRunningStartEvent {

    @ParcelConstructor
    public ContractItemsStartEvent(final long ownerID, final long contractID) {
        super(ownerID, contractID);
    }

}
