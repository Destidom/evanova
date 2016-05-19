package com.tlabs.android.jeeves.service.actions;

import android.content.Context;

import com.tlabs.eve.api.character.CharacterCalendarAttendeesRequest;

public final class CharacterCalendarEventAction extends EveAction {

    public CharacterCalendarEventAction(final Context context, final long charID, final long eventID) {
        super(context, new CharacterCalendarAttendeesRequest(Long.toString(charID), eventID));
    }
}
