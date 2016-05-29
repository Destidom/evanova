package com.tlabs.android.jeeves.notifications;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;

import com.tlabs.android.jeeves.model.EveAccount;

import com.tlabs.eve.api.character.CharacterSheet;
import com.tlabs.eve.api.corporation.CorporationSheet;

class Notification {

    private String title;
    private String subtitle;
    private String text;

    private int resIcon;
    private Uri uri;

    private String largeIcon;

    private long when;
    private int count = 1;
    private final int id;

    public Notification(final int id) {
        this.id = id;
        this.count = 1;
        this.title = "Evanova";
        this.text = "Notification from Evanova";
        this.resIcon = R.drawable.ic_notifications;
        this.largeIcon = null;
        this.uri = null;
        this.when = System.currentTimeMillis();
    }

    public Notification(final Notification notification) {
        this.id = notification.id;
        this.count = notification.count;
        this.title = notification.title;
        this.text = notification.text;
        this.resIcon = notification.resIcon;
        this.largeIcon = notification.largeIcon;
        this.uri = notification.uri;
        this.when = notification.when;
        this.subtitle = notification.subtitle;
    }

    public CharSequence getTitle() {
        return title;
    }

    public Notification setTitle(String title) {
        this.title = title;
        return this;
    }

    public CharSequence getText() {
        return text;
    }

    public Notification setText(String text) {
        this.text = text;
        return this;
    }

    public CharSequence getSubtitle() {
        return subtitle;
    }

    public Notification setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public int getSmallIcon() {
        return resIcon;
    }

    public Notification setSmallIcon(int resIcon) {
        this.resIcon = resIcon;
        return this;
    }

    public Uri getUri() {
        return uri;
    }

    public Notification setUri(Uri uri) {
        this.uri = uri;
        return this;
    }

    public String getLargeIcon() {
        return largeIcon;
    }

    public Notification setLargeIcon(String largeIcon) {
        this.largeIcon = largeIcon;
        return this;
    }

    public final long getWhen() {
        return when;
    }

    public Notification setWhen(long when) {
        this.when = when;
        return this;
    }

    public int getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public Notification setCount(int count) {
        this.count = count;
        return this;
    }

    public static Notification from(final Context context, final EveAccount account) {
        final Uri uri = Notifications.KEYS.buildUpon().appendPath("key").appendPath(Long.toString(account.getId())).build();
        return new Notification((int) account.getId()).
                setTitle(account.getName()).
                setUri(uri);
    }

    public static Notification from(final Context context, final CharacterSheet sheet) {
        return new Notification((int) sheet.getCharacterID()).
                setTitle(sheet.getCharacterName()).
                setSubtitle(sheet.getCharacterName()).
                setLargeIcon(NotificationImages.getCharacterIconURL(context.getApplicationContext(), sheet.getCharacterID())).
                setUri(ContentUris.withAppendedId(Notifications.CHARACTER, sheet.getCharacterID()));
    }

    public static Notification from(final Context context, final CorporationSheet sheet) {
        return new Notification((int)sheet.getCorporationID()).
                setTitle(sheet.getCorporationName()).
                setSubtitle(sheet.getCorporationName()).
                setLargeIcon(NotificationImages.getCorporationIconURL(context.getApplicationContext(), sheet.getCorporationID())).
                setUri(ContentUris.withAppendedId(Notifications.CORPORATION, sheet.getCorporationID()));
    }
}
