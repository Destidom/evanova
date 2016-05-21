package com.tlabs.android.jeeves.notifications;

import android.content.Context;

import com.tlabs.android.jeeves.model.data.cache.CacheFacade;
import com.tlabs.android.jeeves.model.data.social.MailFacade;
import com.tlabs.android.util.Log;
import com.tlabs.android.util.PreferenceSupport;
import com.tlabs.eve.api.character.CharacterCalendar;
import com.tlabs.eve.api.character.CharacterCalendarRequest;
import com.tlabs.eve.api.character.CharacterCalendarResponse;
import com.tlabs.eve.api.mail.MailMessage;
import com.tlabs.eve.api.mail.NotificationMessage;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class InspectorHelper extends PreferenceSupport {
    private static final String LOG = "InspectorHelper";

    private static final String KEY_NOTIFICATIONS_TIME = "jeeves.preferences.inspect.notifications.time.";
    private static final String KEY_NOTIFICATIONS_LAST = "jeeves.preferences.inspect.notifications.last.";

    private static final String KEY_MESSAGES_TIME = "jeeves.preferences.inspect.mail.time.";
    private static final String KEY_MESSAGES_LAST = "jeeves.preferences.inspect.mail.last.";

    private static final String KEY_CALENDAR = "jeeves.preferences.inspect.calendar.";
    private static final String KEY_CALENDARS = "jeeves.preferences.inspect.calendars";//string array

    private final Context context;
    private final MailFacade mail;
    private final CacheFacade cache;

    private final EveNotificationPreferences preferences;

    protected InspectorHelper(final Context context, final MailFacade mailFacade, final CacheFacade cache) {
        super(context);
        this.context = context.getApplicationContext();
        this.mail = mailFacade;
        this.cache = cache;
        this.preferences = new EveNotificationPreferences(context);
    }

    public List<MailMessage> inspectMails(final long ownerID) {
        long lastChecked = getLastMessageTime(ownerID);
        long lastMessage = getLastMessage(ownerID);

        if (lastChecked == -1) {
            lastChecked = System.currentTimeMillis() - 2L * 3600L * 1000L;
        }
        setLastMessageTime(ownerID, System.currentTimeMillis());

        final List<MailMessage> messages = mail.getMailsSince(ownerID, lastChecked);
        if (Log.D) {
            Log.d(LOG, "inspectMails owner=" + ownerID + "; lastChecked=" + lastChecked + "; lastMessage=" + lastMessage + "; newCount=" + messages.size());
        }

        if (messages.isEmpty()) {
            return Collections.emptyList();
        }

        final MailMessage newest = messages.get(0);
        if (Log.D) {
            Log.d(LOG, "inspectMails owner=" + ownerID + "; lastChecked=" + lastChecked + "; lastMessage=" + lastMessage + "; newest=" + newest.getMessageID());
        }
        if (newest.getMessageID() == lastMessage) {
            return Collections.emptyList();
        }

        setLastMessage(ownerID, newest.getMessageID());
        final List<MailMessage> returned = new ArrayList<>();

        for (MailMessage m: messages) {
            if (m.getMessageID() == lastMessage) {
                break;
            }
            returned.add(m);
            if (Log.D) {
                Log.d(LOG, "inspectMails: add " + ToStringBuilder.reflectionToString(m));
            }
        }
        return returned;
    }

    public List<NotificationMessage> inspectNotifications(final long ownerID) {
        long lastChecked = getLastNotificationTime(ownerID);
        long lastMessage = getLastNotification(ownerID);

        if (lastChecked == -1) {
            lastChecked = System.currentTimeMillis() - 2L * 3600L * 1000L;
        }
        setLastNotificationTime(ownerID, System.currentTimeMillis());

        final List<NotificationMessage> messages = mail.getNotificationsSince(ownerID, lastChecked);
        if (Log.D) {
            Log.d(LOG, "inspectNotifications owner=" + ownerID + "; lastChecked=" + lastChecked + "; lastMessage=" + lastMessage + "; newCount=" + messages.size());
        }

        if (messages.isEmpty()) {
            return Collections.emptyList();
        }

        final NotificationMessage newest = messages.get(0);
        if (Log.D) {
            Log.d(LOG, "inspectNotifications owner=" + ownerID + "; lastChecked=" + lastChecked + "; lastMessage=" + lastMessage + "; newest=" + newest.getNotificationID());
        }
        if (newest.getNotificationID() == lastMessage) {
            return Collections.emptyList();
        }

        setLastNotification(ownerID, newest.getNotificationID());
        final List<NotificationMessage> returned = new ArrayList<>();

        for (NotificationMessage m: messages) {
            if (m.getNotificationID() == lastMessage) {
                break;
            }
            returned.add(m);
            if (Log.D) {
                Log.d(LOG, "inspectNotifications: add " + ToStringBuilder.reflectionToString(m));
            }
        }
        return returned;
    }

    private static final long H24 = 24L * 60L * 60000L;

    public List<CharacterCalendar.Entry> inspectCalendar(final long characterID) {
        final CharacterCalendarRequest request = new CharacterCalendarRequest(Long.toString(characterID));
        final CharacterCalendarResponse response = cache.getCached(request);
        if (null == response) {
            return Collections.emptyList();
        }

        final long now = System.currentTimeMillis();

        final CharacterCalendar calendar = response.getCalendar();
        final List<CharacterCalendar.Entry> entries = new ArrayList<>(calendar.getEntries().size());

        for (CharacterCalendar.Entry e: calendar.getEntries()) {
            if (e.getEventDate() < now) {
                preferences.removeCalendarEvent(e.getEventID());
            }
            else if (e.getEventDate() >= now && e.getEventDate() < now + H24) {
                if (-1 == preferences.getCalendarEvent(e.getEventID())) {
                    preferences.addCalendarEvent(e.getEventID());
                    entries.add(e);
                }
            }
        }
        return entries;
    }


    public long getLastMessageTime(final long ownerId) {
        return getLong(KEY_MESSAGES_TIME + "." + ownerId, -1L);
    }

    public long getLastMessage(final long ownerId) {
        return getLong(KEY_MESSAGES_LAST + "." + ownerId, -1);
    }

    public long getLastNotificationTime(final long ownerId) {
        return getLong(KEY_NOTIFICATIONS_TIME + "." + ownerId, -1L);
    }

    public long getLastNotification(final long ownerId) {
        return getLong(KEY_NOTIFICATIONS_LAST + "." + ownerId, -1);
    }

    public void setLastMessageTime(final long ownerId, final long time) {
        setLong(KEY_MESSAGES_TIME + "." + ownerId, time);
    }

    public void setLastNotificationTime(final long ownerId, final long time) {
        setLong(KEY_NOTIFICATIONS_TIME + "." + ownerId, time);
    }

    public void setLastMessage(final long ownerId, final long messageId) {
        setLong(KEY_MESSAGES_LAST + "." + ownerId, messageId);
    }

    public void setLastNotification(final long ownerId, final long messageId) {
        setLong(KEY_NOTIFICATIONS_LAST + "." + ownerId, messageId);
    }
}
