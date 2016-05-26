package com.tlabs.android.jeeves.service.events;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.ParcelProperty;

@Parcel(value = Parcel.Serialization.BEAN)
public final class EveAuthCodeImportEndEvent extends EveAuthCodeEvent implements LongRunningEndEvent {

    @ParcelProperty("accountId")
    long accountId;

    @ParcelConstructor
    public EveAuthCodeImportEndEvent(final String authCode, final long accountId) {
        super(authCode);
        this.accountId = accountId;
    }

    public boolean getAuthenticated() {
        return accountId > 0;
    }

    public long getAccountId() {
        return accountId;
    }
}
