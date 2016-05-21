package com.tlabs.android.evanova.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;

import com.tlabs.android.jeeves.service.EveAPIService;
import com.tlabs.android.jeeves.service.events.EveApiEvent;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.parceler.Parcels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class ContentPublisher extends BroadcastReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(ContentPublisher.class);

    private final Context context;
    private final PublishSubject<EveApiEvent> publisher;

    public ContentPublisher(final Context context) {
        this.context = context;

        final IntentFilter filter = new IntentFilter(EveApiEvent.class.getName());
        LocalBroadcastManager.getInstance(this.context).registerReceiver(this, filter);

        this.publisher = PublishSubject.create();
    }

    public final void publish(final EveApiEvent event) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("sendEveApiEvent: {} ", ToStringBuilder.reflectionToString(event));
        }
        final Intent intent = new Intent(this.context, EveAPIService.class);
        intent.putExtra("event", Parcels.wrap(event));
        this.context.startService(intent);
    }

    public Observable<EveApiEvent> getObservable() {
        return this.publisher.share().subscribeOn(Schedulers.io());
    }

    public void dispose() {
        LocalBroadcastManager.getInstance(this.context).unregisterReceiver(this);
        this.publisher.onCompleted();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Parcelable p = intent.getParcelableExtra("event");
        if (null == p) {
            LOG.debug("onReceive: null parcel");
            return;
        }

        final EveApiEvent event = Parcels.unwrap(p);
        if (LOG.isDebugEnabled()) {
            LOG.debug("onReceive: {}", ToStringBuilder.reflectionToString(event));
        }

        this.publisher.onNext(event);
    }

}
