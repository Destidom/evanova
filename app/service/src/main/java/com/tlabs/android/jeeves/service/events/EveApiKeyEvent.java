package com.tlabs.android.jeeves.service.events;

public class EveApiKeyEvent extends EveApiEvent {

    long keyID;

    public EveApiKeyEvent(final long keyID) {
        this.keyID = keyID;
    }

    public final long getKeyID() {
        return keyID;
    }

}
