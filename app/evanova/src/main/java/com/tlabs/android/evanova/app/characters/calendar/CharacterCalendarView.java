package com.tlabs.android.evanova.app.characters.calendar;

import com.tlabs.android.evanova.mvp.ActivityView;
import com.tlabs.eve.api.character.CharacterCalendar;

interface CharacterCalendarView extends ActivityView {

    void setCalendar(final CharacterCalendar calendar);
}
