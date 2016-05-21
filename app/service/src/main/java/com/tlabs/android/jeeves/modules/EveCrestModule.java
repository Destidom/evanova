package com.tlabs.android.jeeves.modules;

import android.content.Context;

import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.android.jeeves.network.EveCrest;

import org.apache.commons.lang.StringUtils;
import org.devfleet.crest.CrestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class EveCrestModule {
    private static final Logger LOG = LoggerFactory.getLogger(EveCrestModule.class);

    private final Context context;
    private final EveAccount account;

    private CrestService service;

    public EveCrestModule(Context context, final EveAccount account) {
        this.context = context;
        this.account = account;
        this.service = null;
    }

    @Provides
    public CrestService provideCREST() {
        if (null == this.service) {
            this.service = authenticate(this.context, account);
        }
        return this.service;
    }

    private static CrestService authenticate(final Context context, final EveAccount account) {
        if (null == account || StringUtils.isBlank(account.getRefreshToken())) {
            LOG.debug("No account token available; providing public CREST");
            return EveCrest.service(context);
        }
        return EveCrest.service(context, account);
    }

}
