package com.tlabs.android.jeeves.service.events;

public class EveAuthCodeEvent extends EveApiEvent {

    String authCode;

    public EveAuthCodeEvent(final String authCode) {
        this.authCode = authCode;
    }

    public String getAuthCode() {
        return authCode;
    }
}
