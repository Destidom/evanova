package com.tlabs.android.jeeves.service;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import com.tlabs.android.jeeves.data.CacheDatabaseOpenHelper;
import com.tlabs.android.jeeves.data.EvanovaDatabaseOpenHelper;
import com.tlabs.android.jeeves.data.EveDatabaseOpenHelper;
import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.android.jeeves.model.data.cache.CacheFacade;
import com.tlabs.android.jeeves.model.data.cache.CacheFacadeImpl;
import com.tlabs.android.jeeves.model.data.evanova.EvanovaFacade;
import com.tlabs.android.jeeves.model.data.evanova.EvanovaFacadeImpl;
import com.tlabs.android.jeeves.model.data.sde.EveFacade;
import com.tlabs.android.jeeves.model.data.sde.EveFacadeImpl;
import com.tlabs.android.jeeves.notifications.EveNotificationPreferences;
import com.tlabs.android.jeeves.notifications.Notifications;
import com.tlabs.android.jeeves.service.events.CharacterUpdateEndEvent;
import com.tlabs.android.jeeves.service.events.CharacterUpdateStartEvent;
import com.tlabs.android.jeeves.service.events.CorporationUpdateEndEvent;
import com.tlabs.android.jeeves.service.events.CorporationUpdateStartEvent;
import com.tlabs.android.jeeves.service.events.EveAccountUpdateEndEvent;
import com.tlabs.android.jeeves.service.events.EveAccountUpdateStartEvent;
import com.tlabs.android.jeeves.service.events.EveApiEvent;
import com.tlabs.android.util.Log;
import com.tlabs.eve.api.character.CharacterSheet;
import com.tlabs.eve.api.corporation.CorporationSheet;

import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.Date;

public class EveNotificationService extends Service {

    private static final String ALARM_CHARACTER = "com.tlabs.android.evanova.character.ALARM";
    private static final String ALARM_CORPORATION = "com.tlabs.android.evanova.corporation.ALARM";
    private static final String ALARM_ACCOUNT = "com.tlabs.android.evanova.account.ALARM";

    private static final String LOG = "EveNotificationService";

    private EveApiServiceHelper helper;
    private EvanovaFacade evanova;
    private EveNotificationPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();

        final EveFacade eve = new EveFacadeImpl(EveDatabaseOpenHelper.from(this).getDatabase());
        this.evanova =
                new EvanovaFacadeImpl(EvanovaDatabaseOpenHelper.from(this).getDatabase(), eve);
        final CacheFacade cache = new CacheFacadeImpl(CacheDatabaseOpenHelper.from(this).getDatabase());
        this.helper = new EveApiServiceHelper(this, cache, evanova);
        this.preferences = new EveNotificationPreferences(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //Receives alarm intents - execute updates - show notifications as necessary
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == intent) {
            return START_NOT_STICKY;
        }
        if (Log.D) {
            Log.d(LOG, "onStartCommand: " + intent.getAction() + ":" + intent.getData());
        }
        handleIntent(intent);
        return START_NOT_STICKY;
    }

    private void handleIntent(Intent intent) {
        for (final EveApiEvent e: map(intent)) {
            helper.execute(e, new Messenger(new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.obj instanceof CharacterUpdateEndEvent) {
                        final CharacterUpdateEndEvent event = (CharacterUpdateEndEvent) msg.obj;
                        notifyCharacterUpdate(event.getCharID());
                        return;
                    }
                    if (msg.obj instanceof CorporationUpdateEndEvent) {
                        final CorporationUpdateEndEvent event = (CorporationUpdateEndEvent) msg.obj;
                        notifyCorporationUpdate(event.getCorporationID());
                        return;
                    }
                    if (msg.obj instanceof EveAccountUpdateEndEvent) {
                        final EveAccountUpdateEndEvent event = (EveAccountUpdateEndEvent) msg.obj;
                        notifyAccountUpdate(event.getAccountID());
                        return;
                    }
                }
            }));
        }
    }

    public static Intent getCharacterAlarm(final Context context, final long charID) {
        final Intent intent = new Intent(context, EveNotificationService.class);
        intent.setAction(ALARM_CHARACTER);
        intent.setData(Uri.parse("evanova://alarm/character/" + charID));
        return intent;
    }

    public static Intent getCorporationAlarm(final Context context, final long corpID) {
        final Intent intent = new Intent(context, EveNotificationService.class);
        intent.setAction(ALARM_CORPORATION);
        intent.setData(Uri.parse("evanova://alarm/corporation/" + corpID));
        return intent;
    }

    public static Intent getAccountAlarm(final Context context, final long accountID) {
        final Intent intent = new Intent(context, EveNotificationService.class);
        intent.setAction(ALARM_ACCOUNT);
        intent.setData(Uri.parse("evanova://alarm/accounts/" + accountID));
        return intent;
    }

    public static void setAlarms(final Context context, final boolean reset) {

        final EveFacade eve = new EveFacadeImpl(EveDatabaseOpenHelper.from(context).getDatabase());
        EvanovaFacade facade = new EvanovaFacadeImpl(EvanovaDatabaseOpenHelper.from(context).getDatabase(), eve);

        for (Long id: facade.listAccounts()) {
            final Intent intent = getAccountAlarm(context, id);
            if (reset) {
                cancelAlarm(context, intent);
            }
            scheduleAlarm(context, intent);
        }
        for (Long id: facade.getCharacters()) {
            final Intent intent = getCharacterAlarm(context, id);
            if (reset) {
                cancelAlarm(context, intent);
            }
            scheduleAlarm(context, intent);
        }
        for (Long id: facade.getCorporations()) {
            final Intent intent = getCorporationAlarm(context, id);
            if (reset) {
                cancelAlarm(context, intent);
            }
            scheduleAlarm(context, intent);
        }
    }

    public static void scheduleAlarm(final Context context, final Intent intent) {
        final AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        if (null == am) {
            Log.w(LOG, "scheduleAlarm AlarmManager not available.");
            return;
        }

        final EveNotificationPreferences preferences = new EveNotificationPreferences(context);

        final PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
        final long delay = preferences.getAlarmDelay();

        if (null == pendingIntent) {
            if (delay > 0) {
                final PendingIntent newIntent = PendingIntent.getService(context, 0, intent, 0);
                final long when = System.currentTimeMillis() + delay;
                am.setInexactRepeating(
                        AlarmManager.RTC,
                        when,
                        delay,
                        newIntent);
                if (Log.D) {
                    Log.d(LOG, "scheduleAlarm: scheduled " + intent.getData() + " for " + new Date(when));
                }
            }
            else {
                Log.w(LOG, "scheduleAlarm: no Pending intent and no delay (delay=" + delay + ")");
            }
        }
        else {
            if (delay <= 0) {
                am.cancel(pendingIntent);
                pendingIntent.cancel();
                if (Log.D) {
                    Log.d(LOG, "scheduleAlarm: cancelled " + intent.getData());
                }
            }
            else {
                if (Log.D) {
                    Log.d(LOG, "scheduleAlarm: unchanged " + intent.getData());
                }
            }
        }
    }

    private static void cancelAlarm(final Context context, final Intent intent) {
        final AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        if (null == am) {
            Log.w(LOG, "cancelAlarm AlarmManager not available.");
            return;
        }

        final PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
        if (null != pendingIntent) {
            am.cancel(pendingIntent);
            pendingIntent.cancel();
            if (Log.D) {
                Log.d(LOG, "cancelAlarm:" + " cancelled " + intent.getData());
            }
        }
    }

    private void notifyCharacterUpdate(final long charId) {
        if (Log.D) {
            Log.d(LOG, "notifyCharacterUpdate " + charId);
        }
        if (!preferences.getNotificationOption(charId)) {
            cancelNotification(this, charId);
            if (Log.D) {
                Log.d(LOG, "notifyCharacterUpdate ignored " + charId);
            }
            return;
        }

        final CharacterSheet character = evanova.getCharacterSheet(charId);
        if (null == character) {
            cancelNotification(this, charId);
            cancelAlarm(this, getCharacterAlarm(this, charId));
            if (Log.D) {
                Log.d(LOG, "notifyCharacterUpdate cancelled " + charId);
            }
            return;
        }
        Notifications.
                from(this).
                withSound(preferences.getNotificationSound()).
                post(character);
    }

    private void notifyCorporationUpdate(final long corpId) {
        if (Log.D) {
            Log.d(LOG, "notifyCorporationUpdate " + corpId);
        }
        if (!preferences.getNotificationOption(corpId)) {
            cancelNotification(this, corpId);
            if (Log.D) {
                Log.d(LOG, "notifyCorporationUpdate ignored " + corpId);
            }
            return;
        }

        final CorporationSheet corpInfo = evanova.getCorporationSheet(corpId);
        if (null == corpInfo) {
            cancelNotification(this, corpId);
            cancelAlarm(this, getCorporationAlarm(this, corpId));
            if (Log.D) {
                Log.d(LOG, "notifyCorporationUpdate cancelled " + corpId);
            }
            return;
        }
        Notifications.
                from(this).
                withSound(preferences.getNotificationSound()).
                post(corpInfo);
    }

    private void notifyAccountUpdate(final long id) {
        if (Log.D) {
            Log.d(LOG, "notifyAccountUpdate " + id);
        }
        if (!preferences.getNotificationOption(id)) {
            cancelNotification(this, id);
            if (Log.D) {
                Log.d(LOG, "notifyAccountUpdate ignored " + id);
            }
            return;
        }

        final EveAccount account = evanova.getAccount(id);
        if (null == account) {
            cancelNotification(this, id);
            cancelAlarm(this, getAccountAlarm(this, id));
            if (Log.D) {
                Log.d(LOG, "notifyAccountUpdate cancelled " + id);
            }
            return;
        }
        Notifications.
                from(this).
                withSound(preferences.getNotificationSound()).
                post(account);
    }

    private static void cancelNotification(Context context, long ownerId) {
        final NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (null == nm) {
            return;
        }
        nm.cancel((int) ownerId);
    }

    private static Iterable<EveApiEvent> map(Intent intent) {
        final String param = null == intent.getData() ? null : intent.getData().getLastPathSegment();
        if (StringUtils.isBlank(param) || !StringUtils.isNumeric(param)) {
            Log.w(LOG, "Invalid id in Intent " + intent.getAction());
            return Collections.emptyList();
        }

        final long id = Long.parseLong(param);

        if (ALARM_CHARACTER.equalsIgnoreCase(intent.getAction())) {
            return Collections.singletonList(new CharacterUpdateStartEvent(id, false));
        }
        else if (ALARM_CORPORATION.equalsIgnoreCase(intent.getAction())) {
            return Collections.singletonList(new CorporationUpdateStartEvent(id, false));
        }
        else if (ALARM_ACCOUNT.equalsIgnoreCase(intent.getAction())) {
            return Collections.singletonList(new EveAccountUpdateStartEvent(id, false));
        }
        return Collections.emptyList();
    }
}
