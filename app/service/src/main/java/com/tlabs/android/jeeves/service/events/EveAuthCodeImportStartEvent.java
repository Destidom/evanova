package com.tlabs.android.jeeves.service.events;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(value = Parcel.Serialization.BEAN, parcelsIndex = false)
public final class EveAuthCodeImportStartEvent extends EveAuthCodeEvent implements LongRunningStartEvent {

    @ParcelConstructor
    public EveAuthCodeImportStartEvent(final String authCode) {
        super(authCode);
    }
}