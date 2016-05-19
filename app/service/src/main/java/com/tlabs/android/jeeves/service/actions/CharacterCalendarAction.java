package com.tlabs.android.jeeves.service.actions;

import android.content.Context;

import com.tlabs.eve.EveRequest;
import com.tlabs.eve.EveResponse;
import com.tlabs.eve.api.character.CharacterCalendar;
import com.tlabs.eve.api.character.CharacterCalendarRequest;
import com.tlabs.eve.api.character.CharacterCalendarResponse;

import java.util.ArrayList;
import java.util.List;

public final class CharacterCalendarAction extends EveAction {
    private final List<Long> events;

    public CharacterCalendarAction(final Context context, final long charID) {
        super(context, new CharacterCalendarRequest(Long.toString(charID)));
        this.events = new ArrayList<>();
    }
    @Override
    public EveAction onRequestCompleted(EveRequest<? extends EveResponse> request, EveResponse response) {
        this.events.clear();
        final CharacterCalendarResponse r = (CharacterCalendarResponse)response;
        for (CharacterCalendar.Entry e: r.getCalendar().getEntries()) {
            this.events.add(e.getEventID());
        }
        return null;
    }

    public List<Long> getEvents() {
        return events;
    }
}
