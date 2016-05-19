package com.tlabs.android.jeeves.service.events;

public class EveAccountEvent extends EveApiEvent {

    private final long accountID;

    public EveAccountEvent(final long accountID) {
        this.accountID = accountID;
    }

    public final long getAccountID() {
        return accountID;
    }
}
