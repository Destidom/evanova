package com.tlabs.android.jeeves.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tlabs.android.jeeves.data.EvanovaDatabaseOpenHelper;
import com.tlabs.android.jeeves.data.EveDatabaseOpenHelper;
import com.tlabs.android.jeeves.model.data.evanova.EvanovaFacade;
import com.tlabs.android.jeeves.model.data.evanova.EvanovaFacadeImpl;
import com.tlabs.android.jeeves.model.data.sde.EveFacade;
import com.tlabs.android.jeeves.model.data.sde.EveFacadeImpl;
import com.tlabs.android.jeeves.service.EveNotificationService;
import com.tlabs.android.util.Log;

public class EveNotificationReceiver extends BroadcastReceiver {
    private static final String LOG = "EveNotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Log.D) {
            Log.d(LOG, "onReceive: " + intent.getAction());
        }
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            onWakeUp(context);
            return;
        }
    }

    private static void onWakeUp(Context context) {

        final EveFacade eve = new EveFacadeImpl(EveDatabaseOpenHelper.from(context).getDatabase());
        final EvanovaFacade evanova =
                new EvanovaFacadeImpl(EvanovaDatabaseOpenHelper.from(context).getDatabase(), eve);

        for (Long id : evanova.listAccounts()) {
            final Intent alarm = EveNotificationService.getAccountAlarm(context, id);
            EveNotificationService.scheduleAlarm(context, alarm);
        }
        for (Long id : evanova.getCharacters()) {
            final Intent alarm = EveNotificationService.getCharacterAlarm(context, id);
            EveNotificationService.scheduleAlarm(context, alarm);
        }
        for (Long id : evanova.getCorporations()) {
            final Intent alarm = EveNotificationService.getCorporationAlarm(context, id);
            EveNotificationService.scheduleAlarm(context, alarm);
        }
    }
}
