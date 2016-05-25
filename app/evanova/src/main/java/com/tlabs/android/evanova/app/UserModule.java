package com.tlabs.android.evanova.app;

import android.content.Context;

import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.android.jeeves.network.EveCrest;

import org.devfleet.crest.CrestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class UserModule {

    private static final Logger LOG = LoggerFactory.getLogger(UserModule.class);

    private final Context context;
    private final EveAccount account;

    public UserModule(Context context, EveAccount account) {
        this.account = account;
        this.context = context;
    }

    @Provides
    public EveAccount provideActiveAccount() {
        return account;
    }

    @Provides
    public CrestService provideAuthenticatedCrest() {
        return EveCrest.obtainService(this.context, this.account);
    }
}
