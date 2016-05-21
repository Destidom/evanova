package com.tlabs.android.jeeves.notifications;

import android.content.Context;
import android.net.Uri;

import com.tlabs.android.util.PreferenceSupport;

import org.apache.commons.lang.StringUtils;

public final class EveNotificationPreferences extends PreferenceSupport {

    public static final String KEY_FREQUENCY = "preferences.notifications.frequency";

    public static final int MANUAL = 0;
    public static final int MN_15 = 1;
    public static final int HALF_HOUR = 2;
    public static final int HOUR = 3;
    public static final int TWICE_DAILY = 4;
    public static final int DAILY = 5;

    public static final String KEY_NOTIFICATION_OPTION = "preferences.notifications.option";
    public static final String KEY_NOTIFICATION_SOUND = "preferences.notifications.sound";
    public static final String KEY_NOTIFICATION_ACCOUNT_STATUS = "preferences.notifications.account.status";
    public static final String KEY_NOTIFICATION_ACCOUNT_TRAINING = "preferences.notifications.account.training";

    public static final String KEY_NOTIFICATION_TRAINING = "preferences.notifications.training";
    public static final String KEY_NOTIFICATION_MAIL = "preferences.notifications.mail";
    public static final String KEY_NOTIFICATION_NOTIFICATIONS = "preferences.notifications.notifications";
    public static final String KEY_NOTIFICATION_CALENDAR = "preferences.notifications.calendar";
    public static final String KEY_NOTIFICATION_CONTRACTS = "preferences.notifications.contracts";

    private static final String CALENDAR_EVENT =  "saved.notifications.calendar.event.id.";

    public EveNotificationPreferences(Context context) {
        super(context);
    }

    public void setDefaultOptions() {
        setAlarmOption(TWICE_DAILY);
    }

    public void addCalendarEvent(final long eventID) {
        setLong(CALENDAR_EVENT + eventID, eventID);
    }

    public void removeCalendarEvent(final long eventID) {
        remove(CALENDAR_EVENT + eventID);
    }

    public long getCalendarEvent(final long eventID) {
        return getLong(CALENDAR_EVENT + eventID, -1);
    }

    public boolean getAccountStatusNotificationOption() {
        return getBoolean(KEY_NOTIFICATION_ACCOUNT_STATUS, true);
    }

    public boolean getAccountStatusNotificationOption(final long profileId) {
        return getAccountStatusNotificationOption() && getBoolean(KEY_NOTIFICATION_ACCOUNT_STATUS + profileId, true);
    }

    public void setAcountStatusNotificationOption(final long profileId, final boolean notify) {
        setBoolean(KEY_NOTIFICATION_ACCOUNT_STATUS + profileId, notify);
    }

    public boolean getAccountTrainingNotificationOption() {
        return getBoolean(KEY_NOTIFICATION_ACCOUNT_TRAINING, true);
    }

    public boolean getAccountTrainingNotificationOption(final long profileId) {
        return getAccountTrainingNotificationOption() && getBoolean(KEY_NOTIFICATION_ACCOUNT_TRAINING + profileId, true);
    }

    public void setAccountTrainingNotificationOption(final long profileId, final boolean notify) {
        setBoolean(KEY_NOTIFICATION_ACCOUNT_TRAINING + profileId, notify);
    }

    public boolean getCharacterTrainingNotificationOption() {
        return getBoolean(KEY_NOTIFICATION_TRAINING, true);
    }

    public boolean getCharacterTrainingNotificationOption(final long profileId) {
        return getCharacterTrainingNotificationOption() && getBoolean(KEY_NOTIFICATION_TRAINING + profileId, true);
    }

    public void setCharacterTrainingNotificationOption(final long profileId, final boolean notify) {
        setBoolean(KEY_NOTIFICATION_TRAINING + profileId, notify);
    }

    public boolean getMailNotificationOption() {
        return getBoolean(KEY_NOTIFICATION_MAIL, true);
    }

    public boolean getMailNotificationOption(final long profileId) {
        return getMailNotificationOption() && getBoolean(KEY_NOTIFICATION_MAIL + profileId, true);
    }

    public void setMailNotificationOption(final long profileId, final boolean notify) {
        setBoolean(KEY_NOTIFICATION_MAIL + profileId, notify);
    }

    public boolean getMessageNotificationOption() {
        return getBoolean(KEY_NOTIFICATION_NOTIFICATIONS, true);
    }

    public boolean getMessageNotificationOption(final long profileId) {
        return getMessageNotificationOption() && getBoolean(KEY_NOTIFICATION_NOTIFICATIONS + profileId, true);
    }

    public void setMessageNotificationOption(final long profileId, final boolean notify) {
        setBoolean(KEY_NOTIFICATION_NOTIFICATIONS + profileId, notify);
    }

    public boolean getCalendarNotificationOption() {
        return getBoolean(KEY_NOTIFICATION_CALENDAR, true);
    }

    public boolean getCalendarNotificationOption(final long profileId) {
        return getCalendarNotificationOption() && getBoolean(KEY_NOTIFICATION_CALENDAR + profileId, true);
    }

    public void setCalendarNotificationOption(final long profileId, final boolean notify) {
        setBoolean(KEY_NOTIFICATION_CALENDAR + profileId, notify);
    }

    public boolean getContractNotificationOption() {
        return getBoolean(KEY_NOTIFICATION_CONTRACTS, true);
    }

    public boolean getContractNotificationOption(final long profileId) {
        return getContractNotificationOption() && getBoolean(KEY_NOTIFICATION_CONTRACTS + profileId, true);
    }

    public void setContractNotificationOption(final long profileId, final boolean notify) {
        setBoolean(KEY_NOTIFICATION_CONTRACTS + profileId, notify);
    }

    public void setNotificationOption(final long profileId, final boolean notify) {
        setBoolean(KEY_NOTIFICATION_OPTION + profileId, notify);
    }

    public boolean getNotificationOption(final long profileId) {
        return getNotificationOption() && getBoolean(KEY_NOTIFICATION_OPTION + profileId, true);
    }

    public boolean getNotificationOption() {
        return getBoolean(KEY_NOTIFICATION_OPTION, true);
    }

    public Uri getNotificationSound() {
        String uri = getString(KEY_NOTIFICATION_SOUND, null);
        if (StringUtils.isBlank(uri)) {
            return null;
        }
        return Uri.parse(uri);
    }

    public void setAlarmOption(final int option) {
        setString(KEY_FREQUENCY, Integer.toString(option));
    }

    public void setAlarmOption(final String option) {
        setString(KEY_FREQUENCY, option);
    }

    public int getAlarmOption() {
        return getInt(getPreferences(), KEY_FREQUENCY, HOUR);
    }

    private static final long MINUTE_INMS = 1000L * 60L;
    public long getAlarmDelay() {
        switch (getAlarmOption()) {
            case DAILY:
                return 24L * 60L * MINUTE_INMS;
            case HALF_HOUR:
                return 30L * MINUTE_INMS;
            case HOUR:
                return 60L * MINUTE_INMS;
            case MN_15:
                return 15L * MINUTE_INMS;
            case TWICE_DAILY:
                return 12L * 60L * MINUTE_INMS;
            case MANUAL:
                return -1;
            default:
                return 30L * MINUTE_INMS;
        }
    }

}
