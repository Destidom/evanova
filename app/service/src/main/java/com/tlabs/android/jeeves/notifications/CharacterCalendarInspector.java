package com.tlabs.android.jeeves.notifications;

import android.content.Context;

import com.tlabs.android.jeeves.model.data.cache.CacheFacade;
import com.tlabs.android.jeeves.model.data.social.MailFacade;
import com.tlabs.eve.api.character.CharacterCalendar;
import com.tlabs.eve.api.character.CharacterSheet;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

final class CharacterCalendarInspector extends CharacterInspector {

    private final InspectorHelper helper;

    public CharacterCalendarInspector(Context base, MailFacade mail, CacheFacade cache, final CharacterSheet sheet) {
        super(base, sheet);
        this.helper = new InspectorHelper(base, mail, cache);
    }

    @Override
    Notification inspect(CharacterSheet sheet) {
        if (!getPreferences().getCalendarNotificationOption(sheet.getCharacterID())) {
            return null;
        }

        final List<CharacterCalendar.Entry> entries = helper.inspectCalendar(sheet.getCharacterID());
        if (CollectionUtils.isEmpty(entries)) {
            return null;
        }
        if (entries.size() == 1) {
            CharacterCalendar.Entry entry = entries.get(0);
            String text = entry.getEventText();
            if (StringUtils.isBlank(text)) {
                text = entry.getEventTitle();
            }
            if (StringUtils.isBlank(text)) {
                text = "Calendar event on " + FormatHelper.DateTime.SHORT(entry.getEventDate());
            }
            final Notification notification =
                    Notification.from(this, sheet).
                    setTitle(entry.getEventTitle()).
                    setSubtitle(sheet.getCharacterName() + " Calendar").
                    setText(text).
                    setWhen(entry.getEventDate());

            return notification;
        }

        final Notification notification = Notification.from(this, sheet);
        final StringBuilder b = new StringBuilder();
        for (CharacterCalendar.Entry e: entries) {
            b.append("<b>");
            b.append(e.getOwnerName());
            b.append("</b>: ");
            String text = e.getEventText();
            if (StringUtils.isBlank(text)) {
                text = e.getEventTitle();
            }
            if (StringUtils.isBlank(text)) {
                text = "Calendar event on " + FormatHelper.DateTime.SHORT(e.getEventDate());
            }
            b.append(text);
            b.append(" ");
            b.append(FormatHelper.DateTime.SHORT(e.getEventDate()));
            b.append("\n");
        }
        notification.setText(b.toString()).setCount(entries.size()).setSubtitle(sheet.getCharacterName() + " Calendar");
        return notification;
    }
}
