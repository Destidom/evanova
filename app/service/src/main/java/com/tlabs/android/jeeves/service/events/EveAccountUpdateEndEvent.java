package com.tlabs.android.jeeves.service.events;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.ParcelProperty;

@Parcel(value = Parcel.Serialization.BEAN)
public final class EveAccountUpdateEndEvent extends EveAccountEvent {

    @ParcelProperty("authenticated")
    boolean authenticated = false;

    @ParcelConstructor
    public EveAccountUpdateEndEvent(long accountID, boolean authenticated) {
        super(accountID);
        this.authenticated = authenticated;
    }
}
