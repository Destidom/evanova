package com.tlabs.android.jeeves.modules;


import android.content.Context;

import com.tlabs.android.jeeves.data.CacheDatabaseOpenHelper;
import com.tlabs.android.jeeves.data.EvanovaDatabaseOpenHelper;
import com.tlabs.android.jeeves.data.EveDatabaseOpenHelper;
import com.tlabs.android.jeeves.data.FittingDatabaseOpenHelper;
import com.tlabs.android.jeeves.data.MailDatabaseOpenHelper;
import com.tlabs.android.jeeves.model.data.cache.CacheFacade;
import com.tlabs.android.jeeves.model.data.cache.CacheFacadeImpl;
import com.tlabs.android.jeeves.model.data.evanova.EvanovaFacade;
import com.tlabs.android.jeeves.model.data.evanova.EvanovaFacadeImpl;
import com.tlabs.android.jeeves.model.data.fitting.FittingFacade;
import com.tlabs.android.jeeves.model.data.fitting.FittingFacadeImpl;
import com.tlabs.android.jeeves.model.data.sde.EveFacade;
import com.tlabs.android.jeeves.model.data.sde.EveFacadeImpl;
import com.tlabs.android.jeeves.model.data.social.MailFacade;
import com.tlabs.android.jeeves.model.data.social.MailFacadeImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class EveContentModule {

    private Context context;

    public EveContentModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    protected EveFacade provideEveFacade() {
        return new EveFacadeImpl(EveDatabaseOpenHelper.from(this.context).getDatabase());
    }

    @Provides
    protected MailFacade provideMailFacade() {
        return new MailFacadeImpl(
                MailDatabaseOpenHelper.from(this.context).getDatabase(),
                EvanovaDatabaseOpenHelper.from(this.context).getDatabase());
    }

    @Provides
    protected EvanovaFacade provideEvanovaFacade(final EveFacade eve) {
        return new EvanovaFacadeImpl(
                EvanovaDatabaseOpenHelper.from(this.context).getDatabase(),
                eve);
    }

    @Provides
    protected FittingFacade provideFittingFacade() {
        return new FittingFacadeImpl(
                FittingDatabaseOpenHelper.from(this.context).getDatabase(),
                EveDatabaseOpenHelper.from(this.context).getFitting());
    }

    @Provides
    protected CacheFacade provideCacheFacade() {
        return new CacheFacadeImpl(CacheDatabaseOpenHelper.from(this.context).getDatabase());
    }
}
