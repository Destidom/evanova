package com.tlabs.android.jeeves.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcelable;

import com.tlabs.android.jeeves.data.CacheDatabaseOpenHelper;
import com.tlabs.android.jeeves.data.EvanovaDatabaseOpenHelper;
import com.tlabs.android.jeeves.data.EveDatabaseOpenHelper;
import com.tlabs.android.jeeves.model.data.cache.CacheFacade;
import com.tlabs.android.jeeves.model.data.cache.CacheFacadeImpl;
import com.tlabs.android.jeeves.model.data.evanova.EvanovaFacade;
import com.tlabs.android.jeeves.model.data.evanova.EvanovaFacadeImpl;
import com.tlabs.android.jeeves.model.data.sde.EveFacade;
import com.tlabs.android.jeeves.model.data.sde.EveFacadeImpl;
import com.tlabs.android.jeeves.service.events.EveApiEvent;
import com.tlabs.android.util.Log;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.parceler.Parcels;

public final class EveAPIService extends IntentService {

    private static final String LOG = "EveAPIService";

    private EveApiServiceHelper helper;

    public EveAPIService() {
        super("Evanova API Service");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        final EveFacade eve = new EveFacadeImpl(EveDatabaseOpenHelper.from(this).getDatabase());
        final EvanovaFacade evanova =
                new EvanovaFacadeImpl(EvanovaDatabaseOpenHelper.from(this).getDatabase(), eve);
        final CacheFacade cache = new CacheFacadeImpl(CacheDatabaseOpenHelper.from(this).getDatabase());
        this.helper = new EveApiServiceHelper(this, cache, evanova);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final Parcelable p = intent.getParcelableExtra("event");
        if (null == p) {
            Log.w(LOG, "Null 'event' parcel in intent.");
        }
        final EveApiEvent event = Parcels.unwrap(p);
        if (Log.D) {
            Log.d(LOG, "onHandleIntent event=" + ToStringBuilder.reflectionToString(event));
        }
        this.helper.execute(event, null);
    }
}
