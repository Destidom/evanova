package com.tlabs.android.jeeves.service.events;

class CharacterUpdateEvent extends EveApiEvent {

    private final long charID;

    protected CharacterUpdateEvent(final long charID) {
        this.charID = charID;
    }

    public final long getCharID() {
        return charID;
    }
}
