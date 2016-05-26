package com.tlabs.android.jeeves.service.events;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.ParcelProperty;

@Parcel(value = Parcel.Serialization.BEAN)
public final class EveApiKeyImportStartEvent extends EveApiKeyEvent implements LongRunningEndEvent {

    @ParcelProperty("keyValue")
    String keyValue;

    @ParcelProperty("keyName")
    String keyName;

    @ParcelConstructor
    public EveApiKeyImportStartEvent(final long keyID, final String keyValue, final String keyName) {
        super(keyID);
        this.keyValue = keyValue;
        this.keyName = keyName;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public String getKeyName() {
        return keyName;
    }
}
