package com.tlabs.android.jeeves.notifications;

import android.content.Context;


import com.tlabs.android.jeeves.model.data.cache.CacheFacade;
import com.tlabs.android.jeeves.model.data.social.MailFacade;
import com.tlabs.eve.api.character.CharacterSheet;
import com.tlabs.eve.api.mail.MailMessage;

import org.apache.commons.lang.StringUtils;

import java.util.List;

final class CharacterMailsInspector extends CharacterInspector {
    private final InspectorHelper helper;

    public CharacterMailsInspector(Context base, MailFacade mail, CacheFacade cache, final CharacterSheet sheet) {
        super(base, sheet);
        helper = new InspectorHelper(base, mail, cache);
    }

    @Override
    Notification inspect(CharacterSheet sheet) {
        if (!getPreferences().getMailNotificationOption(sheet.getCharacterID())) {
            return null;
        }

        final List<MailMessage> messages = helper.inspectMails(sheet.getCharacterID());
        if (messages.size() == 0) {
            return null;
        }

        if (messages.size() == 1) {
            MailMessage m = messages.get(0);
            final Notification newMail =
                    Notification.from(this, sheet).
                    setTitle(m.getSenderName()).
                    setSubtitle(sheet.getCharacterName() + " Mails").
                    setText(StringUtils.isBlank(m.getBody()) ? m.getTitle() : m.getBody()).
                    setWhen(m.getSentDate()).
                    setLargeIcon(NotificationImages.getCharacterIconURL(getApplicationContext(), m.getSenderID()));
            return newMail;
        }

        final Notification notification = Notification.from(this, sheet);
        final StringBuilder b = new StringBuilder();
        for (MailMessage m: messages) {

            b.append("<b>");
            b.append(m.getSenderName());
            b.append("</b>: ");
            b.append(m.getTitle());
        }
        return notification.
                setText(b.toString()).
                setCount(messages.size()).
                setWhen(messages.get(0).getSentDate());
    }
}
