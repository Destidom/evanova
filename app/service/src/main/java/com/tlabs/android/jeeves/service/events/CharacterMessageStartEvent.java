package com.tlabs.android.jeeves.service.events;

abstract class CharacterMessageStartEvent extends CharacterUpdateEvent implements LongRunningStartEvent {

    private final long[] mailIds;

    public CharacterMessageStartEvent(final long charID, final long[] mailIds) {
        super(charID);
        this.mailIds = mailIds;
    }

    public long[] getMailIds() {
        return mailIds;
    }
}