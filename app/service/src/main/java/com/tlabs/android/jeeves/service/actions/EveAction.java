package com.tlabs.android.jeeves.service.actions;

import android.content.Context;
import android.content.ContextWrapper;

import com.tlabs.android.jeeves.data.CacheDatabaseOpenHelper;
import com.tlabs.android.jeeves.data.EvanovaDatabaseOpenHelper;
import com.tlabs.android.jeeves.data.EveDatabaseOpenHelper;
import com.tlabs.android.jeeves.data.MailDatabaseOpenHelper;
import com.tlabs.android.jeeves.model.data.cache.CacheFacade;
import com.tlabs.android.jeeves.model.data.cache.CacheFacadeImpl;
import com.tlabs.android.jeeves.model.data.evanova.EvanovaFacade;
import com.tlabs.android.jeeves.model.data.evanova.EvanovaFacadeImpl;
import com.tlabs.android.jeeves.model.data.sde.EveFacadeImpl;
import com.tlabs.android.jeeves.model.data.social.MailFacade;
import com.tlabs.android.jeeves.model.data.social.MailFacadeImpl;
import com.tlabs.eve.EveRequest;
import com.tlabs.eve.EveResponse;

public class EveAction extends ContextWrapper {
    protected static final String LOG = "EveAction";
    
    private final EveRequest<? extends EveResponse>[] requests;
    private final boolean synchronous;

    protected final EvanovaFacade evanova;
    protected final MailFacade mail;
    protected final CacheFacade cache;

    protected EveAction(final Context context, final EveRequest<? extends EveResponse>... requests) {
        this(context, true, requests);
    }

    protected EveAction(final Context context, final boolean synchronous, final EveRequest<? extends EveResponse>... requests) {
        super(context);
        this.requests = requests;
        this.synchronous = synchronous;

        this.evanova = new EvanovaFacadeImpl(
                EvanovaDatabaseOpenHelper.from(this).getDatabase(),
                new EveFacadeImpl(EveDatabaseOpenHelper.from(this).getDatabase()));
        this.cache = new CacheFacadeImpl(CacheDatabaseOpenHelper.from(this).getDatabase());
        this.mail = new MailFacadeImpl(
                MailDatabaseOpenHelper.from(context).getDatabase(),
                EvanovaDatabaseOpenHelper.from(this).getDatabase());
    }

    public boolean getSynchronous() {
        return synchronous;
    }

    public final EveRequest<? extends EveResponse>[] getRequests() {
        return this.requests;
    }
    
    public EveAction onRequestCompleted(EveRequest<? extends EveResponse> request, EveResponse response) {
        return null;
    }

}
