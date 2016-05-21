package com.tlabs.android.jeeves.notifications;

import android.content.Context;

import com.tlabs.android.jeeves.model.data.cache.CacheFacade;
import com.tlabs.android.jeeves.model.data.social.MailFacade;
import com.tlabs.eve.api.character.CharacterSheet;
import com.tlabs.eve.api.mail.NotificationMessage;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

final class CharacterNotificationsInspector extends CharacterInspector {
    private final InspectorHelper helper;

    public CharacterNotificationsInspector(Context base, MailFacade mail, CacheFacade cache, final CharacterSheet sheet) {
        super(base, sheet);
        helper = new InspectorHelper(base, mail, cache);
    }

    @Override
    Notification inspect(CharacterSheet sheet) {
        if (!getPreferences().getMessageNotificationOption(sheet.getCharacterID())) {
            return null;
        }

        final List<NotificationMessage> messages = helper.inspectNotifications(sheet.getCharacterID());
        if (messages.size() == 0) {
            return null;
        }

        if (messages.size() == 1) {
            NotificationMessage m = messages.get(0);
            final Notification newMail =
                    Notification.from(this, sheet).
                            setTitle(m.getSenderName()).
                            setSubtitle(sheet.getCharacterName() + " Notifications").
                            setText(StringUtils.isBlank(m.getBody()) ? m.getTitle() : m.getBody()).
                            setWhen(m.getSentDate()).
                            setLargeIcon(NotificationImages.getCharacterIconURL(getApplicationContext(), m.getSenderID()));
            return newMail;
        }

        final Notification notification = Notification.from(this, sheet);
        final StringBuilder b = new StringBuilder();
        for (NotificationMessage m: messages) {
            b.append("<b>");
            b.append(m.getSenderName());
            b.append("</b>: ");
            b.append(m.getTitle());
            b.append("\n");
        }
        return notification.
                setText(b.toString()).
                setCount(messages.size()).
                setWhen(messages.get(0).getSentDate());
    }
}
