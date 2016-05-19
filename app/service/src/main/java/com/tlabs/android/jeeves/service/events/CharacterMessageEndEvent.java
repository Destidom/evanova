package com.tlabs.android.jeeves.service.events;

abstract class CharacterMessageEndEvent extends CharacterUpdateEvent implements LongRunningEndEvent {
    private final long[] mailIds;

    public CharacterMessageEndEvent(final long charID, final long[] mailIds) {
        super(charID);
        this.mailIds = mailIds;
    }

    public long[] getMailIds() {
        return mailIds;
    }
}