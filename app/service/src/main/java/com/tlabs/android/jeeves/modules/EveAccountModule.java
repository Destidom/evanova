package com.tlabs.android.jeeves.modules;


import com.tlabs.android.jeeves.model.EveAccount;

import dagger.Module;
import dagger.Provides;

@Module
public class EveAccountModule {

    private EveAccount eveAccount;

    public EveAccountModule(EveAccount eveAccount) {
        this.eveAccount = eveAccount;
    }

    @Provides
    public EveAccount provideEveAccount() {
        return this.eveAccount;
    }
}
