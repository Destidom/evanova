package com.tlabs.android.jeeves.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.Html;

import com.tlabs.android.jeeves.data.CacheDatabaseOpenHelper;
import com.tlabs.android.jeeves.data.EvanovaDatabaseOpenHelper;
import com.tlabs.android.jeeves.data.EveDatabaseOpenHelper;
import com.tlabs.android.jeeves.data.MailDatabaseOpenHelper;
import com.tlabs.android.jeeves.model.EveAccount;

import com.tlabs.android.jeeves.model.data.cache.CacheFacade;
import com.tlabs.android.jeeves.model.data.cache.CacheFacadeImpl;
import com.tlabs.android.jeeves.model.data.evanova.EvanovaDatabase;
import com.tlabs.android.jeeves.model.data.evanova.EvanovaFacade;
import com.tlabs.android.jeeves.model.data.evanova.EvanovaFacadeImpl;
import com.tlabs.android.jeeves.model.data.sde.EveDatabase;

import com.tlabs.android.jeeves.model.data.sde.EveFacadeImpl;
import com.tlabs.android.jeeves.model.data.social.MailFacade;
import com.tlabs.android.jeeves.model.data.social.MailFacadeImpl;
import com.tlabs.android.util.Log;

import com.tlabs.eve.api.character.CharacterSheet;
import com.tlabs.eve.api.corporation.CorporationSheet;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public final class Notifications {
    private static final String LOG = "Notifications";
    private static final Uri URI = Uri.parse("content://com.tlabs.android.evanova");

    public static final Uri KEYS = URI.buildUpon().appendPath("keys").build();
    public static final Uri CHARACTER = URI.buildUpon().appendPath("char").build();
    public static final Uri CORPORATION = URI.buildUpon().appendPath("corp").build();

    private final NotificationManager nm;
    private final Context context;
    private Uri soundUri = null;

    private final EvanovaFacade evanova;
    private final MailFacade mail;
    private final CacheFacade cache;

    private Notifications(final Context context) {
        this.context = context;
        this.nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (null == this.nm) {
            Log.e(LOG, "NotificationManager not available.");
        }

        final EveDatabase eveDatabase = EveDatabaseOpenHelper.from(context).getDatabase();
        final EvanovaDatabase evanovaDatabase = EvanovaDatabaseOpenHelper.from(context).getDatabase();

        this.evanova = new EvanovaFacadeImpl(
                evanovaDatabase,
                new EveFacadeImpl(eveDatabase));

        this.cache = new CacheFacadeImpl(CacheDatabaseOpenHelper.from(context).getDatabase());
        this.mail = new MailFacadeImpl(
                MailDatabaseOpenHelper.from(context).getDatabase(),
                evanovaDatabase);
    }

    public static Notifications from(final Context context) {
        return new Notifications(context.getApplicationContext());
    }

    public Notifications withSound(final Uri soundUri) {
        this.soundUri = soundUri;
        return this;
    }

    public void post(final EveAccount account) {
        if (null == this.nm) {
            return;
        }
        if (null == account) {
            return;
        }
        postNotifications(new AccountInspector(this.context, evanova, account).inspect());
    }

    public void post(final CorporationSheet sheet) {
        if (null == this.nm) {
            return;
        }
        if (null == sheet) {
            return;
        }
        postNotifications(new CorporationContractsInspector(this.context, evanova, sheet).inspect());
        postNotifications(new CorporationMailsInspector(this.context, mail, cache, sheet).inspect());
        postNotifications(new CorporationNotificationsInspector(this.context, mail, cache, sheet).inspect());
    }

    public void post(final CharacterSheet sheet) {
        if (null == this.nm) {
            return;
        }
        if (null == sheet) {
            return;
        }
        postNotifications(new CharacterTrainingInspector(this.context, evanova, sheet).inspect());
        postNotifications(new CharacterContractsInspector(this.context, evanova, sheet).inspect());
        postNotifications(new CharacterMailsInspector(this.context, mail, cache, sheet).inspect());
        postNotifications(new CharacterNotificationsInspector(this.context, mail, cache, sheet).inspect());
        postNotifications(new CharacterCalendarInspector(this.context, mail, cache, sheet).inspect());
    }

    private void postNotifications(final List<Notification> notifications) {
        Log.d(LOG, "postNotifications: " + notifications.size());
        if (notifications.isEmpty()) {
            return;
        }
        for (Notification n: notifications) {
            postNotification(n);
        }
    }

    private void postNotification(final Notification notification) {
        if (Log.D) {
            Log.d(LOG, "postNotification: " + ToStringBuilder.reflectionToString(notification));
        }
        if (StringUtils.isBlank(notification.getLargeIcon())) {
            postNotificationImpl(notification, null);
        }
        else {
            subscribe(() -> NotificationImages.getNotificationImage(context, notification.getLargeIcon()),
            bitmap -> postNotificationImpl(notification, bitmap));
        }
    }

    private void postNotificationImpl(final Notification notification, final Bitmap largeIcon) {
        final NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();

       /* style.setSummaryText(notification.getSummary());*/
        final String[] split = StringUtils.split(notification.getText().toString(), "\n");
        for (String s: split) {
            String cleared = StringUtils.remove(StringUtils.remove(StringUtils.remove(StringUtils.remove(StringUtils.remove(s, "<br>"), "<br/>"), "\n"), "<p>"), "</p>");
            if (StringUtils.isNotBlank(cleared)) {
                style.addLine(Html.fromHtml(cleared));
            }
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context).
                        setOnlyAlertOnce(true).
                        setAutoCancel(false).
                        setSmallIcon(notification.getSmallIcon(), 0).
                        setLights(0, 0, 0). //notification LED off
                        setShowWhen(true).
                        setWhen(notification.getWhen()).
                        setContentTitle(notification.getTitle()).
                        setNumber(notification.getCount()).
                        //setContentText(notification.getText()).
                        setSubText(notification.getSubtitle()).
                        setContentIntent(createPendingIntent(context, notification.getUri(), notification.getId())).
                        setStyle(style);
        if (null != this.soundUri) {
            builder.setSound(this.soundUri, AudioManager.STREAM_NOTIFICATION);
        }

        if (null != largeIcon) {
            builder.setLargeIcon(largeIcon);
        }

        nm.notify(notification.getId(), builder.build());
        if (Log.D) {
            Log.d(LOG, "postNotificationImpl: " + ToStringBuilder.reflectionToString(notification, ToStringStyle.SHORT_PREFIX_STYLE));
        }
    }

    private static PendingIntent createPendingIntent(
            final Context context,
            final Uri launcherUri,
            final int intentId) {
        final Intent notificationIntent = new Intent();
        notificationIntent.setClassName(
                "com.tlabs.android.evanova",
                "com.tlabs.android.evanova.activity.launcher.UriLauncherActivity");
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        notificationIntent.setFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        //Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        notificationIntent.setData(launcherUri);

        return PendingIntent.getActivity(
                context,
                intentId,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static <T> Observable<T> defer(Func0<T> f) {
        final Observable<T> observable =
                Observable.defer(() -> Observable.just(f.call()));
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private static <T> Subscription subscribe(Func0<T> f, Action1<T> action1) {
        final Observable<T> observable = defer(f);
        return observable.subscribe(action1::call);
    }
}
