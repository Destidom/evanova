package com.tlabs.android.jeeves.service.events;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class EveApiEvent {

    private final long createdTime;

    protected EveApiEvent() {
        super();
        this.createdTime = System.currentTimeMillis();
    }

    public final long getCreatedTime() {
        return createdTime;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
