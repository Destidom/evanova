package com.tlabs.android.jeeves.service.events;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.ParcelProperty;

import java.util.List;

@Parcel(value = Parcel.Serialization.BEAN, parcelsIndex = false)
public final class EveApiKeyImportEndEvent extends EveApiKeyEvent implements LongRunningEndEvent {

    @ParcelProperty("accountIds")
    List<Long> accountIds;

    @ParcelConstructor
    public EveApiKeyImportEndEvent(final long keyID, final List<Long> accountIds) {
        super(keyID);
        this.accountIds = accountIds;
    }

    public List<Long> getAccounts() {
        return accountIds;
    }
}
